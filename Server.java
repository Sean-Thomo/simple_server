import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{
    public static final int PORT = 5000;
    private final BufferedReader in;
    private final PrintStream out;
    private final String clientMachine;
    private static final List<Server> clients = new ArrayList<>();

    public Server(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // System.out.println("Waiting for client...");

        synchronized (clients) {
            clients.add(this);
        }
    }

    public void run() {
        try {
            String messageFromClient;
            while((messageFromClient = in.readLine()) != null) {
                System.out.println("Message \"" + messageFromClient + "\" from " + clientMachine);
                // out.println("Thanks for this message: " + messageFromClient);

                synchronized (clients) {
                    for (Server client : clients) {
                        if (client != this) {
                            client.sendMessage(messageFromClient);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }
    }

    public void sendMessage(String Message) {
        out.println(Message);
        out.flush();
    }

    private void closeQuietly() {
        try {
            in.close();
            out.close();
        } catch (IOException ex) {}
    }
}
