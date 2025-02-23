import java.io.*;
import java.net.*;

public class ClienteB {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        FileOutputStream fileOutputStream = null;
        try {
            socket = new DatagramSocket(9877);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket;
            File pdfFile = new File("received_file.pdf");
            fileOutputStream = new FileOutputStream(pdfFile);

            // Variables para el seguimiento del progreso
            long totalBytesReceived = 0;
            long totalBytesExpected = 0;
            long lastUpdate = 0;
            int totalPacketsReceived = 0;

            System.out.println("Esperando el archivo...");

            while (true) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                totalBytesReceived += receivePacket.getLength();

                // Si es el primer paquete, estimamos el tamaño total del archivo
                if (totalPacketsReceived == 0) {
                    // Estimamos el tamaño total del archivo en función del número total de paquetes
                    totalBytesExpected = receivePacket.getLength() * 10; // Suponiendo 100 paquetes
                }
                totalPacketsReceived++;

                // Calcular el porcentaje de recepción
                int progress = (int) ((totalBytesReceived * 10) / totalBytesExpected);

                // Mostrar el progreso
                if (System.currentTimeMillis() - lastUpdate > 500) { // Actualizamos cada 500ms
                    System.out.printf("Progreso de recepción: %d%% (%d/%d bytes)\n", progress, totalBytesReceived, totalBytesExpected);
                    lastUpdate = System.currentTimeMillis();
                }

                // Guardar el paquete recibido en el archivo PDF
                fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
                
                // Si ya hemos recibido todos los paquetes (esto es solo una simulación del tamaño total)
                if (totalBytesReceived >= totalBytesExpected) {
                    System.out.println("Transferencia completa.");
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
