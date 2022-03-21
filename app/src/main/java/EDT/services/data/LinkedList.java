package EDT.services.data;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<ListNode<T>> {
    ListNode<T> head, tail;
    int length;

    public LinkedList() {
        head = tail = null;
        length = 0;
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

    public void find(ILinkedHelper<T> helper) {
        for (ListNode<T> node : this) {
            helper.handle(node.value);
        }
    }

    @Override
    public String toString() {
        StringBuilder listNodes = new StringBuilder();
        for (ListNode<T> node : this) { //if next element exists
            listNodes.append(node).append(node.next != null ? "->" : "|");
        }

        this.forEach(listNode -> System.out.println(listNode.value + "foreach") );

        return "LinkedList{" +
                "head=" + head +
                "nodes=" + listNodes +
                '}';
    }
}
