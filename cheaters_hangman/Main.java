import java.util.Scanner;
import java.io.File;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.util.Arrays;


public class Main {
    public static HashMap<Integer, HashSet<String>> loadDict() 
                        throws FileNotFoundException {
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

    public static String findMatch(HashSet<String> nDict, 
                                String userGuess, String userHasGuessed) {
        boolean foundResult = false;
        Iterator iterNDict = nDict.iterator();
        String output = "";
        while (!foundResult) {
            foundResult = true;
            String word = String.valueOf(iterNDict.next());
            for (int i = 0; i < userHasGuessed.length(); i++) {
                if (userHasGuessed.charAt(i) != '_') {
                    if (userHasGuessed.charAt(i) != word.charAt(i)) {
                        foundResult = false;
                    }
                }
            }
            output = word;
        }
        return output;
    }

    public static void run(boolean debug, 
                            HashMap<Integer, HashSet<String>> dict, 
                            int length, int nGuesses) {
        HashSet<String> nDict = dict.get(length);
        Scanner userIn = new Scanner(System.in);
        String userVisible = "";
        String[] userGuessed = new String[26];
        String hiddenWord;
        for (int i = 0; i < length; i++) 
            userVisible += "_";

        while (nGuesses > 0) {
            System.out.println("You have guessed: " + userVisible);
            System.out.println("You have " + nGuesses + " guesses remaining.");
            System.out.println("Your guessed letters are: " + Arrays.toString(userGuessed));
            System.out.println();

            String userGuess;
            boolean loopFlag = false;
            do {
                // if the user enters more than one letter, take the first
                System.out.println("What would you like to guess?");
                userGuess = userIn.next().substring(0, 1); 

                for (int i = 0; i < userGuessed.length; i++) {
                    if (userGuess.equals(userGuessed[i])) {
                        loopFlag = true;
                        System.out.println(
                                "You have entered a repeat guess. Please guess again.");
                    }
                }
            } while (loopFlag);

            for (int i = 0; i < userGuessed.length; i++) {
                if (userGuessed[i] == null) {
                    userGuessed[i] = userGuess;
                    break;
                }
            }




            nGuesses--;
        }
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