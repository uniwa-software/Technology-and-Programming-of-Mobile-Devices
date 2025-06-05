import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 41007;
        String hostname;
        String osName = System.getProperty("os.name");
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "UnknownHost";
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try {
                String localIP = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Server IP: " + localIP);
                System.out.println("[" + hostname + "] Server listening on port " + port);
            } catch (UnknownHostException e) {
                System.out.println("Could not get local IP: " + e.getMessage());
            }
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
                ) {
                    String command = in.readLine();
                    System.out.println("[" + hostname + "] Received command: " + command);

                    switch (command) {
                        case "Connected":
                        case "Echo":
                            out.println(hostname + " - " + osName);
                            break;
                        case "Restart":
                            out.println(hostname + " - Rebooting...");
                            break;
                        case "Shutdown":
                            out.println(hostname + " - Shutting down...");
                            break;
                        case "Restore":
                            out.println(hostname + " - Restoring");
                            try{
                                Thread.sleep(5000);
                            }catch(Exception e){
                            	System.out.println("Can't Sleep");
                            }
                            out.println(hostname + " - Restored");
                            break;
                        default:
                            out.println("Unknown command");
                    }
                    
                } catch (IOException e) {
                    System.out.println("[" + hostname + "] Connection error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("[" + hostname + "] Server error: " + e.getMessage());
        }
    }
}