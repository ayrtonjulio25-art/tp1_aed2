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

    // --- BOTÕES DEDICADOS E EXPLÍCITOS ---
    private JButton btnNovaMatricula;
    private JButton btnEntrarFila;
    private JButton btnAtenderFila;
    private JButton btnSubmeterBolsa;
    private JButton btnAtribuirBolsa;

    // Paleta de Cores Profissional
    private final Color COR_PRIMARIA = new Color(41, 50, 65);
    private final Color COR_SECUNDARIA = new Color(238, 108, 77);
    private final Color COR_FUNDO = new Color(245, 247, 250);
    private final Color COR_TEXTO_SIDEBAR = new Color(200, 210, 220);
    private final Font FONTE_BASE = new Font("Segoe UI", Font.PLAIN, 14);

    public AppGUI() {
        this.auth = new AutenticacaoSGA();
        this.secretaria = new SecretariaSGA();

        setTitle("SGA - Sistema de Gestão Académica");
        setSize(1100, 650);
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

        gbc.gridwidth = 1; gbc.gridy = 1; card.add(new JLabel("Utilizador:"), gbc);
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

                if(visualizacaoAtual.equals("LOGS")) atualizarTabela("LOGS");
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

        JButton btnM = criarBotaoSidebar("Alunos Matriculados");
        JButton btnF = criarBotaoSidebar("Fila de Atendimento");
        JButton btnB = criarBotaoSidebar("Pedidos de Bolsa");
        JButton btnLogs = criarBotaoSidebar("Histórico de Acessos");
        JButton btnL = criarBotaoSidebar("Sair do Sistema");

        sidebar.add(btnM); sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnF); sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnB); sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnLogs); sidebar.add(Box.createVerticalGlue());
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

        // --- PAINEL DE TODAS AS OPERAÇÕES ---
        JPanel painelOperacoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelOperacoes.setBackground(COR_FUNDO);

        // Botões Gerais (Sempre visíveis)
        JButton btnPesquisar = new JButton("Pesquisar ID");
        JButton btnOrdenar = new JButton("Ordenar A-Z");
        JButton btnRelatorio = new JButton("Gerar Relatório");

        // Botões Contextuais (Aparecem dependendo da Aba)
        btnNovaMatricula = new JButton("Nova Matrícula");
        btnEntrarFila = new JButton("Entrar na Fila");
        btnAtenderFila = new JButton("Atender Fila");
        btnSubmeterBolsa = new JButton("Submeter Candidatura");
        btnAtribuirBolsa = new JButton("Atribuir Bolsa");

        // Estilização
        estilizarBotaoAcao(btnPesquisar, new Color(74, 144, 226), Color.WHITE);
        estilizarBotaoAcao(btnOrdenar, new Color(42, 157, 143), Color.WHITE);
        estilizarBotaoAcao(btnRelatorio, new Color(108, 117, 125), Color.WHITE);

        estilizarBotaoAcao(btnNovaMatricula, COR_SECUNDARIA, Color.WHITE);
        estilizarBotaoAcao(btnEntrarFila, COR_SECUNDARIA, Color.WHITE);
        estilizarBotaoAcao(btnAtenderFila, COR_PRIMARIA, Color.WHITE);
        estilizarBotaoAcao(btnSubmeterBolsa, COR_SECUNDARIA, Color.WHITE);
        estilizarBotaoAcao(btnAtribuirBolsa, new Color(46, 204, 113), Color.WHITE);

        // Adicionar os botões ao painel
        painelOperacoes.add(btnPesquisar);
        painelOperacoes.add(btnOrdenar);
        painelOperacoes.add(btnRelatorio);
        painelOperacoes.add(btnNovaMatricula);
        painelOperacoes.add(btnEntrarFila);
        painelOperacoes.add(btnAtenderFila);
        painelOperacoes.add(btnSubmeterBolsa);
        painelOperacoes.add(btnAtribuirBolsa);

        areaCentro.add(lblTituloTabela, BorderLayout.NORTH);
        areaCentro.add(scroll, BorderLayout.CENTER);
        areaCentro.add(painelOperacoes, BorderLayout.SOUTH);

        main.add(sidebar, BorderLayout.WEST);
        main.add(areaCentro, BorderLayout.CENTER);

        // --- LISTENERS DE NAVEGAÇÃO ---
        btnM.addActionListener(e -> atualizarTabela("MATRICULAS"));
        btnF.addActionListener(e -> atualizarTabela("FILA"));
        btnB.addActionListener(e -> atualizarTabela("BOLSA"));
        btnLogs.addActionListener(e -> atualizarTabela("LOGS")); // NAVEGAÇÃO PARA OS LOGS
        btnL.addActionListener(e -> cardLayout.show(painelConteudo, "LOGIN"));

        // --- LISTENERS DAS OPERAÇÕES GERAIS ---
        btnPesquisar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite o ID do Aluno para Pesquisar:"));
                Aluno a = secretaria.pesquisarAlunoPorId(id);
                if (a != null) JOptionPane.showMessageDialog(this, "ALUNO ENCONTRADO:\nID: " + a.getNumero() + "\nNome: " + a.getNome(), "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(this, "Nenhum aluno matriculado com o ID: " + id, "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) { }
        });

        btnOrdenar.addActionListener(e -> {
            if (visualizacaoAtual.equals("MATRICULAS")) {
                modeloTabela.setRowCount(0);
                for (Aluno a : secretaria.obterMatriculadosOrdenadosPorNome()) modeloTabela.addRow(new Object[]{a.getNumero(), a.getNome(), "ATIVO (Ordenado)"});
                JOptionPane.showMessageDialog(this, "Lista ordenada via Bubble Sort.", "Ordenação", JOptionPane.INFORMATION_MESSAGE);
            } else JOptionPane.showMessageDialog(this, "Aceda à aba 'Alunos Matriculados' para ordenar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        });

        btnRelatorio.addActionListener(e -> {
            JTextArea textArea = new JTextArea(secretaria.gerarRelatorioDesempenho());
            textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            textArea.setEditable(false);
            textArea.setBackground(new Color(245, 245, 245));
            textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Relatório", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- LISTENERS DAS OPERAÇÕES CONTEXTUAIS ---
        btnNovaMatricula.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome Completo do Aluno:");
            if (nome != null && !nome.trim().isEmpty()) {
                Aluno a = secretaria.matricularAluno(nome);
                JOptionPane.showMessageDialog(this, "Matrícula efetuada com sucesso!\nID Gerado: " + a.getNumero(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela(visualizacaoAtual);
            }
        });

        btnEntrarFila.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome do Aluno:");
            if (nome == null || nome.trim().isEmpty()) return;
            if (secretaria.adicionarParaAtendimento(nome.trim())) {
                JOptionPane.showMessageDialog(this, "Aluno inserido na fila de atendimento.");
                atualizarTabela(visualizacaoAtual);
            } else {
                JOptionPane.showMessageDialog(this, "ERRO: Aluno não matriculado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAtenderFila.addActionListener(e -> {
            Aluno a = secretaria.proximoAAtender();
            if (a != null) JOptionPane.showMessageDialog(this, "Atendimento Concluído para: " + a.getNome(), "Fila FIFO", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, "Não há ninguém na fila de espera.", "Aviso", JOptionPane.WARNING_MESSAGE);
            atualizarTabela(visualizacaoAtual);
        });

        btnSubmeterBolsa.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome do Aluno:");
            if (nome == null || nome.trim().isEmpty()) return;
            String medStr = JOptionPane.showInputDialog(this, "Média Académica (0-20):");
            if (medStr == null) return;
            try {
                double med = Double.parseDouble(medStr);
                if (med < 0 || med > 20) { JOptionPane.showMessageDialog(this, "Média Inválida!", "Erro", JOptionPane.WARNING_MESSAGE); return; }

                if (secretaria.submeterCandidaturaBolsa(nome.trim(), med)) {
                    JOptionPane.showMessageDialog(this, "Candidatura submetida com sucesso!");
                    atualizarTabela(visualizacaoAtual);
                } else {
                    JOptionPane.showMessageDialog(this, "ERRO: Aluno não matriculado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Média Inválida!", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnAtribuirBolsa.addActionListener(e -> {
            Aluno a = secretaria.aprovarProximaBolsa();
            if (a != null) {
                JOptionPane.showMessageDialog(this, "BOLSA ATRIBUÍDA COM SUCESSO!\n\nEstudante contemplado: " + a.getNome() + "\nMédia Académica: " + a.getMediaAcademica(), "Decisão do Max-Heap", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma candidatura pendente no Heap.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
            atualizarTabela(visualizacaoAtual);
        });

        return main;
    }

    private void atualizarTabela(String tipo) {
        this.visualizacaoAtual = tipo;
        modeloTabela.setRowCount(0);

        // Controla que botões aparecem no painel baseando-se na aba atual
        btnNovaMatricula.setVisible(tipo.equals("MATRICULAS"));
        btnEntrarFila.setVisible(tipo.equals("FILA"));
        btnAtenderFila.setVisible(tipo.equals("FILA"));
        btnSubmeterBolsa.setVisible(tipo.equals("BOLSA"));
        btnAtribuirBolsa.setVisible(tipo.equals("BOLSA"));

        if (tipo.equals("MATRICULAS")) {
            lblTituloTabela.setText("Alunos Matriculados");
            modeloTabela.setColumnIdentifiers(new String[]{"ID Estudante", "Nome do Aluno", "Vínculo"});
            for (Aluno a : secretaria.obterMatriculados()) modeloTabela.addRow(new Object[]{a.getNumero(), a.getNome(), "ATIVO"});

        } else if (tipo.equals("FILA")) {
            lblTituloTabela.setText("Fila de Espera (FIFO)");
            modeloTabela.setColumnIdentifiers(new String[]{"Posição", "ID Estudante", "Nome"});
            Aluno[] fila = secretaria.obterFilaEspera();
            for (int i = 0; i < fila.length; i++) modeloTabela.addRow(new Object[]{(i+1) + "º", fila[i].getNumero(), fila[i].getNome()});

        } else if (tipo.equals("BOLSA")) {
            lblTituloTabela.setText("Candidaturas a Bolsa (Max-Heap)");
            modeloTabela.setColumnIdentifiers(new String[]{"ID Estudante", "Nome", "Média Académica"});
            for (Aluno a : secretaria.obterBolsasPrioridade()) modeloTabela.addRow(new Object[]{a.getNumero(), a.getNome(), a.getMediaAcademica()});

        } else if (tipo.equals("LOGS")) {
            lblTituloTabela.setText("Histórico de Acessos (Pilha LIFO)");
            modeloTabela.setColumnIdentifiers(new String[]{"Tempo", "Registo do Evento"});

            // Chama o método que adicionámos ao AutenticacaoSGA
            try {
                String[] logs = auth.obterHistoricoParaInterface();
                for (int i = 0; i < logs.length; i++) {
                    if (logs[i] != null) {
                        String indicador = (i == 0) ? " " : "";
                        modeloTabela.addRow(new Object[]{"T-" + i, logs[i] + indicador});
                    }
                }
            } catch (Exception ex) {
                modeloTabela.addRow(new Object[]{"-", "Aviso: Método 'obterHistoricoParaInterface()' não encontrado em AutenticacaoSGA."});
            }
        }
    }

    // --- AUXILIARES DE ESTILIZAÇÃO ---
    private JButton criarBotaoSidebar(String txt) {
        JButton b = new JButton(txt);
        b.setMaximumSize(new Dimension(230, 45));
        b.setFont(FONTE_BASE);
        b.setForeground(COR_TEXTO_SIDEBAR);
        b.setBackground(COR_PRIMARIA);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
    }
}