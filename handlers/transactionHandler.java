package Hackathon.handlers;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


import com.sun.net.httpserver.*;


public class transactionHandler implements HttpHandler {

    private Connection connection;
    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hackathon?serverTimezone=Europe/Moscow&useSSL=false&allowPublicKeyRetrieval=true", "root", "uliasha");
        } catch (SQLException e) {
            printError(e, t);
            return;
        }
        Map<String, String> queryParams = queryToMap(getRequestString(t));
        Map<String, String> cookies = queryToMap(t.getRequestHeaders().getFirst("Cookie"));
        if(cookies == null || cookies.get("user_token") == null){
            t.getResponseHeaders().add("Location", "http://localhost:8000/auth");
            t.sendResponseHeaders(302, 0);
            t.close();
            return;
        }else{
            if(queryParams == null || queryParams.get("getSite") == "true"){
                sendFile(t, "Hackathon/site/html/index.html");
                return;
            }else if(queryParams.get("getSite") == "false" && queryParams.get("getKey") == "true"){
                //Обмен ключами
            }else if(queryParams.get("getSite") == "false" && queryParams.get("getKey") == "false"){
                //Обмен зашифрованными данными
            }
        }
    }

    private void printError(Exception e, HttpExchange t) throws IOException{
        t.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(503, 0);
        String response = "<html><body>Простите. У нас технические шоколадки :( </body><html>";
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println(e);
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
    public String getRequestString(HttpExchange t){
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
            (t.getRequestBody(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }catch(IOException e){
            return null;
        }
        return textBuilder.toString();
    }
    public Map<String, String> queryToMap(String query){
        if(query == null){
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
