import java.io.*;
import java.net.Socket;

public class ClienteRequisicoes
    {
        private final String host; // Host do servidor
        private final int porta; // Porta do servidor

        public ClienteRequisicoes(String host, int porta) //Construtor que define host e porta do servidor.
            {
                this.host = host;
                this.porta = porta;
            }

        public String uploadArquivo(File arquivo) // Envia um arquivo para o servidor.
            {
                try (Socket socket = new Socket(host, porta);
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                     DataInputStream in = new DataInputStream(socket.getInputStream());
                     FileInputStream fis = new FileInputStream(arquivo)
                    )
                        {
                            // Envia comando e informações do arquivo
                            out.writeUTF("UPLOAD");
                            out.writeUTF(arquivo.getName());
                            out.writeLong(arquivo.length());

                            // Lê o arquivo e envia em blocos
                            byte[] buffer = new byte[4096];
                            int read;

                            while ((read = fis.read(buffer)) != -1)
                                {
                                    out.write(buffer, 0, read);
                                }

                            return in.readUTF();  // Recebe resposta do servidor

                        }
                catch (IOException e)
                        {
                            e.printStackTrace();
                            return "Erro ao enviar arquivo: " + e.getMessage();
                        }
            }

        public String[] listarArquivos() // Lista os arquivos disponíveis no servidor.
            {
                try (Socket socket = new Socket(host, porta);
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                     DataInputStream in = new DataInputStream(socket.getInputStream())
                    )
                        {
                            out.writeUTF("LISTAR"); // Envia comando para listar arquivos

                            // Recebe quantidade de arquivos e armazena em array
                            int total = in.readInt();
                            String[] arquivos = new String[total];

                            for (int i = 0; i < total; i++)
                                {
                                    arquivos[i] = in.readUTF();
                                }

                            return arquivos;
                        }
                catch (IOException e)
                        {
                            e.printStackTrace();
                            return new String[0];
                        }
            }

        public String downloadArquivo(String nomeArquivo, File destino) //Faz o download de um arquivo do servidor.
            {
                try (Socket socket = new Socket(host, porta);
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                     DataInputStream in = new DataInputStream(socket.getInputStream());
                     FileOutputStream fos = new FileOutputStream(destino)
                    )
                        {
                            // Envia comando e nome do arquivo
                            out.writeUTF("DOWNLOAD");
                            out.writeUTF(nomeArquivo);

                            // Recebe tamanho do arquivo
                            long tamanho = in.readLong();
                            if (tamanho == -1) return "Arquivo não encontrado no servidor";

                            // Lê e grava o arquivo em blocos
                            byte[] buffer = new byte[4096];
                            long lidos = 0;
                            int read;

                            while(lidos < tamanho && (read = in.read(buffer, 0, (int)Math.min(buffer.length, tamanho - lidos))) != -1)
                                {
                                    fos.write(buffer, 0, read);
                                    lidos += read;
                                }

                            return "Download concluído: " + destino.getAbsolutePath();
                        }
                catch (IOException e)
                        {
                            e.printStackTrace();
                            return "Erro ao baixar arquivo: " + e.getMessage();
                        }
            }
    }