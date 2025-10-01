public class Cliente
    {
        public static void main(String[] args)
            {
                Menu menu = new Menu("localhost", 5050);  // Cria o menu principal do cliente, conectando ao servidor na máquina local na porta 5050
                menu.exibirMenu(); // Exibe o menu para o usuário
            }
    }