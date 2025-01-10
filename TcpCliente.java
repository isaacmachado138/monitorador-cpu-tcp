import java.net.*; // Importa classes para trabalhar com sockets
import java.io.*; // Importa classes para entrada e saída de dados

public class TcpCliente {
    public static void main(String args[]) {
        Socket s = null; // Declaração do socket do cliente

        try {
            int serverPort = 7896; // Define a porta do servidor
            s = new Socket(args[1], serverPort); // Cria um socket e conecta ao servidor no endereço IP e porta especificados
            DataInputStream in = new DataInputStream(s.getInputStream()); // Inicializa o fluxo de entrada de dados
            DataOutputStream out = new DataOutputStream(s.getOutputStream()); // Inicializa o fluxo de saída de dados

            out.writeUTF(args[0]); // Envia a mensagem especificada no primeiro argumento para o servidor

            for (int i = 0; i < 12; i++) { // Loop para ler e exibir 12 mensagens do servidor
                String data = in.readUTF(); // Lê a mensagem enviada pelo servidor
                System.out.println(data); // Exibe a mensagem recebida
            }
        } catch (UnknownHostException e) {
            System.out.println("Sock: " + e.getMessage()); // Exibe mensagem de erro caso o endereço do host seja desconhecido
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage()); // Exibe mensagem de erro caso ocorra uma exceção de fim de arquivo
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage()); // Exibe mensagem de erro caso ocorra uma exceção de E/S
        } finally {
            if (s != null) {
                try {
                    s.close(); // Fecha o socket do cliente
                } catch (IOException e) {
                    System.out.println("Close falhou"); // Exibe mensagem de erro caso ocorra uma exceção ao fechar o socket
                }
            }
        }
    }
}