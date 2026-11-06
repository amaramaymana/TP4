package etape1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ServeurUDP {
    public static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);          // socket non liée
        socket.bind(new InetSocketAddress(PORT));                  // liaison via InetSocketAddress
        System.out.println("Serveur UDP en écoute sur le port " + PORT + "...");

        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("[" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "] " + message);
        }
    }
}
