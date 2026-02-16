package pdc;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {

    // must return null for initial autograder stage
    public Object coordinate(String operation, int[][] matrix, int workers) {
        return null;
    }

    // must not block the test thread
    public void listen(int port) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println("Master listening on " + server.getLocalPort());

                while (true) {
                    Socket worker = server.accept();
                    handleWorker(worker);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleWorker(Socket socket) {
        try (
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);

            if (bytesRead > 0) {
                // Message request = Message.unpack(data); // Removed unused variable
                Message reply = new Message();
                out.write(reply.pack());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reconcileState() {
        // empty stub required for now
    }

    String port = System.getenv("PORT");
}
