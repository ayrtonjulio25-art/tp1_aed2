package servicos;


import estruturas.dispersao.TabelaHashUtilizadores;
import estruturas.lineares.Fila;
import estruturas.lineares.ListaDuplamenteLigada;
import estruturas.lineares.Pilha;
import estruturas.prioridade.FilaPrioridade;
import modelo.Aluno;
import modelo.Utilizador;

public class AutenticacaoSGA {
    private TabelaHashUtilizadores utilizadores;
    private Pilha historicoLogins;

    public AutenticacaoSGA() {
        this.utilizadores = new TabelaHashUtilizadores();
        this.historicoLogins = new Pilha();
        // Seed de dados
        this.utilizadores.adiciona(new Utilizador("admin", "senha123"));
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
        // Lógica de iteração da pilha aqui
    }
}
