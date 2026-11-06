package etape1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom d'utilisateur : ");
        String nom = sc.nextLine().trim();

        DatagramSocket socket = new DatagramSocket();
        InetAddress serveur = InetAddress.getByName("localhost");
        System.out.println("Tapez vos messages (\"quit\" pour quitter).");

        while (true) {
            String texte = sc.nextLine();
            if (texte.equalsIgnoreCase("quit")) break;
            byte[] data = ("[" + nom + "] : " + texte).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(data, data.length, serveur, ServeurUDP.PORT));
        }
        socket.close();
    }
}
