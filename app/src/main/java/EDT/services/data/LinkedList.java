package EDT.services.data;

import java.util.Collection;
import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {
    protected ListNode<T> head, tail;
    protected int length;

    public LinkedList() {
        head = tail = null;
        length = 0;
    }

    @SafeVarargs
    public LinkedList(T ...elements) {
        this();

        for (T element : elements) {
            insert(element);
        }
    }

    public LinkedList<T> filter(ILinkedIFilter<T> filter) {
        LinkedList<T> filtered = new LinkedList<>();

        for (T el: this) {
            if (filter.isValid(el)) {
                filtered.insert(el);
            }
        }

        return filtered;
    }

    public void insert(T value) {
        ListNode<T> newNode = new ListNode<>(value);
        length++;
        if (head == null) {
            head = tail = newNode;
            return;
        }

        tail.setNext(newNode);
        tail = newNode;
    }

    public void add(T value) {
        insert(value);
    }

    public void clear() {
        reset();
    }

    public void reset() {
        head = tail = null;
        length = 0;
    }

    public void addAll(Collection<? extends T> c) {
        clear();
        for (T el: c) {
            insert(el);
        }
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {
            private ListNode<T> currentNode = head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public T next() {
                ListNode<T> tmp = currentNode;
                currentNode = currentNode.getNext();
                return tmp.getValue();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public T find(ILinkedHelper<T> comparator, T searchValue) {
        for (T node : this) {
            if (comparator.compare(node, searchValue)) {
                return node;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder listNodes = new StringBuilder();
        Iterator<T> iterator = this.iterator();
        while (iterator.hasNext()) {
            listNodes.append(iterator.next()).append(iterator.hasNext() ? "->" : "|");
        }

        return "LinkedList{" +
                "head=" + head +
                "nodes=" + listNodes +
                '}';
    }

    public int size() {
        return length;
    }

    public void updateAt(int index, T newValue) {
        ListNode<T> current = getAt(index);
        if (current == null) return;

        current.setValue(newValue);
    }

    public T get(int index) {
        ListNode<T> node = getNode(index);
        if (node == null) return null;

        return node.getValue();
    }

    public void add(int index, T value) {
        ListNode<T> node = getNode(index);
        if (node == null) {
            insert(value);
            return;
        }

        node.setValue(value);
    }

    public boolean contains(Object o) {
        return indexOfObject(o) != -1;
    }

    public int indexOfObject(Object o) {
        int index = 0;
        for (T node: this) {
            if (node.equals(o)) {
                return index;
            }

            index++;
        }

        return -1;
    }

    public int indexOf(T element) {
        int index = 0;

        for (T el: this) {
            if (el.equals(element)) return index;

            index++;
        }

        return -1;
    }

    public void remove(Object o) {
        if (o == null || head == null) return;

        if (head.getValue().equals(o)) { // The target node is the head
            head = head.next;

            if (tail.getValue().equals(o)) {
                tail = head;
            }

            length--;
            return;
        }

        ListNode<T> current = head;
        while (current.next != null && !current.next.getValue().equals(o)) {
            current = current.next;
        }

        if (current.next == null || !current.next.getValue().equals(o)) {
            return;
        }

        length--;
        current.next = current.next.next;

        if (current.next == null) { // The deleted node, was the last one in the list
            tail = current;
        }
    }

    private ListNode<T> getNode(int index) {
        if (index >= length) return null;

        ListNode<T> current = head;
        int i = 0;
        while (i < index) {
            i++;
            current = current.next;
        }

        return current;
    }


    public ListNode<T> getAt(int index) {
        if (index >= length) return null;

        ListNode<T> current = head;
        int i = 0;
        while (i < index) {
            i++;
            current = current.next;
        }

        return current;
    }

    public T getAt2(int index) {
        if (index >= length) return null;

        ListNode<T> current = head;
        int i = 0;
        while (i < index) {
            i++;
            current = current.next;
        }

        return current.getValue();
    }

}
