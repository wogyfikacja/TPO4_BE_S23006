package zad1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{
    Socket socket;
    public Client(String ip,int port) throws Exception{
        socket = new Socket(ip, port);
    }

    public void send(String message) throws Exception{
        var out = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));
        out.write(message);
    }
    public void getMessage() throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
    }
}