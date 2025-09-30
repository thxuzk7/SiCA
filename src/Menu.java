import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Menu
    {
        private JFrame frame; // Janela principal do menu
        private final ClienteRequisicoes requisicoes; // Objeto responsável pelas requisições ao servidor

        // Construtor que inicializa a classe com host e porta do servidor.
        public Menu(String host, int porta)
            {
                requisicoes = new ClienteRequisicoes(host, porta);
            }

        public void exibirMenu() // Exibe o menu principal com opções de enviar arquivo ou listar arquivos.
            {
                frame = ConfUI.criarFrame("SiCA");

                // Painel principal com layout vertical
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(Color.decode("#f0f2f5"));
                panel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

                // Espaçamento no topo
                panel.add(Box.createRigidArea(new Dimension(0, 30)));

                // Título centralizado
                JLabel titulo = ConfUI.criarTitulo("SiCA");
                titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(titulo);

                panel.add(Box.createRigidArea(new Dimension(0, 20)));

                // Botão para enviar arquivo
                JButton btnUpload = ConfUI.criarBotao("Enviar Arquivo");
                btnUpload.setAlignmentX(Component.CENTER_ALIGNMENT);

                btnUpload.addActionListener(e -> {
                    frame.dispose(); // Fecha menu atual
                    escolherArquivoParaUpload(); // Abre diálogo de upload
                });

                panel.add(btnUpload);

                panel.add(Box.createRigidArea(new Dimension(0, 15)));

                // Botão para listar arquivos
                JButton btnListar = ConfUI.criarBotao("Listar Arquivos");
                btnListar.setAlignmentX(Component.CENTER_ALIGNMENT);

                btnListar.addActionListener(e -> {
                    frame.dispose(); // Fecha menu atual
                    listarArquivos(); // Abre janela de listagem
                });
                panel.add(btnListar);

                frame.add(panel, BorderLayout.CENTER);
                frame.setVisible(true);
            }

        // Abre diálogo para o usuário escolher um arquivo para upload.
        private void escolherArquivoParaUpload()
            {
                JFileChooser chooser = new JFileChooser();
                int retorno = chooser.showOpenDialog(null);

                if (retorno == JFileChooser.APPROVE_OPTION)
                    {
                        File arquivoSelecionado = chooser.getSelectedFile();
                        String resposta = requisicoes.uploadArquivo(arquivoSelecionado);
                        JOptionPane.showMessageDialog(null, resposta);
                    }

                exibirMenu(); // Volta para o menu principal após o upload
            }

        // Exibe todos os arquivos disponíveis no servidor.
        // Permite baixar arquivos individualmente.
        private void listarArquivos()
            {
                String[] arquivos = requisicoes.listarArquivos();
                JFrame listaFrame = new JFrame("Arquivos no Servidor");
                listaFrame.setSize(500, 400);
                listaFrame.setLocationRelativeTo(null);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(Color.decode("#f0f2f5"));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                if  (arquivos.length == 0) // Caso a pasta esteja vazia
                    {
                        JLabel vazioLabel = new JLabel("Pasta vazia");
                        vazioLabel.setFont(new Font("Arial", Font.BOLD, 18));
                        vazioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        panel.add(Box.createVerticalGlue());
                        panel.add(vazioLabel);
                        panel.add(Box.createRigidArea(new Dimension(0, 20)));
                    }
                else
                    {
                        for (String nome : arquivos) // Lista cada arquivo com botão de download
                            {
                                JPanel arquivoPanel = new JPanel();
                                arquivoPanel.setLayout(new BoxLayout(arquivoPanel, BoxLayout.Y_AXIS));
                                arquivoPanel.setBackground(Color.WHITE);
                                arquivoPanel.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                                ));

                                JLabel lblNome = new JLabel(nome);
                                lblNome.setFont(new Font("Arial", Font.PLAIN, 16));
                                lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

                                JButton btnDownload = ConfUI.criarBotao("Download");
                                btnDownload.setAlignmentX(Component.CENTER_ALIGNMENT);
                                btnDownload.addActionListener(e -> baixarArquivo(nome));

                                arquivoPanel.add(lblNome);
                                arquivoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                                arquivoPanel.add(btnDownload);
                                arquivoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                                panel.add(arquivoPanel);
                                panel.add(Box.createRigidArea(new Dimension(0, 10)));
                            }
                    }

                // Botão para voltar ao menu principal
                JButton btnVoltar = ConfUI.criarBotao("Voltar");
                btnVoltar.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnVoltar.setFont(new Font("Arial", Font.PLAIN, 14));
                btnVoltar.setMaximumSize(new Dimension(100, 30));

                btnVoltar.addActionListener(e -> {
                    listaFrame.dispose();
                    exibirMenu();
                });

                panel.add(Box.createVerticalGlue());
                panel.add(btnVoltar);

                JScrollPane scroll = new JScrollPane(panel);
                scroll.getVerticalScrollBar().setUnitIncrement(16);
                listaFrame.add(scroll);
                listaFrame.setVisible(true);
            }

        // Baixa o arquivo selecionado pelo usuário para um local escolhido.
        private void baixarArquivo(String nomeArquivo)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new File(nomeArquivo));
                int retorno = chooser.showSaveDialog(null);
                if (retorno != JFileChooser.APPROVE_OPTION) return;

                File destino = chooser.getSelectedFile();
                String resultado = requisicoes.downloadArquivo(nomeArquivo, destino);
                JOptionPane.showMessageDialog(null, resultado);
            }
    }