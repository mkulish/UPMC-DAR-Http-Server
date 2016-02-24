package edu.upmc.dar.server;

import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.dispatch.RequestHandler;
import edu.upmc.dar.server.dispatch.RequestMatch;
import edu.upmc.dar.server.dispatch.ServletContainer;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;
import edu.upmc.dar.server.html.page.EchoPage;
import edu.upmc.dar.server.servlet.page.InternalErrorPageServlet;
import edu.upmc.dar.server.servlet.page.NotFoundPageServlet;
import edu.upmc.dar.server.util.JsonUtil;
import edu.upmc.dar.server.util.Log;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {
    public static final int DEFAULT_PORT = 7707;
    public static final int THREADS_POOL_SIZE = 5;
    public static final int MAX_CONNECTIONS = 10;
    public static final String DEPLOY_FOLDER_PATH = "deploy";


    public static volatile ServletContainer servletContainer = new ServletContainer();
    public static NotFoundPageServlet notFoundPageServlet = new NotFoundPageServlet();
    public static InternalErrorPageServlet internalErrorPageServlet = new InternalErrorPageServlet();

    /**
     * Server's entry method
     * Initializes the executors pool and starts the HTTP server
     * @param args command line args array
     */
    public static void main(String[] args) {
        int port = (args.length < 1) ? DEFAULT_PORT : Integer.parseInt(args[0]);
        int poolSize = (args.length < 2) ? THREADS_POOL_SIZE : Integer.parseInt(args[1]);
        int maxConnections = (args.length < 3) ? MAX_CONNECTIONS : Integer.parseInt(args[2]);

        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        try{
            Log.info("Initializing servlets..");
            initServlets();
            Log.info("Servlets initialization completed successfully\n");

            Log.info("Initializing deployment listener..");
            initDeployChecker();
            Log.info("Deployment listener initialized\n");

            ServerSocket Server = new ServerSocket(port, maxConnections, InetAddress.getByName("127.0.0.1"));
            Log.info("HTTP server started on port " + port + "\n");

            while(true) {
                try {
                    pool.execute(new RequestHandler(Server.accept()));
                } catch (Exception ex){
                    ex.printStackTrace();
                    break;
                }
            }
        } catch(Throwable ex){
            ex.printStackTrace();
        } finally {
            pool.shutdown();
            Log.info("HTTP server on port " + port + " is shut down");
        }
    }

    /**
     * Manually register the initial servlets
     */
    private static void initServlets(){
        servletContainer.registerServlet(new RequestMatch("/echo/text"), new HttpServlet() {
            @Override
            public void serve(HttpRequest request, HttpResponse response) {
                response.setContentType(ContentType.TEXT_PLAIN);
                response.setBody(request.toString());
            }
        });

        servletContainer.registerServlet(new RequestMatch("/echo/html"), new HttpServlet() {
            @Override
            public void serve(HttpRequest request, HttpResponse response) {
                response.setContentType(ContentType.TEXT_HTML);
                response.setBody(EchoPage.instance().produceHtml(request));
            }
        });

        servletContainer.registerServlet(new RequestMatch("/echo/json"), new HttpServlet() {
            @Override
            public void serve(HttpRequest request, HttpResponse response) throws Exception {
                response.setContentType(ContentType.APP_JSON);
                response.setBody(JsonUtil.serializeRequest(request));
            }
        });
    }

    /**
     * Initializes the listener that checks when new .jar files are placed in the deploy directory
     * Loads new applications from all found jars
     */
    private static void initDeployChecker(){
        File deployFolder = new File(DEPLOY_FOLDER_PATH);
        if(! deployFolder.exists()){
            if(! deployFolder.mkdir()){
                Log.err("Couldn't create the deployment folder under " + deployFolder.getAbsolutePath());
            }
        }

        //TODO use file system's listeners to track when new files are added to the deploy directory

        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkForDeployments();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    /**
     * Checks if there are new jars in the deployment folder and runs the installation
     * @throws Exception
     */
    private static void checkForDeployments() throws Exception {
        File deployFolder = new File(DEPLOY_FOLDER_PATH);
        if(deployFolder.exists() && deployFolder.isDirectory()){
            for(File archive : deployFolder.listFiles((dir, name) -> name.endsWith(".jar"))){
                Log.info("Deployment listener: loading the application " + archive.getName());
                processJarFile(archive);

                if(! archive.delete()){
                    Log.err("Deployment listener: couldn't remove the file " + archive.getName());
                }
            }
        }
    }

    /**
     * Performs applications installation given the jar archive
     * @param file application archive
     * @throws Exception
     */
    private static void processJarFile(File file) throws Exception {
        JarFile jarFile = new JarFile(file.getPath());
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + file.getPath() +"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            String className = je.getName().substring(0, je.getName().length() - ".class".length()).replace('/', '.');
            processNewClass(cl.loadClass(className));
        }

        Log.info("The application " + file.getName() + " was successfully deployed\n");
        cl.close();
        jarFile.close();
    }

    /**
     * Checks if the class is annotated as a servlet
     * If so, creates it's new instance and registers it in the servlet container using the matching params,
     * removing the servlet that was already registered in the container under the same mapping
     * @param newClass new class that is potentially a servlet
     * @throws Exception
     */
    private static void processNewClass(Class<?> newClass) throws Exception {
        if(HttpServlet.class.isAssignableFrom(newClass)) {
            for (Annotation annotation : newClass.getDeclaredAnnotations()) {
                if (annotation.annotationType() == Servlet.class) {
                    Servlet servletAnnotation = (Servlet) annotation;
                    String url = servletAnnotation.url();
                    RequestMethod method = servletAnnotation.method();
                    ContentType contentType = servletAnnotation.produces();

                    RequestMatch matchingCriteria = new RequestMatch(url).setMethod(method);
                    HttpServlet servletInstance = (HttpServlet)newClass.newInstance();
                    servletInstance.setContentType(contentType);

                    Log.info("Registering servlet " + newClass.getName() + ", method: " + method + ", url: " + url + ", produces: " + contentType);
                    servletContainer.unregisterServlet(matchingCriteria);
                    servletContainer.registerServlet(matchingCriteria, servletInstance);
                }
            }
        }
    }
}
