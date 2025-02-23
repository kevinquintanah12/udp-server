import java.io.*;
import java.net.*;

public class clienteA {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        FileInputStream fileInputStream = null;
        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            File pdfFile = new File(args[0]);  // Archivo PDF a enviar
            fileInputStream = new FileInputStream(pdfFile);
            byte[] sendData = new byte[1024];  // Tamaño del buffer de envío
            int bytesRead;
            long totalBytes = pdfFile.length();
            long bytesSent = 0;

            // Enviar el archivo al servidor en bloques
            while ((bytesRead = fileInputStream.read(sendData)) != -1) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, serverAddress, 9876);
                socket.send(sendPacket);
                bytesSent += bytesRead;

                // Mostrar progreso
                int progress = (int) ((bytesSent * 100) / totalBytes);
                System.out.printf("Enviado: %d/%d bytes (Progreso: %d%%)\n", bytesSent, totalBytes, progress);
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
