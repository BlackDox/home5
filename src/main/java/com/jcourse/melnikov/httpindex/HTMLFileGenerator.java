package com.jcourse.melnikov.httpindex;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

public class HTMLFileGenerator {
    String[] args;

    public HTMLFileGenerator(String[] line) {
        args = line;
    }

    public void generate(){

    String output = "";
        if (args.length < 1) {
        System.out.println("Не указана директория");
        return;
    }

    File folder = new File(args[1]);
//    HTMLFileGenerator htmlFileGenerator = new HTMLFileGenerator();

        try {
        output = this.generateHTML(folder);
        System.out.println(output);
    } catch (IOException e) {
        e.printStackTrace();
    }

        try (Writer out = new OutputStreamWriter(new FileOutputStream("index.html"), "utf8")) {

        if (output.length() > 0) {
            out.write(output);
        }


    } catch (IOException e) {
        System.out.println("Не могу сохранить файл");
    }
}

    private String generateHTML(File folder) throws IOException {
        StringBuilder htmlPage = new StringBuilder();
//        File requestFolder = new File(path);
        htmlPage.append("<!DOCTYPE html>\n");
        htmlPage.append("<head>\n");
        htmlPage.append("<meta charset=\"utf-8\">\n");
        htmlPage.append("</head>\n\n");
        htmlPage.append("<body>");
        htmlPage.append("<h1>Содержание ").append(folder).append("</h1>\n");
        htmlPage.append("<hr>\n");
        htmlPage.append("<table cellpadding=\"1\"> \n");
        htmlPage.append("<tr><td><h3>Имя</h3></td><td><h3>Размер</h3></td><td><h3>Последнее изменение</h3></td></tr>\n");
        htmlPage.append("<tr><td>" + "<a href=" + '"').append("..").append('"').append(">...</a></td><td></td><td></td></tr>\n");

        File[] files = folder.listFiles();

        if (files != null) {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    if (!f1.isDirectory() && !f2.isDirectory()) {
                        return f1.compareTo(f2);
                    }

                    if (f1.isDirectory() && f2.isDirectory()) {
                        return f1.compareTo(f2);
                    }

                    if (f1.isDirectory() && !f2.isDirectory()) {
                        return -1;
                    } else return 1;
                }
            });
        }

        if (files != null) {
            for (File file : files) {

                htmlPage.append("<tr><td><a href=" + '"').append(file.getName()).append('"').append(">").append(file.getName()).append("</a></td>");
                htmlPage.append("<td>");
                if (file.isFile()) {
                    htmlPage.append(file.length()).append(" Byte");
                }
                htmlPage.append("</td>");
                htmlPage.append("<td>").append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(file.lastModified())).append("</td></tr>\n");
            }
        }
        htmlPage.append("</table>\n");
        htmlPage.append("</body>");

        return htmlPage.toString();
    }
}
