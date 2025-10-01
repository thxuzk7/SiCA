SiCA - Sistema Cliente-Servidor
SiCA é um projeto em Java que implementa uma arquitetura Cliente-Servidor, permitindo que múltiplas máquinas em uma mesma rede local troquem informações e documentos de forma prática e eficiente.

Requisitos
- Java JDK 8 ou superior instalado em todas as máquinas.
- As máquinas devem estar na mesma rede local (LAN), via cabo ou Wi-Fi.
- A porta 5050 deve estar liberada no firewall da máquina que atuará como servidor.

Passo a passo para executar
1. Descobrir o IP da máquina Servidora
- No computador que será o servidor, abra o Prompt de Comando (Win + R → digite cmd).
- Digite o comando: ipconfig
- Localize o valor de Endereço IPv4 (exemplo: 192.168.1.15).

2. Alterar o IP no Cliente
- Abra a classe Cliente.java.
- Localize a linha: Menu menu = new Menu("localhost", 5050);
- Substitua "localhost" pelo IP descoberto no passo anterior: Menu menu = new Menu("192.168.1.15", 5050);

3. Compilar o programa em todas as máquinas
- Navegue até a pasta src do projeto.
- Compile todos os arquivos Java: javac *.java

4. Rodar o Servidor
- No computador que será o servidor, execute: java Servidor

5. Rodar o Cliente
- No computador que será o cliente, execute: java Cliente
