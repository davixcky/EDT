package EDT.services.data;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<ListNode<T>> {
    protected ListNode<T> head, tail;
    protected int length;

    public LinkedList() {
        head = tail = null;
        length = 0;
    }

    public LinkedList<T> filter(ILinkedIFilter<T> filter) {
        LinkedList<T> filtered = new LinkedList<>();

        for (ListNode<T> el: this) {
            if (filter.isValid(el.getValue())) {
                filtered.insert(el.getValue());
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

    public void reset() {
        head = tail = null;
        length = 0;
    }

    @Override
    public Iterator<ListNode<T>> iterator() {

        return new Iterator<ListNode<T>>() {
            private ListNode<T> currentNode = head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public ListNode<T> next() {
                ListNode<T> tmp = currentNode;
                currentNode = currentNode.getNext();
                return tmp;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public T find(ILinkedHelper<T> comparator, T searchValue) {
        for (ListNode<T> node : this) {
            T nodeValue = node.getValue();
            if (comparator.compare(nodeValue, searchValue)) {
                return nodeValue;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder listNodes = new StringBuilder();
        for (ListNode<T> node : this) { //if next element exists
            listNodes.append(node).append(node.next != null ? "->" : "|");
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

    public int indexOf(T element) {
        int index = 0;

        for (ListNode<T> el: this) {
            if (el.getValue().equals(element)) return index;

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

        int i = 0;
        for (ListNode<T> el: this) {
            if (i == index) return el;

            i++;
        }

        return null;
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
