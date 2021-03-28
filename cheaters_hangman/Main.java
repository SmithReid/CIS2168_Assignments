import java.util.Scanner;
import java.io.File;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.io.FileNotFoundException;


public class Main {
    public static HashMap<Integer, HashSet<String>> loadDict() throws FileNotFoundException {
        HashMap<Integer, HashSet<String>> dict = new HashMap<>();
        Scanner words = new Scanner(new File("text_files/words.txt"));
        while (words.hasNextLine()) {
            String word = words.nextLine();
            if (dict.get(word.length()) == null) 
                dict.put(word.length(), new HashSet<String>());
            dict.get(word.length()).add(word);
        }
        return dict;
    }

    public static void run(boolean debug, 
                            HashMap<Integer, HashSet<String>> dict, 
                            int length, int nGuesses) {
        HashSet<String> nDict = dict.get(length);
        
    }

    public static void main(String[] args) throws FileNotFoundException {
        HashMap<Integer, HashSet<String>> dict = loadDict();

        if (args.length > 0) {
            if (args[0].toLowerCase().equals("debug")) {
                System.out.println("Debug mode: ");
                if (args.length == 3)
                    run(true, dict, Integer.valueOf(args[1]), Integer.valueOf(args[2]));
                else {
                    Scanner userIn = new Scanner(System.in);

                    System.out.println("How long would you like the word to be?");
                    int length = userIn.nextInt();
                    System.out.println("How many guesses would you like?");
                    int nGuesses = userIn.nextInt();

                    run(true, dict, length, nGuesses);
                }
            }
        } else {
            Scanner userIn = new Scanner(System.in);

            System.out.println("How long would you like the word to be?");
            int length = userIn.nextInt();
            System.out.println("How many guesses would you like?");
            int nGuesses = userIn.nextInt();

            run(false, dict, length, nGuesses);
        }
    }
}