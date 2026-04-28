package servicos;

// Aqui estou na camada de serviços, responsável pela lógica do sistema,
// ou seja, regras de negócio como autenticação e controlo de acesso.

import estruturas.dispersao.TabelaHashUtilizadores;
import estruturas.lineares.Pilha;
import modelo.Utilizador;

// Esta classe é responsável por gerir o processo de autenticação do sistema,
// incluindo login, cadastro de utilizadores e histórico de acessos.
public class AutenticacaoSGA {

    // Estrutura de dados usada para armazenar utilizadores
    // Utilizo uma Tabela Hash para garantir buscas rápidas (O(1) em média)
    private TabelaHashUtilizadores utilizadores;

    // Pilha usada para armazenar o histórico de logins
    // Segue a regra LIFO (Last In, First Out)
    private Pilha historicoLogins;

    // Construtor da classe
    public AutenticacaoSGA() {

        // Inicializo a tabela hash de utilizadores
        this.utilizadores = new TabelaHashUtilizadores();

        // Inicializo a pilha de histórico
        this.historicoLogins = new Pilha();

        // Seed de dados: utilizadores criados automaticamente no arranque do sistema
        // Isso facilita testes e demonstração
        this.utilizadores.adiciona(new Utilizador("admin", "senha123"));
        this.utilizadores.adiciona(new Utilizador("secretaria", "senha123"));
        this.utilizadores.adiciona(new Utilizador("anaestudante", "senha123"));
        this.utilizadores.adiciona(new Utilizador("anaestudante1", "senha123"));

        // Registo inicial no histórico
        this.historicoLogins.push("SISTEMA: Utilizadores iniciais carregados.");
    }

    // Método responsável por cadastrar novos utilizadores no sistema
    public void cadastrarUsuario(String username, String password) {

        // Validação simples para evitar usernames vazios ou nulos
        if (username != null && !username.trim().isEmpty()) {

            // Adiciono o novo utilizador na tabela hash
            this.utilizadores.adiciona(new Utilizador(username, password));

            // Registo o evento no histórico
            this.historicoLogins.push("SISTEMA: Novo utilizador criado - " + username);
        }
    }

    // Método de login do sistema
    public boolean login(String username, String password) {

        // Procuro o utilizador na tabela hash (busca eficiente)
        Utilizador u = utilizadores.busca(username);

        // Verifico se o utilizador existe e se a password corresponde
        if (u != null && u.getPassword().equals(password)) {

            // Registo sucesso no histórico
            historicoLogins.push("LOGIN SUCESSO: " + username);

            return true;
        }

        // Caso falhe, também registo no histórico
        historicoLogins.push("LOGIN FALHADO: " + username);

        return false;
    }

    // Método para imprimir o histórico no terminal (debug ou teste)
    public void imprimirHistorico() {
        System.out.println("--- Histórico de Acessos ---");

        // Obtenho os dados preparados para visualização
        String[] logs = obterHistoricoParaInterface();

        // Imprimo cada registo
        for (String log : logs) {
            System.out.println(log);
        }
    }

    // Método importante: devolve o histórico sem destruir a pilha
    // Respeitando a regra LIFO (último a entrar, primeiro a sair)
    public String[] obterHistoricoParaInterface() {

        int tam = historicoLogins.size();

        // Crio um array para devolver os logs
        String[] logs = new String[tam];

        // 1. Crio uma pilha auxiliar
        // Isto é necessário para não perder os dados originais
        Pilha pilhaAuxiliar = new Pilha();

        // 2. Retiro elemento por elemento do topo da pilha original
        // e guardo no array e na pilha auxiliar
        for (int i = 0; i < tam; i++) {

            // peekAndPop retira o topo (LIFO)
            String log = (String) historicoLogins.peekAndPop();

            // O array fica ordenado do mais recente para o mais antigo
            logs[i] = log;

            // Guardo na pilha auxiliar
            pilhaAuxiliar.push(log);
        }

        // 3. Reconstituo a pilha original
        // Isto garante que o histórico não é perdido
        while (!pilhaAuxiliar.isEmpty()) {

            // Ao retirar da auxiliar, a ordem volta ao normal
            historicoLogins.push(pilhaAuxiliar.peekAndPop());
        }

        // Retorno o histórico pronto para uso na interface
        return logs;
    }
}