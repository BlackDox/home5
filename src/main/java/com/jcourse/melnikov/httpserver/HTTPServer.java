package com.jcourse.melnikov.httpserver;
import com.jcourse.melnikov.httpindex.GenerateIndex;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;

//by Семён
public class HTTPServer {
    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080);
        boolean marker = false;
        while (true) {
            Socket s = ss.accept();
            //System.err.println("Client accepted");
            new Thread(new SocketProcessor(s, marker)).start();
            marker = true;
        }
    }

    private static class SocketProcessor implements Runnable {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;
        private boolean isRoot;

        private SocketProcessor(Socket socket, boolean isRoot) throws Throwable {
            this.socket = socket;
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
            this.isRoot = isRoot;
        }

        public void run() {

            try {
                String[] param = readInputHeaders();
                String args =null;
                GenerateIndex index = null;
//                if(isRoot){
                args = URLDecoder.decode(param[1], "UTF-8");
                System.out.println(args);
                index = new GenerateIndex(args);
                if(param[0].equals("GET")){
                    writeResponse(index.answerGet());
                }
                if (param[0].equals("HEAD")){
                    writeResponse("");
                }
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    socket.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }

        private void writeResponse(String s) throws Throwable {//Äîëæåí áûòü îáðàáîò÷èê 404.500.501

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Date: Jan 5\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            outputStream.write(result.getBytes());
            outputStream.flush();
        }

        private String[] readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String request = br.readLine();
            String[] args = request.split(" ");
            return args;
        }
    }
}
