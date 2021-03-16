import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;


// Your class. Notice how it has no generics.
// This is because we use generics when we have no idea what kind of data we are getting
// Here we know we are getting two pieces of data:  a string and a line number
public class IndexTree {

    // This is your root 
    // again, your root does not use generics because you know your nodes
    // hold strings, an int, and a list of integers
    private IndexNode root;
    
    // Make your constructor
    // It doesn't need to do anything

    public IndexTree() {
        ;
    }
    
    // complete the methods below
    
    // this is your wrapper method
    // it takes in two pieces of data rather than one
    // call your recursive add method
    public void add(String word, int lineNumber) {
        if (root.word == word) {
            root.add(lineNumber);
            return;
        }
        add(this.root, word, lineNumber);
    }

    // your recursive method for add
    // Think about how this is slightly different the the regular add method
    // When you add the word to the index, if it already exists, 
    // you want to  add it to the IndexNode that already exists
    // otherwise make a new indexNode
    private IndexNode add(IndexNode root, String word, int lineNumber) {
        if (word == root.word) {
            root.add(lineNumber);
            return root;
        }
        if (word.compareTo(root.word) == -1) {
            if (root.left == null) {
                root.left = new IndexNode(word, lineNumber);
                return root.left;
            }
            return root.left.add(lineNumber);
        }
        else {
            if (root.right == null) {
                root.right = new IndexNode(word, lineNumber);
                return root.right;
            }
            return root.right.add(lineNumber);
        }
    }
    
    // returns true if the word is in the index
    public boolean contains(String word) {
        return true;
    }
    
    // call your recursive method
    // use book as guide
    public void delete(String word) {
        
    }
    
    // your recursive case
    // remove the word and all the entries for the word
    // This should be no different than the regular technique.
    private IndexNode delete(IndexNode root, String word) {
        return null;
    }

    // prints all the words in the index in inorder order
    // To successfully print it out
    // this should print out each word followed by the number of occurrences and the list of all occurrences
    // each word and its data gets its own line
    public void printIndex() {
        System.out.println(this.toString());
    }

    public void indexToFile(String filename) throws FileNotFoundException {
        PrintStream out = new PrintStream(new File("test.txt"));
        out.print(this.toString());
        out.close();
    }

    public String toString() {
        return "";
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        IndexTree index = new IndexTree();

        // load the file
        Scanner sc = new Scanner(new File("pg100.txt"));
        
        // add all the words to the tree
        int lineNumber = 0;
        boolean flag = false;
        do {
            String firstLine = sc.nextLine();
            Scanner lineSC = new Scanner(firstLine);
            if (lineSC.hasNext()) {
                index.root = new IndexNode(lineSC.next(), lineNumber);
                while (lineSC.hasNext()) {
                    index.add(lineSC.next(), lineNumber);
                }
            } else 
                flag = false;
                lineNumber++;
        } while (flag);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Scanner lineSC = new Scanner(line);
            while (lineSC.hasNext()) 
                index.add(lineSC.next(), lineNumber);
            lineNumber++;
        }
        
        // print out the index
        index.indexToFile("complete_index.txt");
        
        // test removing a word from the index
        index.delete("thou");
        index.indexToFile("index_minus_thou.txt");

        
    }
}