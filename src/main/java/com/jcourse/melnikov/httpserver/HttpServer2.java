package com.jcourse.melnikov.httpserver;

import com.jcourse.melnikov.httpindex.HTMLFileGenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by BlackDox on 21.12.2017.
 */
public class HttpServer2 {

    public void runHTTPServer2() throws Throwable {
        ServerSocket ss = new ServerSocket(8080);

        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }

    private static class SocketProcessor implements Runnable {
        private HTMLFileGenerator htmlFileGenerator;
        private Socket s;
        private InputStream is;
        private OutputStream os;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        public void run() {
            try {
                readInputHeaders();
//                writeResponse("<html><body><h1>111</h1></body></html>\r\n\r\n");

            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }

        private void writeResponse(String s) throws Throwable {
//            String response = "HTTP/1.1 404 Not Found\r\n";
//            String response = "HTTP/1.1 200 OK\r\n";
            // + "Server: YarServer/2009-09-09\r\n" + "Content-Type: text/html\r\n" + "Content-Length: " + s.length() + "\r\n" + "Connection: close\r\n\r\n";
            String result = s;
            os.write(result.getBytes());
            os.flush();
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s;
            StringBuilder sb = new StringBuilder();
            while (true) {
                s = br.readLine();
                System.out.println(s);
                sb.append(s);
                if (s == null || s.trim().length() == 0) {
                    break;
                }
            }
            String[] line = sb.toString().split(" ");
            System.out.println("line[0] - " + line[0]);
            System.out.println("line[1] - " + line[1]);

            if (line[1].equals("/") && line[0].equals("GET")) {
                writeResponse("HTTP/1.1 200 OK\r\n");
                writeResponse("Content-Type: text/html; charset=UTF-8\r\n\r\n");
                htmlFileGenerator = new HTMLFileGenerator(line, false);
                writeResponse(htmlFileGenerator.generate());
            } else if (line[0].equals("GET")) {
                writeResponse("HTTP/1.1 200 OK\r\n");
                writeResponse("Content-Type: text/html; charset=UTF-8\r\n\r\n");
                htmlFileGenerator = new HTMLFileGenerator(line, true);
                writeResponse(htmlFileGenerator.generate());
            } else if (line[0].equals("HEAD")){
                System.out.println("ПОКА НЕ РЕАЛИЗОВАНО! xD");
            } else {
                writeResponse("HTTP/1.1 501 Not Implemented\r\n");
                writeResponse("Content-Type: text/html; charset=UTF-8\r\n\r\n");
                writeResponse("<html><body><h1>Неизвестная команда</h1></body></html>\r\n");
            }
        }
    }
}
