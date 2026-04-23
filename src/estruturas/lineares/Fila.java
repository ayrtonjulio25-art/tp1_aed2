package estruturas.lineares;

import estruturas.interfaces.InterfaceFila;

public class Fila implements InterfaceFila {
    private ListaDuplamenteLigada lista = new ListaDuplamenteLigada();

    public void enqueue(Object element) { lista.adicionaFim(element); }
    public void dequeue() { lista.removeInicio(); }
    public Object peek() { return lista.pega(0); }
    public Object peekAndDequeue() {
        Object elemento = peek();
        dequeue();
        return elemento;
    }
    public boolean isEmpty() { return lista.tamanho() == 0; }
    public int size() { return lista.tamanho(); }

    public Object pega(int posicao) {
        return lista.pega(posicao);
    }
}
