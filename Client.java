import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {
        try (
            Socket socket = new Socket("localhost", 5000);
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            out.println("Connected to server. Type your message and press ENTER to send.");
            while (true) {
                String messageToSend = reader.readLine();
                out.println(messageToSend);
                out.flush();
                String messageFromServer = in.readLine();
                System.out.println("Server says: " + messageFromServer);
    
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
