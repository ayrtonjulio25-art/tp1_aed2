package servicos;

import estruturas.dispersao.TabelaHashUtilizadores;
import estruturas.lineares.Pilha;
import modelo.Utilizador;

public class AutenticacaoSGA {
    private TabelaHashUtilizadores utilizadores;
    private Pilha historicoLogins;

    public AutenticacaoSGA() {
        this.utilizadores = new TabelaHashUtilizadores();
        this.historicoLogins = new Pilha();

        // Seed de dados: Utilizadores iniciais do sistema
        this.utilizadores.adiciona(new Utilizador("admin", "senha123"));
        this.utilizadores.adiciona(new Utilizador("secretaria", "senha123"));
        this.utilizadores.adiciona(new Utilizador("anaestudante", "senha123"));
        this.utilizadores.adiciona(new Utilizador("anaestudante1", "senha123"));


        // Registo inicial no histórico
        this.historicoLogins.push("SISTEMA: Utilizadores iniciais carregados.");
    }

    // Método para permitir o cadastro de novos utilizadores dinamicamente
    public void cadastrarUsuario(String username, String password) {
        if (username != null && !username.trim().isEmpty()) {
            this.utilizadores.adiciona(new Utilizador(username, password));
            this.historicoLogins.push("SISTEMA: Novo utilizador criado - " + username);
        }
    }

    public boolean login(String username, String password) {
        Utilizador u = utilizadores.busca(username);
        if (u != null && u.getPassword().equals(password)) {
            historicoLogins.push("LOGIN SUCESSO: " + username);
            return true;
        }
        historicoLogins.push("LOGIN FALHADO: " + username);
        return false;
    }

    public void imprimirHistorico() {
        System.out.println("--- Histórico de Acessos ---");
        String[] logs = obterHistoricoParaInterface();
        for (String log : logs) {
            System.out.println(log);
        }
    }

    // Método corrigido para ler a Pilha respeitando a regra estrita do LIFO
    public String[] obterHistoricoParaInterface() {
        int tam = historicoLogins.size();
        String[] logs = new String[tam];

        // 1. Criamos uma pilha temporária para não perder os dados
        Pilha pilhaAuxiliar = new Pilha();

        // 2. Retiramos do topo (LIFO) um a um, guardamos no array e na auxiliar
        for (int i = 0; i < tam; i++) {
            String log = (String) historicoLogins.peekAndPop();
            logs[i] = log; // O Array fica preenchido do mais recente para o mais antigo
            pilhaAuxiliar.push(log);
        }

        // 3. Devolvemos os dados à pilha original (para manter o histórico intacto)
        // Como estamos a tirar do topo da auxiliar, eles voltam na ordem original perfeita!
        while (!pilhaAuxiliar.isEmpty()) {
            historicoLogins.push(pilhaAuxiliar.peekAndPop());
        }

        return logs;
    }
}