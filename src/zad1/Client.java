package zad1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client{
    private Socket socket;
    private List<String> topics;
    public Client(String ip,int port) throws Exception{
        socket = new Socket(ip, port);
        this.getTopics();
    }

    public void send(String message) throws Exception{
        var out = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));
        out.write(message);
    }
    public String getMessage() throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String inputLine;
        inputLine = in.readLine();      
        return inputLine;
    }
    public void getTopics() throws Exception{
        this.send("GET");
        String setup = getMessage();
        var split = setup.split(",");
        topics = List.of(split);
    }
    public void addTopic(String topic) throws Exception{
        this.send("ADMINADD:"+topic);
        String setup = getMessage();
        System.out.println(setup);
    }
    public void deleteTopic(String topic) throws Exception{
        this.send("ADMINDELETE:"+topic);
        String setup = getMessage();
        System.out.println(setup);
    }
    public void writeToTopic(String topic,String message) throws Exception{
        this.send("ADMINWRITE:"+topic+":"+message);
        String setup = getMessage();
        System.out.println(setup);
    }
    public void addTopicToClient(String topic) throws Exception{
        this.send("CLIENTADD:"+topic);
        String setup = getMessage();
        System.out.println(setup);
    }
    public void deleteTopicFromClient(String topic) throws Exception{
        this.send("CLIENTDEL:"+topic);
        String setup = getMessage();
        System.out.println(setup);
    }
    public void close() throws Exception{
        socket.close();
    }
}