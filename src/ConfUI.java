import javax.swing.*;
import java.awt.*;

/*
 * Classe ConfUI responsável por criar componentes de interface gráfica
 * personalizados de forma padronizada para toda a aplicação.
 *
 * Inclui métodos para criar JFrame, JButton, JPanel e JLabel com estilo
 * consistente e aparência agradável.
 */

public class ConfUI
    {
        public static JFrame criarFrame(String titulo) // Cria e retorna um JFrame configurado com tamanho, cor de fundo e layout padrão.
            {
                JFrame frame = new JFrame(titulo);
                frame.setSize(450,320);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.getContentPane().setBackground(Color.decode("#f0f2f5"));
                return frame;
            }

        public static JButton criarBotao(String texto) // Cria e retorna um JButton estilizado.
            {
                JButton botao = new JButton(texto);
                botao.setFont(new Font("Arial", Font.BOLD, 16));
                botao.setBackground(Color.decode("#4CAF50"));
                botao.setForeground(Color.WHITE);
                botao.setFocusPainted(false);
                botao.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
                return botao;
            }

        public static JLabel criarTitulo(String texto) // Cria e retorna um JLabel estilizado como título.
            {
                JLabel label = new JLabel(texto);
                label.setFont(new Font("Arial", Font.BOLD, 28));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setForeground(Color.decode("#1E3A8A"));
                return label;
            }
    }