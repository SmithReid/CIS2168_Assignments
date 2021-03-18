import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.StringBuilder;


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
        if (root.word.equals(word)) {
            root.add(lineNumber);
            return root;
        }
        if (word.compareTo(root.word) < 0) {
            if (root.left == null) {
                root.left = new IndexNode(word, lineNumber);
                return root.left;
            }
            return add(root.left, word, lineNumber);
        }
        else {
            if (root.right == null) {
                root.right = new IndexNode(word, lineNumber);
                return root.right;
            }
            return add(root.right, word, lineNumber);
        }
    }
    
    // returns true if the word is in the index
    public boolean contains(String word) {
        word = word.replaceAll("[^a-zA-Z ]", "").toUpperCase();
        if (root.word.equals(word))
            return true;
        return contains(root, word);
    }

    private boolean contains(IndexNode root, String word) {
        if (root.word.equals(word))
            return true;
        if (word.compareTo(root.word) < 0) {
            if (root.left != null)
                return contains(root.left, word);
            else
                return false;
        } else {
            if (root.right != null)
                return contains(root.right, word);
            else return false;
        }
    }
    
    // call your recursive method
    // use book as guide
    public void delete(String word) {
        word = word.replaceAll("[^a-zA-Z ]", "").toUpperCase();
        this.root = delete(this.root, word);
    }
    
    // your recursive case
    // remove the word and all the entries for the word
    // This should be no different than the regular technique.
    private IndexNode delete(IndexNode root, String word) {
        if (root == null)
            return null;
        int comparison = word.compareTo(root.word);
        if (comparison < 0) {
            root.left = delete(root.left, word);
            return root;
        } else if (comparison > 0) {
            root.right = delete(root.right, word);
            return root;
        } else {
            if (root.left == null && root.right == null)
                return null;
            else if (root.left != null && root.right == null)
                return root.left;
            else if (root.left == null && root.right != null)
                return root.right;
            else {
                IndexNode current = root.left;
                while (current.right != null)
                    current = current.right;
                root.word = current.word;
                root.list = current.list;
                root.occurrences = current.occurrences;
                root.left = delete(root.left, root.word);
                return root;
            }
        }
    }

    // prints all the words in the index in inorder order
    // To successfully print it out
    // this should print out each word followed by the number of occurrences and the list of all occurrences
    // each word and its data gets its own line
    public void printIndex() {
        System.out.println(this.toString());
    }

    public void indexToFile(String filename) throws FileNotFoundException {
        PrintStream out = new PrintStream(new File(filename));
        out.print(this.toString());
        out.close();
    }

    public String toString() {
        // https://janac.medium.com/simple-tostring-method-for-binary-search-trees-af6b7171f432
        String output = "Begin index: \n";
        return output += this.toStringInOrder(this.root);
    }

    private String toStringInOrder(IndexNode root) {
        if (root == null)
            return "";

        StringBuilder builder = new StringBuilder();

        builder.append(toStringInOrder(root.left));
        builder.append(root.toString());
        builder.append(toStringInOrder(root.right));

        return builder.toString();
    }

    public void buildIndex(IndexTree index) throws FileNotFoundException {
        // load the file
        Scanner sc = new Scanner(new File("pg100.txt"));
        
        // add all the words to the tree
        int lineNumber = 0;
        boolean flag = false;
        do {
            String firstLine = sc.nextLine().replaceAll("[^a-zA-Z ]", "").toUpperCase();
            Scanner lineSC = new Scanner(firstLine);
            if (lineSC.hasNext()) {
                index.root = new IndexNode(lineSC.next(), lineNumber);
                while (lineSC.hasNext()) {
                    index.add(lineSC.next(), lineNumber);
                }
                lineNumber++;
            } else 
                flag = false;
                lineNumber++;
        } while (flag);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().replaceAll("[^a-zA-Z ]", "").toUpperCase();
            Scanner lineSC = new Scanner(line);
            while (lineSC.hasNext()) {
                index.add(lineSC.next(), lineNumber);
                lineNumber++;
            }
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        // regex courtesy of: https://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java
        IndexTree index = new IndexTree();
        
        index.buildIndex(index);

        System.out.println(index.contains("THOU"));
        System.out.println(index.contains("A"));
        
        // print out the index
        index.indexToFile("complete_index.txt");
        
        // test removing a word from the index
        index.delete("thou");
        index.indexToFile("index_minus_thou.txt");

        // printIndex();
    }
}