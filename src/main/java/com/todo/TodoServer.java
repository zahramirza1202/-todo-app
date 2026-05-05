package com.todo;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class TodoServer {

    static List<String> tasks = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/tasks", (HttpExchange exchange) -> {
            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                String response = String.join(",", tasks);
                sendResponse(exchange, response);

            } else if (method.equals("POST")) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                tasks.add(body);
                sendResponse(exchange, "Task added");

            } else if (method.equals("DELETE")) {
                tasks.clear();
                sendResponse(exchange, "All tasks deleted");
            }
        });

        server.start();
        System.out.println("Server running on port 8080");
    }

    static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}