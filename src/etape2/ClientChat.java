package etape2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientChat {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom d'utilisateur : ");
        String nom = sc.nextLine().trim();

        DatagramSocket socket = new DatagramSocket();
        InetAddress serveur = InetAddress.getByName("localhost");

        Thread reception = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try {
                while (!socket.isClosed()) {
                    DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                    socket.receive(p);
                    System.out.println(new String(p.getData(), 0, p.getLength(), StandardCharsets.UTF_8));
                }
            } catch (Exception e) {

            }
        });
        reception.setDaemon(true);
        reception.start();

        // Message d'arrivée : permet au serveur d'enregistrer ce client
        envoyer(socket, serveur, "[" + nom + "] : a rejoint le chat");
        System.out.println("Connecté. Tapez vos messages (\"quit\" pour quitter).");

        while (true) {
            String texte = sc.nextLine();
            if (texte.equalsIgnoreCase("quit")) {
                envoyer(socket, serveur, "[" + nom + "] : a quitté le chat /quit");
                break;
            }
            envoyer(socket, serveur, "[" + nom + "] : " + texte);
        }
        socket.close();
    }

    private static void envoyer(DatagramSocket socket, InetAddress serveur, String msg) throws Exception {
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(data, data.length, serveur, ServeurChat.PORT));
    }
}
