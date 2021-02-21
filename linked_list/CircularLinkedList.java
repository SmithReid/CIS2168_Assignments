import java.util.Iterator;
import java.lang.IndexOutOfBoundsException;
import java.util.Arrays;
import java.util.List;

public class CircularLinkedList<E> implements Iterable<E> {
    // Your variables
    Node<E> head;
    Node<E> tail;
    int size;  // BE SURE TO KEEP TRACK OF THE SIZE

    // implement this constructor
    
    public CircularLinkedList() {
        size = 0;
    }

    // I highly recommend using this helper method
    // Return Node<E> found at the specified index
    // be sure to handle out of bounds cases
    private Node<E> getNode(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        Node<E> output = head;
        for (int i = 0; i < index; i++) {
            output = output.next;
        }
        return output;
    }


    // attach a node to the end of the list
    public boolean add(E item) {
        this.add(size,item);
        return true;
    }

    // Cases to handle
    // out of bounds
    // adding to empty list
    // adding to front
    // adding to "end"
    // adding anywhere else
    // REMEMBER TO INCREMENT THE SIZE
    public void add(int index, E item){
        if (index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        Node<E> inserting = new Node(item);
        if (size == 0) {
            head = inserting;
        } else if (size == 1) {
            tail = inserting;
            head.next = tail;
        } else {
            Node<E> prev;
            Node<E> next;
            if (index == 0) {
                prev = tail;
                next = head;
                head = inserting;
            } else if (index == size) {
                tail = inserting;
                prev = getNode(index - 1);
                next = head;
            } else {
                prev = getNode(index - 1);
                next = getNode(index);
            }
            inserting.next = next;
            prev.next = inserting;
        }
        size++;
    }
    
    // remove must handle the following cases
    // out of bounds
    // removing the only thing in the list
    // removing the first thing in the list (need to adjust the last thing in the list to point to the beginning)
    // removing the last thing 
    // removing any other node
    // REMEMBER TO DECREMENT THE SIZE
    public E remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        E output = this.getNode(index).item;
        if (size == 2) {
            if (index == 0) head = tail;
            tail = null;
            size--;
            return output;
        }
        if (size == 1) {
            head = null;
            size--;
            return output;
        }
        Node<E> prev;
        Node<E> next;
        if (index == 0) {
            prev = tail;
            next = head;
            head = head.next;
        } else if (index == size - 1) {
            prev = getNode(index - 1);
            next = head;
            tail = prev;
        } else {
            prev = getNode(index - 1);
            next = getNode(index + 1);
        }
        prev.next = next;
        size--;
        return output;
    }
    
    public int getIndexOfItem(E item) {
        Node<E> node = head;
        int index = 0;
        while (!node.item.equals(item)) {
            node = node.next;
            index++;
        }
        if (node.item.equals(item)) return index;
        else return -1; // we didn't find it
    }

    public int removeReturnIndex(E item) {
        Node<E> node = head;
        int index = 0;
        while (!node.item.equals(item)) {
            node = node.next;
            index++;
        }
        // much copy-pasta from this.remove(int index) {...} ...
        if (node.item.equals(item)) {
            Node<E> prev;
            Node<E> next;
            if (index == 0) {
                prev = tail;
                next = head;
                head = head.next;
            } else if (index == size - 1) {
                prev = getNode(index - 1);
                next = head;
                tail = prev;
            } else {
                prev = getNode(index - 1);
                next = getNode(index + 1);
            }
            prev.next = next;
            size--;
            return index;
        } else return -1;
    }

    public E read(int index) {
        return this.getNode(index).item;
    }
    
    // Turns your list into a string
    // Useful for debugging
    public String toString(){
        Node<E> current =  head;
        StringBuilder result = new StringBuilder();
        if(size == 0){
            return "";
        }
        if(size == 1) {
            return head.item.toString();
            
        }
        else{
            do{
                result.append(current.item);
                result.append(" ==> ");
                current = current.next;
            } while(current != head);
        }
        return result.toString();
    }
    
    
    public Iterator<E> iterator() {
        return new ListIterator<E>();
    }
    
    // provided code for different assignment
    // you should not have to change this
    // change at your own risk!
    // this class is not static because it needs the class it's inside of to survive!
    private class ListIterator<E> implements Iterator<E>{
        
        Node<E> nextItem;
        Node<E> prev;
        int index;
        
        @SuppressWarnings("unchecked")
        //Creates a new iterator that starts at the head of the list
        public ListIterator(){
            nextItem = (Node<E>) head;
            index = 0;
        }

        // returns true if there is a next node
        // this is always should return true if the list has something in it
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return size != 0;
        }
        
        // advances the iterator to the next item
        // handles wrapping around back to the head automatically for you
        public E next() {
            // TODO Auto-generated method stub
            prev =  nextItem;
            nextItem = nextItem.next;
            index =  (index + 1) % size;
            return prev.item;
    
        }
        
        // removed the last node was visted by the .next() call 
        // for example if we had just created a iterator
        // the following calls would remove the item at index 1 (the second person in the ring)
        // next() next() remove()
        public void remove() {
            int target;
            if(nextItem == head) {
                target = size - 1;
            } else{ 
                target = index - 1;
                index--;
            }
            CircularLinkedList.this.remove(target); //calls the above class
        }
        
    }
    
    // It's easiest if you keep it a singly linked list
    // SO DON'T CHANGE IT UNLESS YOU WANT TO MAKE IT HARDER
    private static class Node<E>{
        E item;
        Node<E> next;
        
        public Node(E item) {
            this.item = item;
        }
        
    }
    
    public static void main(String[] args){
        CircularLinkedList cll = new CircularLinkedList();
        int[] intsTest = new int[] {0, 1, 2, 3, 4, 5, 6};
        for (int value : intsTest) {
            cll.add(value);
        }
        System.out.println(cll); 
        cll.remove(3);
        System.out.println(cll);  
    }   
}
