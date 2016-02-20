package edu.upmc.dar.server.dispatch;

import edu.upmc.dar.server.Main;
import edu.upmc.dar.server.request.HttpRequest;
import edu.upmc.dar.server.response.HttpResponse;
import edu.upmc.dar.server.servlet.HttpServlet;
import edu.upmc.dar.server.util.HttpRequestParser;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

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
        System.out.println("[" + new Date() + "] Received request from "
                + socket.getInetAddress() + ":" + socket.getPort());

        try(PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true)) {
            HttpRequest request = HttpRequestParser.parseRequest(socket.getInputStream());
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
            //TODO send 404 Not Found response or just echo the request??
            servlet = Main.echoServlet;
            System.out.println("Warning: no mapping found, sending default echo response");
        }

        return servlet.processRequest(request);
    }
}
