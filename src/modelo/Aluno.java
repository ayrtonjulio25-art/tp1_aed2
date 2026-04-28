package modelo;

public class Aluno implements Comparable<Aluno> {
    private int numero;
    private String nome;
    private double mediaAcademica; // Usado para prioridade em vagas/bolsas

    public Aluno(int numero, String nome, double mediaAcademica) {
        this.numero = numero;
        this.nome = nome;
        this.mediaAcademica = mediaAcademica;
    }

    public int getNumero() { return numero; }
    public String getNome() { return nome; }
    public double getMediaAcademica() { return mediaAcademica; }


    @Override
    public int compareTo(Aluno outro) {
        // Ordenação: maior média tem maior prioridade no Heap
        return Double.compare(this.mediaAcademica, outro.mediaAcademica);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // É a mesma referência na memória
        if (obj == null || getClass() != obj.getClass()) return false; // Tipos diferentes

        Aluno outroAluno = (Aluno) obj;

        // Verifica se o número e o nome correspondem exatamente (ignorando maiúsculas/minúsculas)
        return this.numero == outroAluno.numero &&
                this.nome.equalsIgnoreCase(outroAluno.nome);
    }

    @Override
    public String toString() {
        return nome + " (" + numero + ") - Média: " + mediaAcademica;
    }
}
