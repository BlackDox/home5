package com.jcourse.melnikov.httpindex;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;

//by Семён
public class GenerateIndex {
    //private String indexName;
    //private File index;
    //private File directory;
    private String directory;
    private Socket socket;
    private String htmlquery;

    //    GenerateIndex(String fileName, String dirName) throws IOException {
//        this.index = new File(dirName+"\\" + fileName);
//        this.indexName = fileName;
//        this.directory = new File(dirName);
//        if(!index.exists()){
//            index.createNewFile();
//        }
//    }
    public GenerateIndex(String directory){
        this.directory = directory;
    }

    public String getDirectory(){
        return this.directory;
    }

    public String genTitle(){
        StringBuilder path = new StringBuilder();
        path.append("<title>Index of "+directory+"</title>");
        return path.toString();
    }

    public String genHead(){
        StringBuilder header = new StringBuilder();
        header.append("<head>"+this.genTitle()+"</head>");
        return header.toString();
    }

    public String answerGet() throws IOException {
        StringBuilder str = new StringBuilder();
        str.append("<html>"+this.genHead()+this.genBody()+"</html>");
        return str.toString();
    }

    public String answerHead() throws IOException {
        StringBuilder str = new StringBuilder();
        str.append("<html>"+this.genHead()+"<body></body></html>");
        return str.toString();
    }

    public String genTree() throws IOException {
        File file = new File(directory);
        File[] tree = file.listFiles();
        StringBuilder str = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        str.append("<table>");
        str.append("<thead><tr><th>Name</th><th>Size</th><th>Date modified</th></tr></thead>");
        for(File f: tree){
            StringBuilder str2 = new StringBuilder();
            str2.append(f.getPath());
            if(str2.charAt(0)=='\\') {
                str2.delete(0,1);
            }
            str.append("<tr><td data-value =\""+str2.toString()+"\"><a href=\""+ str2.toString()+ "\">"+str2.delete(0, f.getParentFile().getPath().length()-1).toString()+"</a></td><td data-value =\""+f.length() +"\">"+f.length()+"B</td><td data-value =\""+sdf.format(f.lastModified())+"\">"+sdf.format(f.lastModified())+"</td></tr>");
        }
        str.append("</table>");
        return str.toString();
    }

    public String genBody() throws IOException {
        StringBuilder str = new StringBuilder();
        str.append("<h1 id=\"header\">"+directory+"</h1>");
        str.append("<div><a href=\""+".."+"\"><span>[parrent dir]</span></a></div>");
        str.append(this.genTree());
        return str.toString();
    }
}
