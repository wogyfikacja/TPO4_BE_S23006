package zad1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.Set;


public class Server {
    private static final int DEFAULT_TIME_PORT = 8900;
    private static List<String> topics;

    // Constructor with no arguments creates a time server on default port.
    public Server() throws Exception {
        acceptConnections(DEFAULT_TIME_PORT);
    }

    // Constructor with port argument creates a time server on specified port.
    public Server(int port) throws Exception {
        acceptConnections(port);
    }

    // Accept connections for current time. Lazy Exception thrown.
    private static void acceptConnections(int port) throws Exception {
        // Selector for incoming time requests
        Selector acceptSelector = SelectorProvider.provider().openSelector();

        // Create a new server socket and set to non blocking mode
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // Bind the server socket to the local host and port

        InetAddress lh = InetAddress.getLocalHost();
        InetSocketAddress isa = new InetSocketAddress(lh, port);
        ssc.socket().bind(isa);

        // Register accepts on the server socket with the selector. This
        // step tells the selector that the socket wants to be put on the
        // ready list when accept operations occur, so allowing multiplexed
        // non-blocking I/O to take place.
        SelectionKey acceptKey = ssc.register(acceptSelector,
            SelectionKey.OP_ACCEPT);

        int keysAdded = 0;

        // Here's where everything happens. The select method will
        // return when any operations registered above have occurred, the
        // thread has been interrupted, etc.
        while ((keysAdded = acceptSelector.select()) > 0) {
            // Someone is ready for I/O, get the ready keys
            Set<SelectionKey> readyKeys = acceptSelector.selectedKeys();
            var i = readyKeys.iterator();

            // Walk through the ready keys collection and process date requests.
            while (i.hasNext()) {
                SelectionKey sk = (SelectionKey) i.next();
                i.remove();
                // The key indexes into the selector so you
                // can retrieve the socket that's ready for I/O
                ServerSocketChannel nextReady = (ServerSocketChannel) sk
                    .channel();
                Socket s = nextReady.accept().socket();

                var in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                var out = new BufferedWriter(new PrintWriter(s.getOutputStream(), true));

                String mesGot = in.readLine();
                var chopped = mesGot.split(":");
                String att = (String)sk.attachment();

                switch (chopped[0]) {
                    case "GET":
                        String toSend = "";
                        for(String topic : topics) {
                            toSend = toSend +","+ topic;
                        }
                        out.write(toSend);
                        break;
                    case "ADMINADD":
                        topics.add(chopped[1]);
                        out.write("Done adding " + chopped[1]);
                        break;
                    case "ADMINDELETE":
                        topics.remove(chopped[1]);
                        out.write("Done deleting " + chopped[1]);
                        break;
                    case "ADMINWRITE":
                        i.forEachRemaining((key) -> {
                            if(((String)key.attachment()).contains(chopped[1])){
                                ServerSocketChannel nex = (ServerSocketChannel) key
                                .channel();
                                try {
                                    Socket sock = nex.accept().socket();
                                    var toMess = new BufferedWriter(new PrintWriter(sock.getOutputStream(), true));
                                    toMess.write(chopped[2]);
                                    toMess.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case "CLIENTADD":
                        if(topics.contains(chopped[1])){
                            sk.attach(att + "," + chopped[1]);
                            out.write("Added topic");
                        }
                        else{
                            out.write("No such topic");
                        }
                        break;
                    case "CLIENTDEL":
                        att.replace(","+chopped[1]+",", ",");
                        out.write("Replacement done");
                        break;
                    default:
                        out.write("Bad flag");
                        break;
                }

                in.close();
                out.close();
            }
        }
    }

    // Entry point.
    public static void main(String[] args) {
        // Parse command line arguments and
        try {
            Server nbt = new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}