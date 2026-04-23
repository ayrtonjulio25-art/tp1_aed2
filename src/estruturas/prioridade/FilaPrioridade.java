package estruturas.prioridade;


// Implementação de Max-Heap usando um array dinâmico construído do zero
public class FilaPrioridade {
    private Comparable[] heap;
    private int tamanho;
    private int capacidade;

    public FilaPrioridade(int capacidadeInicial) {
        this.capacidade = capacidadeInicial;
        this.heap = new Comparable[capacidade];
        this.tamanho = 0;
    }

    public void inserir(Comparable elemento) {
        if (tamanho == capacidade) expandirCapacidade();
        heap[tamanho] = elemento;
        subir(tamanho);
        tamanho++;
    }

    public Comparable extrairMaximo() {
        if (tamanho == 0) return null;
        Comparable max = heap[0];
        heap[0] = heap[tamanho - 1];
        tamanho--;
        descer(0);
        return max;
    }

    private void subir(int indice) {
        int pai = (indice - 1) / 2;
        while (indice > 0 && heap[indice].compareTo(heap[pai]) > 0) {
            trocar(indice, pai);
            indice = pai;
            pai = (indice - 1) / 2;
        }
    }

    private void descer(int indice) {
        int maior = indice;
        int esq = 2 * indice + 1;
        int dir = 2 * indice + 2;

        if (esq < tamanho && heap[esq].compareTo(heap[maior]) > 0) maior = esq;
        if (dir < tamanho && heap[dir].compareTo(heap[maior]) > 0) maior = dir;

        if (maior != indice) {
            trocar(indice, maior);
            descer(maior);
        }
    }

    private void trocar(int i, int j) {
        Comparable temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void expandirCapacidade() {
        capacidade *= 2;
        Comparable[] novoHeap = new Comparable[capacidade];
        System.arraycopy(heap, 0, novoHeap, 0, tamanho);
        heap = novoHeap;
    }

    public Comparable[] getElementos() {
        Comparable[] copia = new Comparable[tamanho];
        System.arraycopy(heap, 0, copia, 0, tamanho);
        return copia;
    }
}