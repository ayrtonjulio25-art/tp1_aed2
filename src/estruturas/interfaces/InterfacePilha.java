package estruturas.interfaces;

// InterfacePilha.java (Ficha 3)
public interface InterfacePilha {
    public void push(Object element);
    public void pop();
    public Object peek();
    public Object peekAndPop();
    public boolean isEmpty();
    public int size();
}