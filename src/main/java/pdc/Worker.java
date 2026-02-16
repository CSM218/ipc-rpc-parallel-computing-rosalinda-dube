package pdc;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * A Worker is a node in the cluster capable of high-concurrency computation.
 * 
 * CHALLENGE: Efficiency is key. The worker must minimize latency by
 * managing its own internal thread pool and memory buffers.
 */
public class Worker {

    /**
     * Connects to the Master and initiates the registration handshake.
     * The handshake must exchange 'Identity' and 'Capability' sets.
     */
    public void joinCluster(String masterHost, int port) {
        // TODO: Implement the cluster join protocol
        
    try {
        Socket socket = new Socket(masterHost, port);
        System.out.println("Connected to master");

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        // Create HELLO message
        Message msg = new Message();
msg.magic = "PDC";
msg.version = 1;
msg.type = "HELLO";
msg.sender = "worker-1";
msg.timestamp = System.currentTimeMillis();
msg.payload = "I am ready".getBytes();
msg.messageType = "HELLO";
msg.studentId = "N00011100X"; // replace with your student number

        // Send message
        byte[] data = msg.pack();

        // Send length first (framing)
        out.write(ByteBuffer.allocate(4).putInt(data.length).array());
        out.write(data);
        out.flush();

        System.out.println("HELLO sent to master");

        // Wait for reply
        byte[] lengthBytes = in.readNBytes(4);
        int length = ByteBuffer.wrap(lengthBytes).getInt();

        byte[] responseBytes = in.readNBytes(length);
        Message response = Message.unpack(responseBytes);

        System.out.println("Master says: " + response.type);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    

    /**
     * Executes a received task block.
     * 
     * Students must ensure:
     * 1. The operation is atomic from the perspective of the Master.
     * 2. Overlapping tasks do not cause race conditions.
     * 3. 'End-to-End' logs are precise for performance instrumentation.
     */
    public void execute() {
        // TODO: Implement internal task scheduling
        while (!taskQueue.isEmpty()) {
            String task = taskQueue.poll();
            if (task != null) {
                System.out.println("Processing: " + task);
                taskCounter.incrementAndGet();
            }
        }
    }

    private ConcurrentLinkedQueue<String> taskQueue =
            new ConcurrentLinkedQueue<>();
    private AtomicInteger taskCounter = new AtomicInteger(0);
}
