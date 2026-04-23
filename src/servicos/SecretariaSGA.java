package servicos;

import estruturas.lineares.Fila;
import estruturas.lineares.ListaDuplamenteLigada;
import estruturas.prioridade.FilaPrioridade;
import modelo.Aluno;

public class SecretariaSGA {
    private int contadorIds = 2026000; // Ano 2026 + número sequencial
    private Fila filaEsperaAtendimento;
    private FilaPrioridade filaBolsasEstudo;
    private ListaDuplamenteLigada alunosMatriculados;

    public SecretariaSGA() {
        this.filaEsperaAtendimento = new Fila();
        this.filaBolsasEstudo = new FilaPrioridade(50);
        this.alunosMatriculados = new ListaDuplamenteLigada();
    }

    // 1. OPERAÇÃO OBRIGATÓRIA: INSERÇÃO
    public Aluno matricularAluno(String nome) {
        this.contadorIds++;
        Aluno novoAluno = new Aluno(this.contadorIds, nome, 0.0);
        alunosMatriculados.adicionaFim(novoAluno);
        return novoAluno;
    }

    public boolean adicionarParaAtendimento(Aluno aluno) {
        if (!alunosMatriculados.contem(aluno)) return false;
        filaEsperaAtendimento.enqueue(aluno);
        return true;
    }

    public boolean submeterCandidaturaBolsa(Aluno aluno) {
        if (!alunosMatriculados.contem(aluno)) return false;
        filaBolsasEstudo.inserir(aluno);
        return true;
    }

    // 2. OPERAÇÃO OBRIGATÓRIA: REMOÇÃO
    public Aluno proximoAAtender() {
        return (Aluno) filaEsperaAtendimento.peekAndDequeue();
    }

    public Aluno aprovarProximaBolsa() {
        return (Aluno) filaBolsasEstudo.extrairMaximo();
    }

    // 3. OPERAÇÃO OBRIGATÓRIA: PESQUISA
    public Aluno pesquisarAlunoPorId(int id) {
        for (int i = 0; i < alunosMatriculados.tamanho(); i++) {
            Aluno a = (Aluno) alunosMatriculados.pega(i);
            if (a != null && a.getNumero() == id) {
                return a; // Encontrou e retorna o aluno
            }
        }
        return null; // Não encontrou
    }

    // 4. OPERAÇÃO OBRIGATÓRIA: ORDENAÇÃO (Bubble Sort Manual)
    public Aluno[] obterMatriculadosOrdenadosPorNome() {
        Aluno[] alunos = obterMatriculados();
        boolean trocou;
        for (int i = 0; i < alunos.length - 1; i++) {
            trocou = false;
            for (int j = 0; j < alunos.length - i - 1; j++) {
                if (alunos[j].getNome().compareToIgnoreCase(alunos[j + 1].getNome()) > 0) {
                    Aluno temp = alunos[j];
                    alunos[j] = alunos[j + 1];
                    alunos[j + 1] = temp;
                    trocou = true;
                }
            }
            if (!trocou) break; // Otimização: se não houve troca, já está ordenado
        }
        return alunos;
    }

    // 5. OPERAÇÃO OBRIGATÓRIA: RELATÓRIO DE DESEMPENHO
    public String gerarRelatorioDesempenho() {
        long tempoInicio = System.nanoTime();
        pesquisarAlunoPorId(-1); // Simula pior caso de pesquisa
        long tempoFim = System.nanoTime();

        return "======== RELATÓRIO DE DESEMPENHO SGA ========\n\n" +
                "1. Ocupação Atual das Estruturas:\n" +
                "   - Lista Duplamente Ligada (Matrículas): " + alunosMatriculados.tamanho() + " elementos\n" +
                "   - Fila FIFO (Atendimentos): " + filaEsperaAtendimento.size() + " elementos\n" +
                "   - Heap Binário (Bolsas): " + filaBolsasEstudo.getElementos().length + " elementos\n\n" +
                "2. Análise de Complexidade de Tempo (Big-O):\n" +
                "   - Inserção na Fila: O(1)\n" +
                "   - Inserção no Heap (Bolsas): O(log N)\n" +
                "   - Pesquisa na Lista Ligada: O(N)\n" +
                "   - Ordenação (Bubble Sort): O(N²)\n\n" +
                "3. Métrica de Execução do Sistema:\n" +
                "   - Tempo para varredura completa da Lista (Pior caso): " + (tempoFim - tempoInicio) + " ns\n\n" +
                "=============================================";
    }

    // --- MÉTODOS AUXILIARES PARA A INTERFACE GRÁFICA ---
    public Aluno[] obterMatriculados() {
        Aluno[] alunos = new Aluno[alunosMatriculados.tamanho()];
        for(int i = 0; i < alunosMatriculados.tamanho(); i++) alunos[i] = (Aluno) alunosMatriculados.pega(i);
        return alunos;
    }

    public Aluno[] obterFilaEspera() {
        Aluno[] alunos = new Aluno[filaEsperaAtendimento.size()];
        for(int i = 0; i < filaEsperaAtendimento.size(); i++) alunos[i] = (Aluno) filaEsperaAtendimento.pega(i);
        return alunos;
    }

    public Aluno[] obterBolsasPrioridade() {
        Comparable[] elementos = filaBolsasEstudo.getElementos();
        Aluno[] alunos = new Aluno[elementos.length];
        for(int i = 0; i < elementos.length; i++) alunos[i] = (Aluno) elementos[i];
        return alunos;
    }
}