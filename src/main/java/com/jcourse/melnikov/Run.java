package com.jcourse.melnikov;

import com.jcourse.melnikov.httpserver.HttpServer2;

public class Run {
    public static void main(String[] args) {
        HttpServer2 server2 = new HttpServer2();

        //       http://localhost:8080
        //TODO Сервер в качестве аргумента принимает адрес на серве
        //TODO юзер вводит локалхост8080 и видит эту дирректорию

        try {
            server2.runHTTPServer2();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
