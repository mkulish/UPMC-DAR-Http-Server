package edu.upmc.dar.server.dispatch;

import edu.upmc.dar.server.Main;
import edu.upmc.dar.server.http.request.HttpRequest;
import edu.upmc.dar.server.http.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;
import edu.upmc.dar.server.http.request.HttpRequestParser;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) { this.socket = socket; }

    /**
     * This method will be executed when the request is processed by one of the threads of the executors' pool
     * Parses the request raw data, writes it to the standard output, generates and sends an adequate response
     */
    public void run() {
        System.out.println();
        System.out.println("--------------------------");
        System.out.println();
        System.out.println("[" + new Date() + "] Received request from " + socket.getInetAddress() + ":" + socket.getPort());

        try(PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true)) {
            HttpRequest request = HttpRequestParser.parseRequest(socket);
            System.out.println(request);

            HttpResponse response = getResponse(request);

            socketWriter.print(response);
            socketWriter.close();

            System.out.println("Response sent, content type: " + response.getContentType());
        } catch (Throwable ex){
            ex.printStackTrace();
        }
    }

    /**
     * Generates an HTTP response given an HTTP request
     * @param request HTTP request to process
     * @return HTTP response object
     * @throws Exception
     */
    private HttpResponse getResponse(HttpRequest request) throws Exception {
        HttpServlet servlet = Main.servletContainer.processRequest(request);

        if(servlet == null){
            //Send 404 Not Found response
            System.out.println("Warning: no mapping found, sending 404 Not found response");
            return Main.notFoundPageServlet.processRequest(request);
        }

        try{
            return servlet.processRequest(request);
        } catch (Exception ex){
            //Send 500 Internal Server Error if there was an exception
            System.out.println("Exception in the servlet method, sending 500 Internal Error response");
            ex.printStackTrace();
            return Main.internalErrorPageServlet.processRequest(request);
        }
    }
}
