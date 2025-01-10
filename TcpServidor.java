import java.net.*; // Importa classes para trabalhar com sockets
import java.io.*; // Importa classes para entrada e saída de dados
import java.lang.management.ManagementFactory; // Importa classes para gerenciamento do sistema
import com.sun.management.OperatingSystemMXBean; // Importa classes específicas para obter informações do sistema operacional
import java.text.SimpleDateFormat; // Importa classes para formatação de datas
import java.util.Date; // Importa classes para trabalhar com datas

public class TcpServidor {
    public static void main(String args[]) {
        try {
            int serverPort = 7896; // Define a porta do servidor
            ServerSocket listenSocket = new ServerSocket(serverPort); // Cria um socket de servidor que escuta na porta definida
            System.out.println("AGUARDANDO REQUISIÇÃO NA PORTA " + serverPort); // Exibe mensagem indicando que o servidor está aguardando conexões

            while (true) {
                Socket clientSocket = listenSocket.accept(); // Aceita uma conexão do cliente
                System.out.println("CONEXÃO ACEITA: " + clientSocket.getInetAddress()); // Exibe o endereço IP do cliente que se conectou
                Connection c = new Connection(clientSocket); // Cria uma nova thread para lidar com a conexão do cliente
            }
        } catch (IOException e) {
            System.out.println("Listen: " + e.getMessage()); // Exibe mensagem de erro caso ocorra uma exceção de E/S
        }
    }
}

class Connection extends Thread {
    DataInputStream in; // Declaração do fluxo de entrada de dados
    DataOutputStream out; // Declaração do fluxo de saída de dados
    Socket clientSocket; // Declaração do socket do cliente

    public Connection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket; // Inicializa o socket do cliente
            in = new DataInputStream(clientSocket.getInputStream()); // Inicializa o fluxo de entrada de dados
            out = new DataOutputStream(clientSocket.getOutputStream()); // Inicializa o fluxo de saída de dados
            this.start(); // Inicia a thread
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage()); // Exibe mensagem de erro caso ocorra uma exceção de E/S
        }
    }

    public void run() {
        try {
            String data = in.readUTF(); // Lê a mensagem enviada pelo cliente
            System.out.println("MENSAGEM RECEBIDA: " + data); // Exibe a mensagem recebida

            if (data.equalsIgnoreCase("CPU")) { // Verifica se a mensagem é "CPU"
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class); // Obtém o bean de gerenciamento do sistema operacional
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss"); // Cria um formatador de data e hora
                double totalCpuUsage = 0; // Inicializa a variável para armazenar o uso total da CPU

                for (int i = 1; i <= 10; i++) { // Loop para coletar dados de uso da CPU 10 vezes
                    double cpuLoad = osBean.getSystemCpuLoad() * 100; // Obtém o uso da CPU e converte para porcentagem
                    totalCpuUsage += cpuLoad; // Acumula o uso da CPU
                    String timestamp = formatter.format(new Date()); // Obtém a data e hora atual formatada
                    out.writeUTF(i + ") " + timestamp + " | " + String.format("%.3f", cpuLoad) + "%"); // Envia os dados de uso da CPU para o cliente
                    Thread.sleep(1000); // Aguarda 1 segundo antes de coletar o próximo dado
                }

                double averageCpuUsage = totalCpuUsage / 10; // Calcula o uso médio da CPU
                out.writeUTF("VALOR MEDIO DO USO DA CPU DO SERVIDOR: " + String.format("%.3f", averageCpuUsage) + "%"); // Envia o valor médio de uso da CPU para o cliente
                out.writeUTF("TEMPO DE PROCESSAMENTO: 10 segundos"); // Envia o tempo de processamento para o cliente
            } else if (data.equalsIgnoreCase("-help")) { // Verifica se a mensagem é "-help"
                out.writeUTF("AJUDA:"); // Envia mensagem de ajuda para o cliente
                out.writeUTF("<><><><PARAMETROS><><><>"); // Envia mensagem de ajuda para o cliente
                out.writeUTF("CPU - > apresenta os dados de utilizacao da CPU DO SERVIDOR"); // Envia mensagem de ajuda para o cliente
                out.writeUTF("<><><><CONEXAO FECHOU><><><>"); // Envia mensagem de ajuda para o cliente
            } else { // Caso a mensagem não seja "CPU" ou "-help"
                out.writeUTF("ERRO1:"); // Envia mensagem de erro para o cliente
                out.writeUTF("***PARAMETRO INCORRETO***"); // Envia mensagem de erro para o cliente
                out.writeUTF("UTILIZE"); // Envia mensagem de erro para o cliente
                out.writeUTF("<CPU> ou <cpu> para ler os dados do servidor"); // Envia mensagem de erro para o cliente
                out.writeUTF("<-help> para ajuda."); // Envia mensagem de erro para o cliente
                out.writeUTF("Ex.: java TcpCliente -help localhost"); // Envia mensagem de erro para o cliente
            }
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage()); // Exibe mensagem de erro caso ocorra uma exceção de fim de arquivo
        } catch (IOException | InterruptedException e) {
            System.out.println("IO: " + e.getMessage()); // Exibe mensagem de erro caso ocorra uma exceção de E/S ou interrupção
        } finally {
            try {
                clientSocket.close(); // Fecha o socket do cliente
                System.out.println("<><><><CONEXAO FECHOU><><><>"); // Exibe mensagem indicando que a conexão foi fechada
            } catch (IOException e) {
                System.out.println("Close falhou"); // Exibe mensagem de erro caso ocorra uma exceção ao fechar o socket
            }
        }
    }
}