// Written by Reid Smith with some collaboration with Olivia Chaves


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

    public static String collectUserGuess(String[] userGuessed, int guessN) {
        Scanner userIn = new Scanner(System.in);
        boolean loopFlag;
        String output;
        do {
            loopFlag = false;
            System.out.println("Please enter your guess: ");
            output = userIn.next().substring(0, 1).toLowerCase();
            char outChar = output.charAt(0);
            if (outChar < 'a' || outChar > 'z') {
                System.out.println("Please enter a letter.");
                loopFlag = true;
            }
            for (int i = 0; i <= guessN; i++) {
                if (output.equals(userGuessed[i])) {
                    System.out.println(
                        "You have already guessed that letter. Please guess again.");
                    loopFlag = true;
                }
            }
        } while (loopFlag);

        loopFlag = true;
        userGuessed[guessN] = output;
        return output;
    }

    public static HashSet<String> findMatches(HashSet<String> nDict, 
                                String[] userGuessed, int guessN) {
        HashSet<String> output = new HashSet<>();
        for (String word : nDict) {
            boolean wordFlag = true;
            for (int i = 0; i < guessN; i++) {
                if (word.contains(userGuessed[i])) 
                    wordFlag = false;
            }
            if (!word.contains(userGuessed[guessN]))
                wordFlag = false;
            if (wordFlag)
                output.add(word);
        }
        return output;
    }

    public static void run(boolean debug, 
                            HashMap<Integer, HashSet<String>> dict, 
                            int length, int nGuesses) {
        HashSet<String> nDict = dict.get(length);
        int startGuesses = nGuesses;
        boolean contBool;
        do {
            contBool = true;
            nGuesses = startGuesses;

            HashSet<String> missingUserGuesses = new HashSet<>();
            missingUserGuesses.addAll(nDict);

            Scanner userIn = new Scanner(System.in);

            String[] userGuessed = new String[26];

            String userVisible = "";
            String hiddenWord;
            for (int i = 0; i < length; i++) 
                userVisible += "_";
            boolean userWon = false;

            int guessN = 0;

            while (nGuesses > 0) {
                System.out.println("You have guessed: " + userVisible);
                System.out.println("You have " + nGuesses + " guesses remaining.");
                System.out.println("Your guessed letters are: " + Arrays.toString(userGuessed));
                System.out.println();

                String userGuess = collectUserGuess(userGuessed, guessN);

                // first we will build a set of words that DON'T match the user's guesses
                HashSet<String> thirdCopy = new HashSet<>();
                thirdCopy.addAll(missingUserGuesses);

                for (String word : thirdCopy) {
                    if (word.contains(userGuess)) {
                        missingUserGuesses.remove(word);
                    }
                }

                if (missingUserGuesses.size() == 0) {
                    nDict = findMatches(nDict, userGuessed, guessN);
                    hiddenWord = nDict.iterator().next();
                } else if (missingUserGuesses.size() == 1) {
                    hiddenWord = missingUserGuesses.iterator().next();
                } else {
                    hiddenWord = nDict.iterator().next();
                }

                if (userVisible.equals(hiddenWord)) {
                    nGuesses = 1;
                    userWon = true;
                }

                guessN++;
                nGuesses--;
            }

            if (userWon) 
                System.out.println("You won!");
            else 
                System.out.println("You lost!");
            System.out.println("Would you like to play again? (y/n)");
            String cont = userIn.next().substring(0, 1).toLowerCase();

            if (cont.equals("y")) 
                contBool = true;
            else 
                contBool = false;

        } while (contBool);
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