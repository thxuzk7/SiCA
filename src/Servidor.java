import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor
    {
        private static final int PORTA = 8080; // Define a porta em que o servidor irá escutar

        public static void main(String[] args)
            {
                try (ServerSocket serverSocket = new ServerSocket(PORTA))
                    {
                        while(true) // Loop infinito para aceitar conexões de clientes
                            {
                                Socket clienteSocket = serverSocket.accept(); // Aguarda e aceita a conexão de um cliente
                                new Thread(new TrataRequisicoes(clienteSocket)).start(); // Cria uma nova thread para tratar as requisições do cliente
                            }
                    }
                catch (IOException e)
                    {
                        e.printStackTrace(); // Caso ocorra algum erro na criação do ServerSocket ou na aceitação de clientes
                    }
            }
    }