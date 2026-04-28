package estruturas.lineares;

import estruturas.interfaces.InterfacePilha;

public class Pilha implements InterfacePilha {
    private ListaDuplamenteLigada lista = new ListaDuplamenteLigada();



    public void push(Object element) { lista.adicionaFim(element); }

    public void pop() { lista.removeFim(); }
    public Object peek() { return lista.pega(lista.tamanho() - 1); }
    public Object peekAndPop() {
        Object elemento = peek();
        pop();
        return elemento;
    }

    public boolean isEmpty() { return lista.tamanho() == 0; }
    public int size() { return lista.tamanho(); }
}