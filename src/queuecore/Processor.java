package queuecore;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Processor implements Runnable {
    private final ServerFunctions serverFunctions = new ServerFunctions();
    private ThreadQueue<Socket> requests;
    public Processor(ThreadQueue<Socket> requests) {
        this.requests = requests;
    }

    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            try
            {
                socket = requests.pop();
            } catch (InterruptedException e) {e.printStackTrace();}
            if (socket == null) {
                return;
            }
                try
                {
                    System.out.println("Got request:");
                    // reading request from the socket
                    process(socket);
                }
                finally
                {
                    try
                    {
                        socket.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void process(Socket socket){
            BufferedReader input = null;
            HttpRequest request = null;
            PrintWriter output = null;
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                request = HttpRequest.parse(input);
                }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String resp = "";
                // writing to the socket
                System.out.println("Got request:");
                System.out.flush();
                String req = request.toString();
                String filename = "";
                if (req.contains("create/")) {
                    filename = System.getProperty("user.dir") + "/files/" + serverFunctions.createFileName(req) + ".txt";
                    System.out.println("FILENAME TO CREATE IS " + filename);

                    try {
                        File f = new File(filename);
                        if (f.createNewFile()) {
                            resp += "Successfully created file: " + f.getName();
                        } else {
                            resp += "File " + f.getName() + " already exists:";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (req.contains("delete/")) {
                    filename = System.getProperty("user.dir") + "/files/" + serverFunctions.deleteFileName(req) + ".txt";
                    System.out.println("FILENAME TO DELETE IS " + filename);
                    try {
                        File f = new File(filename);
                        if (f.delete()) {
                            resp += f.getName() + " file successfully deleted";   //getting and printing the file name
                        } else {
                            resp += "Cannot find file:";

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (req.contains("square/")) {
                    double[] res = serverFunctions.square(req);
                    resp += "Square of of " + res[0] + " is: " + res[1];
                } else if (req.contains("add/")) {
                    double[] res = serverFunctions.add(req);
                    resp += res[0] + " + " + res[1] + " = " + res[2];
                }


                // To send response back to the client.
                output = new PrintWriter(socket.getOutputStream());
                // We are returning a simple web page now.
                // http version status code
                output.println("HTTP/1.1 200 OK");
                // content type and encoding
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();
                // html code
                output.println("<html>");
                output.println("<head><title>Simple Server</title></head>");
                output.println("<body><p>Hello, world!</p>");
                output.println(resp);
                output.println("<body><p>Simple webserver</p></body>");
                output.println("</html>");
                output.flush();
                System.out.println("Server connection finished");
            } catch (Exception e) {
                e.printStackTrace();
                // Closing inputStream, outputStream and Socket
            } finally {
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

