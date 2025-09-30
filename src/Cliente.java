public class Cliente
    {
        public static void main(String[] args)
            {
                Menu menu = new Menu("localhost", 8080);  // Cria o menu principal do cliente, conectando ao servidor na máquina local na porta 8080
                menu.exibirMenu(); // Exibe o menu para o usuário
            }
    }