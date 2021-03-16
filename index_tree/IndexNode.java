import java.util.List;
import java.util.ArrayList;

public class IndexNode  {
    // The word for this entry
    String word;
    // The number of occurrences for this word
    int occurrences;
    // A list of line numbers for this word.
    List<Integer> list; 
    
    IndexNode left;
    IndexNode right;
    
    // Constructors
    // Constructor should take in a word and a line number
    // it should initialize the list and set occurrences to 1
    
    public IndexNode(String word, int lineNumber) {
        occurrences = 1;
        list = new ArrayList<Integer>();
        list.add(lineNumber);
    }

    public IndexNode add(int lineNumber) {
        list.add(lineNumber);
        occurrences++;
        return this;
    }
  
    // Complete This
    // return the word, the number of occurrences, and the lines it appears on.
    // string must be one line
    
    public String toString() {
        String output = word + " occurs " + occurrences + " times, on line(s): \n";
        for (int i = 0; i < list.size() - 1; i++)  
            output += list.get(i) + ", ";
        output += list.get(list.size() - 1) + ".";
        return output;
    }
}