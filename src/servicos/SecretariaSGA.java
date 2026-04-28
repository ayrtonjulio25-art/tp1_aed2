package servicos;

import estruturas.lineares.Fila;
import estruturas.lineares.ListaDuplamenteLigada;
import estruturas.prioridade.FilaPrioridade;
import modelo.Aluno;



public class SecretariaSGA {

    // Fila FIFO: garante que o primeiro aluno a chegar é o primeiro a ser atendido
    private Fila filaEsperaAtendimento;

    // Max-Heap: garante que o aluno com maior média académica é aprovado primeiro
    private FilaPrioridade filaBolsasEstudo;

    // Lista duplamente ligada: registo mestre de todos os alunos matriculados
    private ListaDuplamenteLigada alunosMatriculados;

    // Contador que gera IDs únicos: começa em 2026000, o 1º aluno fica com 2026001
    private int contadorIds = 2026000;


    // Construtor: inicializa as três estruturas de dados ao criar a secretaria
    public SecretariaSGA() {
        this.filaEsperaAtendimento = new Fila();
        this.filaBolsasEstudo = new FilaPrioridade(50); // heap com capacidade inicial de 50
        this.alunosMatriculados = new ListaDuplamenteLigada();
    }


    // =====================================================================
    // OPERAÇÃO 1: INSERÇÃO
    // =====================================================================




    // Cria um novo aluno com ID automático e adiciona ao fim da lista — O(1)
    public Aluno matricularAluno(String nome) {
        this.contadorIds++;  // incrementa o contador para gerar um ID único
        Aluno novoAluno = new Aluno(this.contadorIds, nome, 0.0); // média começa em 0
        alunosMatriculados.adicionaFim(novoAluno); // insere no fim da lista ligada
        return novoAluno; // retorna o aluno criado para a interface usar
    }

    public boolean adicionarParaAtendimento(String nome) {
        Aluno aluno = pesquisarAlunoPorNome(nome);
        if (aluno == null) return false;
        filaEsperaAtendimento.enqueue(aluno);
        return true;
    }

    public boolean submeterCandidaturaBolsa(String nome, double mediaAcademica) {
        Aluno alunoRegistado = pesquisarAlunoPorNome(nome);
        if (alunoRegistado == null) return false;
        Aluno candidato = new Aluno(alunoRegistado.getNumero(), alunoRegistado.getNome(), mediaAcademica);
        filaBolsasEstudo.inserir(candidato);
        return true;
    }


    // =====================================================================
    // OPERAÇÃO 2: REMOÇÃO
    // =====================================================================


    // Remove e retorna o primeiro aluno da fila (quem chegou há mais tempo) — O(1)
    public Aluno proximoAAtender() {
        return (Aluno) filaEsperaAtendimento.peekAndDequeue();
    }

    // Remove e retorna o aluno com maior média académica do heap — O(log N)
    public Aluno aprovarProximaBolsa() {
        return (Aluno) filaBolsasEstudo.extrairMaximo();
    }


    // =====================================================================
    // OPERAÇÃO 3: PESQUISA
    // =====================================================================

    // Percorre a lista à procura do aluno com o ID fornecido — O(N) no pior caso
    public Aluno pesquisarAlunoPorId(int id) {
        for (int i = 0; i < alunosMatriculados.tamanho(); i++) {
            Aluno a = (Aluno) alunosMatriculados.pega(i);
            if (a != null && a.getNumero() == id) {
                return a; // encontrou: retorna imediatamente (não continua a percorrer)
            }
        }
        return null; // chegou ao fim sem encontrar: retorna null
    }

    public Aluno pesquisarAlunoPorNome(String nome) {
        if (nome == null) return null;
        String alvo = nome.trim();
        if (alvo.isEmpty()) return null;
        for (int i = 0; i < alunosMatriculados.tamanho(); i++) {
            Aluno a = (Aluno) alunosMatriculados.pega(i);
            if (a != null && a.getNome().equalsIgnoreCase(alvo)) {
                return a;
            }
        }
        return null;
    }

    // 4. OPERAÇÃO OBRIGATÓRIA: ORDENAÇÃO (Bubble Sort Manual)
    public Aluno[] obterMatriculadosOrdenadosPorNome() {
        Aluno[] alunos = obterMatriculados(); // copia para array — NÃO altera a lista original

        boolean trocou; // flag de optimização: detecta se a lista já está ordenada

        // Loop externo: cada passagem coloca o maior elemento ainda fora de ordem no fim
        for (int i = 0; i < alunos.length - 1; i++) {
            trocou = false;

            // Loop interno: compara pares adjacentes e troca se estiverem fora de ordem
            for (int j = 0; j < alunos.length - i - 1; j++) {
                // compareToIgnoreCase: retorna positivo se alunos[j] vem depois de alunos[j+1]
                if (alunos[j].getNome().compareToIgnoreCase(alunos[j + 1].getNome()) > 0) {
                    // Troca os dois alunos de posição usando variável temporária
                    Aluno temp = alunos[j];
                    alunos[j] = alunos[j + 1];
                    alunos[j + 1] = temp;
                    trocou = true; // houve troca — a lista ainda não estava ordenada
                }
            }

            // Optimização: se uma passagem completa não fez nenhuma troca, já está ordenado
            if (!trocou) break;
        }
        return alunos; // retorna o array ordenado (a lista original permanece inalterada)
    }


    // =====================================================================
    // OPERAÇÃO 5: RELATÓRIO DE DESEMPENHO
    // =====================================================================


    // Gera um relatório com métricas reais do sistema em tempo de execução
    public String gerarRelatorioDesempenho() {
        // Mede o tempo de uma pesquisa no pior caso (ID -1 nunca existe → percorre tudo)
        long tempoInicio = System.nanoTime();
        pesquisarAlunoPorId(-1); // força a varredura completa da lista
        long tempoFim = System.nanoTime();

        // Monta a string do relatório com os dados actuais das três estruturas
        return "======== RELATÓRIO DE DESEMPENHO SGA ========\n\n" +
                "1. Ocupação Atual das Estruturas:\n" +
                // tamanho() é O(1) — o contador é mantido pela própria lista
                "   - Lista Duplamente Ligada (Matrículas): " + alunosMatriculados.tamanho() + " elementos\n" +
                "   - Fila FIFO (Atendimentos): " + filaEsperaAtendimento.size() + " elementos\n" +
                // getElementos() retorna só os elementos activos (não a capacidade total do array)
                "   - Heap Binário (Bolsas): " + filaBolsasEstudo.getElementos().length + " elementos\n\n" +
                "2. Análise de Complexidade de Tempo (Big-O):\n" +
                "   - Inserção na Fila: O(1)\n" +
                "   - Inserção no Heap (Bolsas): O(log N)\n" +
                "   - Pesquisa na Lista Ligada: O(N)\n" +
                "   - Ordenação (Bubble Sort): O(N²)\n\n" +
                "3. Métrica de Execução do Sistema:\n" +
                // subtracção dos dois timestamps dá o tempo real gasto em nanosegundos
                "   - Tempo para varredura completa da Lista (Pior caso): " + (tempoFim - tempoInicio) + " ns\n\n" +
                "=============================================";
    }


    // =====================================================================
    // MÉTODOS AUXILIARES PARA A INTERFACE GRÁFICA
    // =====================================================================

    // Converte a lista ligada para array — necessário para popular a JTable da interface
    public Aluno[] obterMatriculados() {
        Aluno[] alunos = new Aluno[alunosMatriculados.tamanho()]; // array do tamanho exacto
        for (int i = 0; i < alunosMatriculados.tamanho(); i++) {
            alunos[i] = (Aluno) alunosMatriculados.pega(i); // cast seguro: a lista só tem Alunos
        }
        return alunos;
    }



    // Converte a fila de espera para array sem remover os elementos — só para visualização
    public Aluno[] obterFilaEspera() {
        Aluno[] alunos = new Aluno[filaEsperaAtendimento.size()];
        for (int i = 0; i < filaEsperaAtendimento.size(); i++) {
            alunos[i] = (Aluno) filaEsperaAtendimento.pega(i); // pega() não remove — só lê
        }
        return alunos;
    }

    // Converte os elementos activos do heap para array de Alunos — só para visualização
    public Aluno[] obterBolsasPrioridade() {
        Comparable[] elementos = filaBolsasEstudo.getElementos(); // cópia dos elementos activos
        Aluno[] alunos = new Aluno[elementos.length];
        for (int i = 0; i < elementos.length; i++) {
            alunos[i] = (Aluno) elementos[i]; // cast de Comparable para Aluno
        }
        return alunos;
    }
}

