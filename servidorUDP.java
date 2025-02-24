import java.net.*;

public class servidorUDP {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876);
            byte[] buffer = new byte[1028];
            DatagramPacket receivePacket;
            InetAddress clientAddressB = InetAddress.getByName("127.0.0.1");

            while (true) {
                receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                DatagramPacket sendPacket = new DatagramPacket(
                    receivePacket.getData(), receivePacket.getLength(),
                    clientAddressB, 9877
                );
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
