package estruturas.interfaces;

// InterfaceFila.java (Ficha 3)
public interface InterfaceFila {
    public void enqueue(Object element);
    public void dequeue();
    public Object peek();
    public Object peekAndDequeue();
    public boolean isEmpty();
    public int size();
}