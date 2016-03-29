package edu.upmc.dar.server;

import edu.upmc.dar.server.common.annotation.Servlet;
import edu.upmc.dar.server.common.enumeration.ContentType;
import edu.upmc.dar.server.common.enumeration.RequestMethod;
import edu.upmc.dar.server.dispatch.RequestHandler;
import edu.upmc.dar.server.dispatch.RequestMatch;
import edu.upmc.dar.server.dispatch.ServletContainer;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.model.Model;
import edu.upmc.dar.server.servlet.HttpServlet;
import edu.upmc.dar.server.servlet.page.InternalErrorPageServlet;
import edu.upmc.dar.server.servlet.page.NotFoundPageServlet;
import edu.upmc.dar.server.util.JsonUtil;
import edu.upmc.dar.server.util.Log;
import edu.upmc.dar.server.util.TemplateParserUtil;
import edu.upmc.dar.server.view.Template;
import edu.upmc.dar.server.view.ViewResolver;

import javax.tools.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {
    public static final int DEFAULT_PORT = 80;
    public static final int THREADS_POOL_SIZE = 5;
    public static final int MAX_CONNECTIONS = 10;
    public static final String DEPLOY_FOLDER_PATH = "deploy";
    public static final String APPS_FOLDER_PATH = "apps";
    public static boolean debugMode;

    public static NotFoundPageServlet notFoundPageServlet = new NotFoundPageServlet();
    public static InternalErrorPageServlet internalErrorPageServlet = new InternalErrorPageServlet();

    /**
     * Server's entry method
     * Initializes the executors pool and starts the HTTP server
     * @param args command line args array
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        int poolSize = THREADS_POOL_SIZE;
        int maxConnections = MAX_CONNECTIONS;
        for(int i = 0; i < args.length; ++i){
            switch(args[i]){
                case "-port":{
                    if(i < args.length - 1){
                        port = Integer.parseInt(args[++i]);
                    }
                    break;
                }
                case "-poolSize":{
                    if(i < args.length - 1){
                        poolSize = Integer.parseInt(args[++i]);
                    }
                    break;
                }
                case "-maxCon":{
                    if(i < args.length - 1){
                        maxConnections = Integer.parseInt(args[++i]);
                    }
                    break;
                }
                case "-debug":{
                    debugMode = true;
                    break;
                }
            }
        }

        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        try{
            initServlets();
            initTemplates();
            loadApplications();
            initDeployChecker();

            ServerSocket Server = new ServerSocket(port, maxConnections, InetAddress.getByName("127.0.0.1"));
            Log.skipLine();
            Log.info("HTTP server started on port " + port + ((debugMode) ? " in debug mode" : ""));

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
        Log.info("Initializing core servlets..");

        ServletContainer.instance().registerServlet(new RequestMatch("/echo/text"), new HttpServlet() {
            @Override
            public String serve(HttpRequest request, HttpResponse response, Model model) {
                response.setContentType(ContentType.TEXT_PLAIN);
                return request.toString();
            }
        });

        ServletContainer.instance().registerServlet(new RequestMatch("/echo/html"), new HttpServlet() {
            @Override
            public String serve(HttpRequest request, HttpResponse response, Model model) {
                response.setContentType(ContentType.TEXT_HTML);
                return "templates/echo";
            }
        });

        ServletContainer.instance().registerServlet(new RequestMatch("/echo/json"), new HttpServlet() {
            @Override
            public String serve(HttpRequest request, HttpResponse response, Model model) throws Exception {
                response.setContentType(ContentType.APP_JSON);
                return JsonUtil.serializeRequest(request);
            }
        });

        Log.info("Core servlets initialization completed successfully");
    }

    /**
     * Manually register the initial templates
     */
    private static void initTemplates() throws Exception {
        Log.info("Initializing core templates..");

        String webappFolderPath = "../webapp";
        File webappFolder = new File("webapp");

        ClassLoader classLoader = new URLClassLoader(new URL[]{webappFolder.toURI().toURL()});
        processApplication(classLoader, webappFolder, "", webappFolderPath);

        Log.info("Core templates initialization completed successfully");
    }

    /**
     * Loads all the previously installed applications from the apps folder
     */
    private static void loadApplications() throws Exception{
        Log.skipLine();
        Log.info("Loading applications..");

        File appsFolder = new File(APPS_FOLDER_PATH);
        if(! appsFolder.exists()){
            appsFolder.mkdir();
        }

        for(File appFolder : appsFolder.listFiles()){
            if(appFolder.isDirectory()){
                Log.skipLine();
                Log.info("Installing the application " + appFolder.getName());

                ClassLoader classLoader = new URLClassLoader(new URL[]{appFolder.toURI().toURL()});
                processApplication(classLoader, appFolder, "", appFolder.getName());

                Log.info("The application " + appFolder.getName() + " was successfully installed\n");
            }
        }

        Log.info("All applications were loaded");
    }

    /**
     * Initializes the listener that checks when new .jar files are placed in the deploy directory
     * Loads new applications from all found jars
     */
    private static void initDeployChecker(){
        Log.skipLine();
        Log.info("Initializing deployment listener..");

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

        Log.info("Deployment listener initialized");
    }

    /**
     * Checks if there are new jars in the deployment folder and runs the installation
     * @throws Exception
     */
    private static void checkForDeployments() throws Exception {
        File deployFolder = new File(DEPLOY_FOLDER_PATH);
        if(deployFolder.exists() && deployFolder.isDirectory()){
            for(File archive : deployFolder.listFiles((dir, name) -> name.endsWith(".jar"))){
                Log.skipLine();
                Log.info("Deployment listener: loading the application " + archive.getName());

                processJarFile(archive);

                if(! archive.delete()){
                    Log.err("Deployment listener: couldn't remove the file " + archive.getName());
                }
            }
        }
    }

    /**
     * Extracts the jar file into the apps folder and runs the installation
     * @param file application archive
     * @throws Exception
     */
    private static void processJarFile(File file) throws Exception {
        String appName = file.getName().substring(0, file.getName().length() - ".jar".length());
        JarFile jarFile = new JarFile(file.getPath());
        Enumeration<JarEntry> e = jarFile.entries();

        File appsFolder = new File(APPS_FOLDER_PATH);
        if(! appsFolder.exists()){
            appsFolder.mkdir();
        }

        File appFolder = new File(appsFolder, appName);
        if(appFolder.exists()) {
            appFolder.delete();
        }
        appFolder.mkdir();

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            File entryFile = new File(appFolder, je.getName());

            if(je.isDirectory()){
                entryFile.mkdir();
                continue;
            }

            InputStream is = jarFile.getInputStream(je);
            FileOutputStream os = new FileOutputStream(entryFile);
            while(is.available() > 0){
                os.write(is.read());
            }
            os.close();
            is.close();
        }
        jarFile.close();

        Log.info("The jar file " + file.getName() + " was extracted, installing the application..\n");

        ClassLoader classLoader = new URLClassLoader(new URL[]{appFolder.toURI().toURL()});
        processApplication(classLoader, appFolder, "", appName);

        Log.info("The application " + appName + " was successfully installed\n");
    }

    /**
     * Performs applications installation
     * @param folder application folder
     * @throws Exception
     */
    private static void processApplication(ClassLoader cl, File folder, String currentPath, String appName) throws Exception {
        String packageName = currentPath.replace('/', '.');

        List<Object[]> templates = new LinkedList<>();

        for(File file : folder.listFiles()){
            if(file.isDirectory()){
                processApplication(cl, file, (("".equals(currentPath)) ? "" : currentPath + "/") + file.getName(), appName);
            } else {
                if(file.getName().endsWith(".class")){
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - ".class".length());
                    processNewClass(cl.loadClass(className));
                }
                if(file.getName().endsWith(".tpl")){
                    templates.add(new Object[]{file, currentPath});
                }
            }
        }

        for(Object[] template : templates){
            File templateFile = (File)template[0];
            String templatePath = (String)template[1];
            processNewTemplate(cl, templateFile, templatePath, appName);
        }
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

                    Log.debug("Registering servlet " + newClass.getName() + ", method: " + method + ", url: " + url + ", produces: " + contentType);
                    ServletContainer.instance().unregisterServlet(matchingCriteria);
                    ServletContainer.instance().registerServlet(matchingCriteria, servletInstance);
                }
            }
        }
    }

    /**
     * Registers the template in the view resolver
     * @param templateFile the template file
     * @param path the path to the template
     * @throws Exception
     */
    private static void processNewTemplate(ClassLoader classLoader, File templateFile, String path, String appName) throws Exception {
        String templateName = templateFile.getName().substring(0, templateFile.getName().length() - ".tpl".length());

        StringBuilder templateClassNameSb = new StringBuilder();
        for(String nameFragment : templateName.split("[-_.]")){
            templateClassNameSb.append(nameFragment.substring(0,1).toUpperCase() + nameFragment.substring(1));
        }
        templateClassNameSb.append("Template");
        String templateClassName =  templateClassNameSb.toString();
        String packageName = path.replace('/', '.');

        File generatedSrcFile = TemplateParserUtil.compileTemplate(templateFile, path, appName, packageName, templateClassName);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        List<String> optionList = new LinkedList<>();
        optionList.add("-classpath");
        optionList.add(System.getProperty("java.class.path") + ";apps\\" + appName);

        Iterable<? extends JavaFileObject> compilationUnit
                = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(generatedSrcFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnit);

        if(task.call()){
            Class<?> templateClass = classLoader.loadClass(packageName + "." + templateClassName);
            Template templateInstance = (Template)templateClass.newInstance();
            String templateFullName = path + "/" + templateName;

            ViewResolver.instance().unregisterTemplate(templateFullName);
            ViewResolver.instance().registerTemplate(templateFullName, templateInstance);

            Log.debug("Registering template " + templateFullName);

            File generatedBinFile = new File(generatedSrcFile.getAbsolutePath().replace(".java",".class"));
            generatedBinFile.delete();
            if(!debugMode){
                generatedSrcFile.delete();
            }
        } else {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.err.format("%s: %d - %s%n",
                        diagnostic.getSource().toUri(),
                        diagnostic.getLineNumber(),
                        diagnostic.getMessage(Locale.ENGLISH));
            }
        }
        fileManager.close();
    }
}
