package etape2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServeurChat {
    public static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(PORT));
        System.out.println("Serveur de chat UDP en écoute sur le port " + PORT + "...");

        Set<SocketAddress> clients = new LinkedHashSet<>();
        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            SocketAddress expediteur = packet.getSocketAddress();
            String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
            if (clients.add(expediteur)) {
                System.out.println("Nouveau client : " + expediteur);
            }
            System.out.println(expediteur + " -> " + message);

            // Diffusion à tous les autres clients
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            for (SocketAddress client : clients) {
                if (!client.equals(expediteur)) {
                    socket.send(new DatagramPacket(data, data.length, client));
                }
            }

            // Départ d'un client
            if (message.endsWith("/quit")) {
                clients.remove(expediteur);
                System.out.println("Client parti : " + expediteur);
            }
        }
    }
}
