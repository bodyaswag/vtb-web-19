package Hackathon.handlers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;


import com.sun.net.httpserver.*;


public class shopHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        sendFile(t, "Hackathon/site/html/shop1.html");
    }

    public void sendFile(HttpExchange t, String path) throws IOException {
        File file = new File(path);
        t.sendResponseHeaders(200, file.length());
        try (OutputStream os = t.getResponseBody()) {
            Files.copy(file.toPath(), os);
        }catch(Exception e){
            System.out.println(e);
        }
    }
   
}
