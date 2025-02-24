import java.io.*;
import java.net.*;

public class clienteA {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        FileInputStream fileInputStream = null;
        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            File pdfFile = new File(args[0]);
            fileInputStream = new FileInputStream(pdfFile);
            
            byte[] fileData = new byte[(int) pdfFile.length()];
            fileInputStream.read(fileData);
            
            int packetSize = 1024;
            int totalPackets = (int) Math.ceil((double) fileData.length / packetSize);
            
            for (int i = 0; i < totalPackets; i++) {
                int start = i * packetSize;
                int end = Math.min(start + packetSize, fileData.length);
                byte[] packetData = new byte[end - start + 4];

                // Encabezado (4 bytes) para el número de paquete
                packetData[0] = (byte) (i >> 8);
                packetData[1] = (byte) i;
                packetData[2] = (byte) (totalPackets >> 8);
                packetData[3] = (byte) totalPackets;

                // Copiar datos del archivo
                System.arraycopy(fileData, start, packetData, 4, end - start);

                DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, serverAddress, 9876);
                socket.send(sendPacket);

                int progress = (i + 1) * 100 / totalPackets;
                System.out.printf("Enviando %d/%d paquetes (%d%%)\n", i + 1, totalPackets, progress);
            }

            System.out.println("Archivo enviado.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
