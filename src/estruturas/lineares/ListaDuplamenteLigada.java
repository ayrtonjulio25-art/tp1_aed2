package estruturas.lineares;

import estruturas.interfaces.InterfaceGeral;

public class ListaDuplamenteLigada implements InterfaceGeral {
    private No inicio;
    private No ultimo;
    private int totalDeElementos;

    public void adicionaInicio(Object elemento) {
        if (this.totalDeElementos == 0) {
            No novo = new No(null, elemento, null);
            this.inicio = novo;
            this.ultimo = novo;
        } else {
            No novo = new No(null, elemento, this.inicio);
            this.inicio.setAnterior(novo);
            this.inicio = novo;
        }
        this.totalDeElementos++;
    }

    public void adicionaFim(Object elemento) {
        if (this.totalDeElementos == 0) {
            adicionaInicio(elemento);
        } else {
            No novo = new No(this.ultimo, elemento, null);
            this.ultimo.setProximo(novo);
            this.ultimo = novo;
            this.totalDeElementos++;
        }
    }

    public void removeInicio() {
        if (this.totalDeElementos == 0) return;
        this.inicio = this.inicio.getProximo();
        if (this.inicio != null) {
            this.inicio.setAnterior(null);
        } else {
            this.ultimo = null;
        }
        this.totalDeElementos--;
    }

    public void removeFim() {
        if (this.totalDeElementos == 0) return;
        if (this.totalDeElementos == 1) {
            this.removeInicio();
        } else {
            this.ultimo = this.ultimo.getAnterior();
            this.ultimo.setProximo(null);
            this.totalDeElementos--;
        }
    }

    public Object pega(int posicao) {
        if (posicao < 0 || posicao >= totalDeElementos) return null;
        No atual = inicio;
        for (int i = 0; i < posicao; i++) {
            atual = atual.getProximo();
        }
        return atual.getElemento();
    }

    public int tamanho() { return this.totalDeElementos; }

    // Implementações simplificadas para satisfazer a interface
    public void adicionaPosicao(int posicao, Object elemento) { /* Lógica de travessia e inserção */ }
    public void removePosicao(int posicao) { /* Lógica de travessia e remoção */ }
    public boolean contem(Object elemento) {
        No atual = inicio;
        while(atual != null) {
            if(atual.getElemento().equals(elemento)) return true;
            atual = atual.getProximo();
        }
        return false;
    }
}
