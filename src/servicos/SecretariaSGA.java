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

    public boolean adicionarParaAtendimento(Aluno aluno) {
        // Usa o contem(). Se o ID ou o Nome forem diferentes da matrícula, ele barra!
        if (!alunosMatriculados.contem(aluno)) {
            return false;
        }
        filaEsperaAtendimento.enqueue(aluno);
        return true;
    }

    public boolean submeterCandidaturaBolsa(Aluno aluno) {
        // Usa o contem(). Se o ID ou o Nome forem diferentes da matrícula, ele barra!
        if (!alunosMatriculados.contem(aluno)) {
            return false;
        }
        filaBolsasEstudo.inserir(aluno);
        return true;
    }

    public Aluno matricularAluno(String nome) {
        this.contadorIds++; // Incrementa o ID automaticamente
        Aluno novoAluno = new Aluno(this.contadorIds, nome, 0.0);

        // Adiciona à Lista Duplamente Ligada
        alunosMatriculados.adicionaFim(novoAluno);

        // Retorna o aluno criado para a GUI saber qual foi o ID gerado
        return novoAluno;
    }

    public Aluno proximoAAtender() {
        return (Aluno) filaEsperaAtendimento.peekAndDequeue();
    }

    public Aluno aprovarProximaBolsa() {
        return (Aluno) filaBolsasEstudo.extrairMaximo();
    }

    public Aluno[] obterMatriculados() {
        Aluno[] alunos = new Aluno[alunosMatriculados.tamanho()];
        for(int i = 0; i < alunosMatriculados.tamanho(); i++) {
            alunos[i] = (Aluno) alunosMatriculados.pega(i);
        }
        return alunos;
    }

    public Aluno[] obterFilaEspera() {
        Aluno[] alunos = new Aluno[filaEsperaAtendimento.size()];
        for(int i = 0; i < filaEsperaAtendimento.size(); i++) {
            alunos[i] = (Aluno) filaEsperaAtendimento.pega(i);
        }
        return alunos;
    }

    public Aluno[] obterBolsasPrioridade() {
        Comparable[] elementos = filaBolsasEstudo.getElementos();
        Aluno[] alunos = new Aluno[elementos.length];
        for(int i = 0; i < elementos.length; i++) {
            alunos[i] = (Aluno) elementos[i];
        }
        return alunos;
    }
}