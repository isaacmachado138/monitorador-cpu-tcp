import java.net.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpServidor {
    public static void main(String args[]) {
        try {
            int serverPort = 7896;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("AGUARDANDO REQUISIÇÃO NA PORTA " + serverPort);

            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("CONEXÃO ACEITA: " + clientSocket.getInetAddress());
                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen: " + e.getMessage());
        }
    }
}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public Connection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    public void run() {
        try {
            String data = in.readUTF();
            System.out.println("MENSAGEM RECEBIDA: " + data);

            if (data.equalsIgnoreCase("CPU")) {
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
                double totalCpuUsage = 0;

                for (int i = 1; i <= 10; i++) {
                    double cpuLoad = osBean.getSystemCpuLoad() * 100;
                    totalCpuUsage += cpuLoad;
                    String timestamp = formatter.format(new Date());
                    out.writeUTF(i + ") " + timestamp + " | " + String.format("%.3f", cpuLoad) + "%");
                    Thread.sleep(1000);
                }

                double averageCpuUsage = totalCpuUsage / 10;
                out.writeUTF("VALOR MEDIO DO USO DA CPU DO SERVIDOR: " + String.format("%.3f", averageCpuUsage) + "%");
                out.writeUTF("TEMPO DE PROCESSAMENTO: 10 segundos");
            } else if (data.equalsIgnoreCase("-help")) {
                out.writeUTF("AJUDA:");
                out.writeUTF("<><><><PARAMETROS><><><>");
                out.writeUTF("CPU - > apresenta os dados de utilizacao da CPU DO SERVIDOR");
                out.writeUTF("<><><><CONEXAO FECHOU><><><>");
            } else {
                out.writeUTF("ERRO1:");
                out.writeUTF("***PARAMETRO INCORRETO***");
                out.writeUTF("UTILIZE");
                out.writeUTF("<CPU> ou <cpu> para ler os dados do servidor");
                out.writeUTF("<-help> para ajuda.");
                out.writeUTF("Ex.: java TcpCliente -help localhost");
            }
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("<><><><CONEXAO FECHOU><><><>");
            } catch (IOException e) {
                System.out.println("Close falhou");
            }
        }
    }
}