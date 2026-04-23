package frontend;

import modelo.Aluno;
import servicos.AutenticacaoSGA;
import servicos.SecretariaSGA;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AppGUI extends JFrame {

    private AutenticacaoSGA auth;
    private SecretariaSGA secretaria;

    private CardLayout cardLayout;
    private JPanel painelConteudo;

    // Componentes da Tabela
    private JTable tabelaDados;
    private DefaultTableModel modeloTabela;
    private JLabel lblTituloTabela;
    private String visualizacaoAtual = "MATRICULAS";

    // Paleta de Cores Profissional
    private final Color COR_PRIMARIA = new Color(41, 50, 65);     // Azul Escuro
    private final Color COR_SECUNDARIA = new Color(238, 108, 77); // Laranja
    private final Color COR_FUNDO = new Color(245, 247, 250);
    private final Color COR_TEXTO_SIDEBAR = new Color(200, 210, 220);
    private final Font FONTE_BASE = new Font("Segoe UI", Font.PLAIN, 14);

    public AppGUI() {
        this.auth = new AutenticacaoSGA();
        this.secretaria = new SecretariaSGA();

        setTitle("SGA - Sistema de Gestão Académica (ISUTC)");
        setSize(1050, 650); // Ligeiramente mais largo para caber todos os botões
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        painelConteudo.add(criarPainelLogin(), "LOGIN");
        painelConteudo.add(criarPainelSecretaria(), "SECRETARIA");

        add(painelConteudo);
        cardLayout.show(painelConteudo, "LOGIN");
    }

    private JPanel criarPainelLogin() {
        JPanel painelFundo = new JPanel(new GridBagLayout());
        painelFundo.setBackground(COR_FUNDO);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLogin = new JLabel("Login Administrativo", SwingConstants.CENTER);
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogin.setForeground(COR_PRIMARIA);
        gbc.gridy = 0; gbc.gridwidth = 2; card.add(lblLogin, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; card.add(new JLabel("Utilizador:"), gbc);
        JTextField txtUser = new JTextField("admin", 15); gbc.gridx = 1; card.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("Senha:"), gbc);
        JPasswordField txtPass = new JPasswordField("senha123", 15); gbc.gridx = 1; card.add(txtPass, gbc);

        JButton btnEntrar = new JButton("ENTRAR");
        estilizarBotaoAcao(btnEntrar, COR_SECUNDARIA, Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = new Insets(20, 0, 0, 0);
        card.add(btnEntrar, gbc);

        btnEntrar.addActionListener(e -> {
            if (auth.login(txtUser.getText(), new String(txtPass.getPassword()))) {
                cardLayout.show(painelConteudo, "SECRETARIA");
                atualizarTabela("MATRICULAS");
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais Inválidas!", "Erro de Acesso", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelFundo.add(card);
        return painelFundo;
    }

    private JPanel criarPainelSecretaria() {
        JPanel main = new JPanel(new BorderLayout());

        // --- SIDEBAR MODERNA ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COR_PRIMARIA);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        JLabel lblLogo = new JLabel("ISUTC SGA");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblLogo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton btnM = criarBotaoSidebar("1. Alunos Matriculados");
        JButton btnF = criarBotaoSidebar("2. Fila de Atendimento");
        JButton btnB = criarBotaoSidebar("3. Pedidos de Bolsa");
        JButton btnL = criarBotaoSidebar("Sair do Sistema");

        sidebar.add(btnM); sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnF); sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnB); sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnL);

        // --- ÁREA DE DADOS (TABELA) ---
        JPanel areaCentro = new JPanel(new BorderLayout(20, 20));
        areaCentro.setBackground(COR_FUNDO);
        areaCentro.setBorder(new EmptyBorder(30, 30, 30, 30));

        lblTituloTabela = new JLabel("Registos Atuais");
        lblTituloTabela.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloTabela.setForeground(new Color(61, 90, 128));

        modeloTabela = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaDados = new JTable(modeloTabela);
        tabelaDados.setRowHeight(35);
        tabelaDados.setFont(FONTE_BASE);
        tabelaDados.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelaDados.setSelectionBackground(new Color(238, 108, 77, 50));

        JScrollPane scroll = new JScrollPane(tabelaDados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        scroll.getViewport().setBackground(Color.WHITE);

        // --- PAINEL DE TODAS AS OPERAÇÕES OBRIGATÓRIAS ---
        JPanel painelOperacoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelOperacoes.setBackground(COR_FUNDO);

        JButton btnPesquisar = new JButton("Pesquisar ID");
        JButton btnOrdenar = new JButton("Ordenar A-Z");
        JButton btnRelatorio = new JButton("Gerar Relatório");
        JButton btnAdicionar = new JButton("Nova Operação");
        JButton btnProcessar = new JButton("Processar Fila/Heap");

        // Estilização diferenciada para ajudar o utilizador a distinguir as ações
        estilizarBotaoAcao(btnPesquisar, new Color(74, 144, 226), Color.WHITE);
        estilizarBotaoAcao(btnOrdenar, new Color(42, 157, 143), Color.WHITE);
        estilizarBotaoAcao(btnRelatorio, new Color(108, 117, 125), Color.WHITE);
        estilizarBotaoAcao(btnAdicionar, COR_SECUNDARIA, Color.WHITE);
        estilizarBotaoAcao(btnProcessar, COR_PRIMARIA, Color.WHITE);

        painelOperacoes.add(btnPesquisar);
        painelOperacoes.add(btnOrdenar);
        painelOperacoes.add(btnRelatorio);
        painelOperacoes.add(btnAdicionar);
        painelOperacoes.add(btnProcessar);

        areaCentro.add(lblTituloTabela, BorderLayout.NORTH);
        areaCentro.add(scroll, BorderLayout.CENTER);
        areaCentro.add(painelOperacoes, BorderLayout.SOUTH);

        main.add(sidebar, BorderLayout.WEST);
        main.add(areaCentro, BorderLayout.CENTER);

        // Listeners de Navegação
        btnM.addActionListener(e -> atualizarTabela("MATRICULAS"));
        btnF.addActionListener(e -> atualizarTabela("FILA"));
        btnB.addActionListener(e -> atualizarTabela("BOLSA"));
        btnL.addActionListener(e -> cardLayout.show(painelConteudo, "LOGIN"));

        // Listeners de Inserção e Processamento
        btnAdicionar.addActionListener(e -> acaoInserir());
        btnProcessar.addActionListener(e -> acaoProcessar());

        // --- LÓGICA DAS NOVAS OPERAÇÕES ---

        // PESQUISA
        btnPesquisar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o ID do Aluno para Pesquisar:"));
                Aluno a = secretaria.pesquisarAlunoPorId(id);
                if (a != null) {
                    JOptionPane.showMessageDialog(this, "ALUNO ENCONTRADO:\nID: " + a.getNumero() + "\nNome: " + a.getNome(), "Resultado da Pesquisa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum aluno matriculado com o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) { /* Tratamento silencioso caso feche a janela */ }
        });

        // ORDENAÇÃO
        btnOrdenar.addActionListener(e -> {
            if (visualizacaoAtual.equals("MATRICULAS")) {
                modeloTabela.setRowCount(0);
                for (Aluno a : secretaria.obterMatriculadosOrdenadosPorNome()) {
                    modeloTabela.addRow(new Object[]{a.getNumero(), a.getNome(), "ATIVO (Ordenado)"});
                }
                JOptionPane.showMessageDialog(this, "Lista ordenada via Bubble Sort.", "Ordenação Concluída", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Aceda à aba 'Alunos Matriculados' para ordenar a lista principal.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // RELATÓRIO DE DESEMPENHO
        btnRelatorio.addActionListener(e -> {
            JTextArea textArea = new JTextArea(secretaria.gerarRelatorioDesempenho());
            textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            textArea.setEditable(false);
            textArea.setBackground(new Color(245, 245, 245));
            textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Relatório de Desempenho", JOptionPane.INFORMATION_MESSAGE);
        });

        return main;
    }

    private void atualizarTabela(String tipo) {
        this.visualizacaoAtual = tipo;
        modeloTabela.setRowCount(0);

        if (tipo.equals("MATRICULAS")) {
            lblTituloTabela.setText("Alunos Matriculados (Lista Duplamente Ligada)");
            modeloTabela.setColumnIdentifiers(new String[]{"ID Estudante", "Nome do Aluno", "Vínculo"});
            for (Aluno a : secretaria.obterMatriculados()) {
                modeloTabela.addRow(new Object[]{a.getNumero(), a.getNome(), "ATIVO"});
            }
        } else if (tipo.equals("FILA")) {
            lblTituloTabela.setText("Fila de Espera (FIFO - First-In First-Out)");
            modeloTabela.setColumnIdentifiers(new String[]{"Posição", "ID Estudante", "Nome"});
            Aluno[] fila = secretaria.obterFilaEspera();
            for (int i = 0; i < fila.length; i++) {
                modeloTabela.addRow(new Object[]{(i+1) + "º", fila[i].getNumero(), fila[i].getNome()});
            }
        } else if (tipo.equals("BOLSA")) {
            lblTituloTabela.setText("Candidaturas a Bolsa (Heap - Prioridade por Média)");
            modeloTabela.setColumnIdentifiers(new String[]{"ID Estudante", "Nome", "Média Académica"});
            for (Aluno a : secretaria.obterBolsasPrioridade()) {
                modeloTabela.addRow(new Object[]{a.getNumero(), a.getNome(), a.getMediaAcademica()});
            }
        }
    }

    private void acaoInserir() {
        try {
            if (visualizacaoAtual.equals("MATRICULAS")) {
                String nome = JOptionPane.showInputDialog(this, "Nome Completo do Aluno:");

                if (nome != null && !nome.trim().isEmpty()) {
                    Aluno recemMatriculado = secretaria.matricularAluno(nome);
                    JOptionPane.showMessageDialog(this,
                            "Matrícula realizada com sucesso!\n" +
                                    "Estudante: " + recemMatriculado.getNome() + "\n" +
                                    "ID Gerado: " + recemMatriculado.getNumero(),
                            "Matrícula Concluída", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    return;
                }

            } else if (visualizacaoAtual.equals("FILA")) {
                int id = Integer.parseInt(JOptionPane.showInputDialog("ID do Aluno:"));
                String nome = JOptionPane.showInputDialog("Confirme o Nome:");
                if (secretaria.adicionarParaAtendimento(new Aluno(id, nome, 0.0))) {
                    JOptionPane.showMessageDialog(this, "Aluno inserido na fila de atendimento.");
                } else {
                    JOptionPane.showMessageDialog(this, "ERRO: Aluno não matriculado ou dados divergentes!", "Falha de Integridade", JOptionPane.ERROR_MESSAGE);
                }

            } else if (visualizacaoAtual.equals("BOLSA")) {
                int id = Integer.parseInt(JOptionPane.showInputDialog("ID do Aluno:"));
                String nome = JOptionPane.showInputDialog("Nome:");
                double med = Double.parseDouble(JOptionPane.showInputDialog("Média Académica (0-20):"));

                if (med < 0 || med > 20) {
                    JOptionPane.showMessageDialog(this, "Média Inválida! Insira um valor entre 0 e 20.", "Erro de Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (secretaria.submeterCandidaturaBolsa(new Aluno(id, nome, med))) {
                    JOptionPane.showMessageDialog(this, "Candidatura submetida com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "ERRO: Verificação de matrícula falhou!", "Falha de Integridade", JOptionPane.ERROR_MESSAGE);
                }
            }

            atualizarTabela(visualizacaoAtual);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Certifique-se de que insere apenas números válidos no ID e na Média.", "Erro de Formatação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {}
    }

    private void acaoProcessar() {
        if (visualizacaoAtual.equals("FILA")) {
            Aluno a = secretaria.proximoAAtender();
            if (a != null) JOptionPane.showMessageDialog(this, "Atendimento Concluído: " + a.getNome());
            else JOptionPane.showMessageDialog(this, "Não há ninguém na fila de espera.");
        } else if (visualizacaoAtual.equals("BOLSA")) {
            Aluno a = secretaria.aprovarProximaBolsa();
            if (a != null) JOptionPane.showMessageDialog(this, "BOLSA APROVADA\nBeneficiário: " + a.getNome() + "\nMédia: " + a.getMediaAcademica());
            else JOptionPane.showMessageDialog(this, "Nenhuma candidatura pendente no Heap.");
        } else {
            JOptionPane.showMessageDialog(this, "Selecione 'Fila de Atendimento' ou 'Pedidos de Bolsa' para processar registros.");
        }
        atualizarTabela(visualizacaoAtual);
    }

    // --- AUXILIARES DE ESTILIZAÇÃO ---

    private JButton criarBotaoSidebar(String txt) {
        JButton b = new JButton(txt);
        b.setMaximumSize(new Dimension(230, 45));
        b.setFont(FONTE_BASE);
        b.setForeground(COR_TEXTO_SIDEBAR);
        b.setBackground(COR_PRIMARIA);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setForeground(Color.WHITE); b.setBackground(new Color(61, 90, 128)); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setForeground(COR_TEXTO_SIDEBAR); b.setBackground(COR_PRIMARIA); }
        });
        return b;
    }

    private void estilizarBotaoAcao(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(150, 40));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
    }
}