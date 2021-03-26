import java.io.*;
import java.util.*;

public class Hangman {

    public static HashMap<String,List> createWordFamilies(List <String> wordlist, char letter){
        HashMap<String, List> map = new HashMap<>();
        for(String word : wordlist){
            String id = getID(word, letter);
            if(map.get(id) == null){
                List <String> list = new ArrayList<>();
                map.put(id, list);
            }
            List <String> list = new ArrayList<>();
            list = map.get(id);
            list.add(word);
            map.put(id,list);
        }
        return map;
    }

    public static String getID(String word, char letter){
        String id = "";
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) == (letter)) {
                id += 1;
            }
            else{
                id += 0;
            }
        }
        return id;
    }

    public static List<String> createDictionary(int numLetters){
        String fileName = "words.txt";
        List <String> wordlist = new ArrayList<>();


        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //System.out.println(line);
                String[] words = line.split("\\s+");

                for (String word : words) {
                    word = word.toLowerCase(Locale.ROOT);
                    //System.out.println(word);

                    if(word.length() == numLetters){
                        //add each word to storage method
                        wordlist.add(word);
                    }
                }
            }
            scanner.close();

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return wordlist;
    }

    public static HashMap<String, List> mostOptions(HashMap<String, List> map){
        HashMap<String, List> most = new HashMap<>();

        String key = "";
        List <String> list = new ArrayList<>();
        int longest = 0;
        for (Map.Entry<String,List> entry : map.entrySet()){
            if(entry.getValue().size() > longest){
                longest = entry.getValue().size();
                list = entry.getValue();
                key = entry.getKey();
            }
        }
        most.put(key,list);
        return most;
    }

    public static String wordSoFar(int numLetters){
        String word = "";
        for(int i = 0; i < numLetters; i++){
            word = word + "-";
        }
        return word;
    }

    public static void main(String[] args) {

        //Get input from user

        Scanner input = new Scanner(System.in);
        int numLetters = 30;
        //while numLetters is a valid key
        while(numLetters > 22) {
            System.out.println("Enter the size of the word you want to guess: ");
            numLetters = Integer.parseInt(input.nextLine());
            if (numLetters > 22){
                System.out.println("Please enter a valid word size.");
            }
        }
        System.out.println("Please enter the number of guesses you would like to get: ");
        int numGuesses = Integer.parseInt(input.nextLine());
        if(numGuesses >= 26){
            System.out.println("Cheater! But we'll allow it...");
        }



        //create dictionary

        List <String> wordlist = createDictionary(numLetters);

        //create set of guessed letters
        Set <Character> guessedLetters = new HashSet <> ();

        String wordSoFar = wordSoFar(numLetters);

        while(numGuesses > 0) {

            //ask for a letter
            System.out.println("Please guess a letter: ");
            String letters = input.nextLine().toLowerCase(Locale.ROOT);
            char letter = letters.charAt(0);

            while(guessedLetters.add(letter) == false) {
                System.out.println("You already guessed that! Please guess another letter: ");
                letters = input.nextLine().toLowerCase(Locale.ROOT);
                letter = letters.charAt(0);
            }


            HashMap<String, List> map = createWordFamilies(wordlist, letter);

            //choose the key that has the most options
            //System.out.println(mostOptions(map));
            //numGuesses = 0;
            //make that list the new wordlist

            map = mostOptions(map);

            String word = "";

            for (Map.Entry<String,List> entry : map.entrySet()){
                word = entry.getKey();
                //System.out.println(word);

                wordlist = entry.getValue();
                //System.out.println(wordlist);
            }

            //adjust wordSoFar
            String newWord = "";


            for(int j = 0; j < numLetters; j++){
                if(wordSoFar.charAt(j) == '-'){
                    if(word.charAt(j) == '1'){
                        newWord = newWord + letter;
                    }
                    else{
                        newWord += "-";
                    }
                }
                else{
                    newWord += wordSoFar.charAt(j);
                }
            }

            wordSoFar = newWord;

            //newWord is what we want printed

            //decrement guesses
            numGuesses --;

            //print out revealed letters with spaces, wrong guesses, and number of guesses remaining

            System.out.println(newWord);
            System.out.println("Guessed letters: " + guessedLetters);
            System.out.println("Guesses Remaining: " + numGuesses);

            //check if win
            for(int k = 0; k < numLetters; k++){
                if(newWord.charAt(k) == '-'){
                    break;
                }
                if(k == (numLetters - 1)){
                    System.out.println("Congratulations! You win!");
                    numGuesses = -1;
                }
            }
        }

        if(numGuesses == 0){
            System.out.println("Sorry! The word was " + wordlist.get(0));
        }

        input.close();

    }
}
