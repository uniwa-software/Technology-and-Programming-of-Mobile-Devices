import java.io.*;
import java.net.*;

public class LabServer {
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
            System.out.println("[" + hostname + "] Server listening on port " + port);

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

                            final PrintWriter threadOut = out;
                            final String threadHostname = hostname;

                            new Thread(() -> {
                                try {
                                    Thread.sleep(5000); // Προσομοίωση καθυστέρησης
                                    threadOut.println(threadHostname + " - Restored");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
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
