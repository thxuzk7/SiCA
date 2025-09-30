import java.io.*;
import java.net.Socket;

/*
 * Classe TrataRequisicoes responsável por processar as requisições
 * recebidas de um cliente em uma thread separada.
 *
 * Suporta as ações: UPLOAD, DOWNLOAD e LISTAR arquivos.
 */

public class TrataRequisicoes implements Runnable
    {
        private Socket socket; // Socket associado ao cliente conectado
        private final File pastaSiCA = new File("pastaSiCA"); // Pasta onde os arquivos do servidor serão armazenados

        // Construtor que recebe o socket do cliente.
        // Cria a pasta de armazenamento se não existir.

        public TrataRequisicoes(Socket socket)
            {
                this.socket = socket;
                if (!pastaSiCA.exists()) pastaSiCA.mkdir();
            }

        @Override
        public void run() // Metodo executado pela thread, processando as requisições do cliente.
            {
                try (DataInputStream in = new DataInputStream(socket.getInputStream());
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream())
                    )
                        {
                            String acao = in.readUTF(); // Lê a ação solicitada pelo cliente

                            switch(acao.toUpperCase())
                                {
                                    case "UPLOAD" -> // Caso de upload de arquivo
                                        {
                                            String nomeArquivo = in.readUTF(); // Nome do arquivo
                                            long tamanho = in.readLong(); // Tamanho do arquivo
                                            File arquivoDestino = new File(pastaSiCA, nomeArquivo);

                                            // Grava o arquivo recebido na pasta do servidor
                                            try (FileOutputStream fos = new FileOutputStream(arquivoDestino))
                                                {
                                                    byte[] buffer = new byte[4096];
                                                    long lidos = 0;
                                                    int read;

                                                    while (lidos < tamanho && (read = in.read(buffer, 0, (int)Math.min(buffer.length, tamanho - lidos))) != -1)
                                                        {
                                                            fos.write(buffer, 0, read);
                                                            lidos += read;
                                                        }
                                                }

                                            out.writeUTF("UPLOAD FEITO COM SUCESSO"); // Confirmação de upload concluído
                                        }
                                    case "LISTAR" -> // Caso de listagem de arquivos
                                        {
                                            File[] arquivos = pastaSiCA.listFiles(); // Lista arquivos na pasta
                                            out.writeInt(arquivos.length); // Envia quantidade de arquivos

                                            for (File f : arquivos)
                                                {
                                                    out.writeUTF(f.getName()); // Envia nome de cada arquivo
                                                }
                                        }
                                    case "DOWNLOAD" -> // Caso de download de arquivo
                                        {
                                            String nomeArquivo = in.readUTF(); // Nome do arquivo a ser baixado
                                            File arquivo = new File(pastaSiCA, nomeArquivo);

                                            if  (arquivo.exists())
                                                {
                                                    out.writeLong(arquivo.length()); // Envia tamanho do arquivo
                                                    try (FileInputStream fis = new FileInputStream(arquivo))
                                                        {
                                                            byte[] buffer = new byte[4096];
                                                            int read;

                                                            // Envia o arquivo em blocos
                                                            while((read = fis.read(buffer)) != -1)
                                                                {
                                                                    out.write(buffer, 0, read);
                                                                }
                                                        }
                                                }
                                            else
                                                {
                                                    out.writeLong(-1); // Indica que o arquivo não foi encontrado
                                                }
                                        }
                                    default -> out.writeUTF("AÇÃO_INVÁLIDA");  // Caso de ação inválida
                                }
                        }
                catch(IOException e)
                        {
                            e.printStackTrace();
                        }
            }
    }