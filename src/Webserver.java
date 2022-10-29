import queuecore.Processor;
import queuecore.ThreadQueue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Webserver {

    public static void main(String[] args) {

        // Port number for http request
        final int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        // The maximum queue length for incoming connection
        int queueLength = args.length > 2 ? Integer.parseInt(args[2]) : 50;
        // The max number of threads allowed
        int numOfThreads = (args.length > 1 ? Integer.parseInt(args[1]) : 10);

        // Queue of requests
        ThreadQueue<Socket> requests = new ThreadQueue<>();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Web Server is starting up, listening at port " + port + ".");
            System.out.println("You can access http://localhost:" + port + " now.");
            // start threads
            for (int i = 0; i < numOfThreads; i++) {
                Processor processorThread = new Processor(requests);
                Thread th = new Thread(processorThread);
                th.start();
            }
            while (true) {
                // Accept connection
                Socket socket = serverSocket.accept();
                // store request inside queue if the queue size less than max queue length
                if (requests.size() <= queueLength){
                    requests.add(socket);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Server has been shutdown!");
        }
    }
}


