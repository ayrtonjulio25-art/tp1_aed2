package estruturas.interfaces;

// InterfaceGeral.java (Ficha 2)
public interface InterfaceGeral {
    public void adicionaInicio(Object elemento);
    public void adicionaPosicao(int posicao, Object elemento);
    public void adicionaFim(Object elemento);
    public Object pega(int posicao);
    public void removeInicio();
    public void removePosicao(int posicao);
    public void removeFim();
    public boolean contem(Object elemento);
    public int tamanho();
}