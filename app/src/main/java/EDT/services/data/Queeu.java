package EDT.services.data;

public class Queeu<T> extends LinkedList<T> {
    public Queeu() {
        super();
    }

    public T poll() {
        if (head == null) return null;

        length--;
        ListNode<T> tmp = head;
        head = head.next;

        return tmp.getValue();
    }

    public void add(T node) {
        insert(node);
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public int size() {
        return length;
    }
}
