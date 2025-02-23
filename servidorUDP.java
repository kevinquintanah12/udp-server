import java.net.*;

public class servidorUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876);  // Puerto del servidor
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket;
            InetAddress clientAddressA = InetAddress.getByName("127.0.0.1");
            InetAddress clientAddressB = InetAddress.getByName("127.0.0.1");

            while (true) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                
                // Enviar datos a Cliente B
                DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), clientAddressB, 9877);
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
