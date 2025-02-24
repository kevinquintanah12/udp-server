import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class ClienteB {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        FileOutputStream fileOutputStream = null;
        try {
            socket = new DatagramSocket(9877);
            Map<Integer, byte[]> receivedPackets = new TreeMap<>();
            int totalPackets = -1;

            System.out.println("Esperando el archivo...");

            while (true) {
                byte[] receiveData = new byte[1028];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // Leer el número de secuencia y el total de paquetes
                int seqNum = ((receiveData[0] & 0xFF) << 8) | (receiveData[1] & 0xFF);
                totalPackets = ((receiveData[2] & 0xFF) << 8) | (receiveData[3] & 0xFF);

                byte[] fileChunk = Arrays.copyOfRange(receiveData, 4, receivePacket.getLength());
                receivedPackets.put(seqNum, fileChunk);

                int progress = (receivedPackets.size() * 100) / totalPackets;
                System.out.printf("Recibido: %d/%d paquetes (%d%%)\n", receivedPackets.size(), totalPackets, progress);

                // Si hemos recibido todos los paquetes, escribirlos en el archivo
                if (receivedPackets.size() == totalPackets) {
                    System.out.println("Recepción completa. Guardando archivo...");
                    File pdfFile = new File("received_file.pdf");
                    fileOutputStream = new FileOutputStream(pdfFile);

                    for (int i = 0; i < totalPackets; i++) {
                        fileOutputStream.write(receivedPackets.get(i));
                    }
                    System.out.println("Archivo guardado como 'received_file.pdf'");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
