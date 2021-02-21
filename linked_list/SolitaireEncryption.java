public class SolitaireEncryption {

    public static char encryptChar(char letter, int key) {
        int value =   letter - 'a';
        int encryptedValue =  (value + key) % 26;
        char encryptedChar = (char) (encryptedValue+'a');

        return encryptedChar;
    }


    public static char decryptChar(char letter, int key) {
        int value =   letter - 'a';
        int decryptedValue =  (value + (26-key)) % 26;
        char decryptedChar = (char) (decryptedValue+'a');

        return decryptedChar;
    }

    public static int getKey(CircularLinkedList<Integer> deck) { // calls the steps methods
        step1(deck);
        step2(deck);
        step3(deck);
        step4(deck);
        return step5(deck);
    }

    private static void step1(CircularLinkedList<Integer> deck) {
        int jokerAIndex = deck.removeReturnIndex(27);
        deck.add((jokerAIndex + 1) % deck.size, 27);
    }

    private static void step2(CircularLinkedList<Integer> deck) {
        int jokerAIndex = deck.removeReturnIndex(28);
        deck.add((jokerAIndex + 2) % deck.size, 28);
    }
    private static void step3(CircularLinkedList<Integer> deck) {
        CircularLinkedList top = new CircularLinkedList();
        CircularLinkedList middle = new CircularLinkedList();
        CircularLinkedList bottom = new CircularLinkedList();

        int flag = 0;
        for (int i = 0; i < 28; i++) {
            int toAssign = deck.remove(0);
            if (toAssign == 27 || toAssign == 28) flag++;
            if (flag == 0)                                     top.add(toAssign);
            if (flag == 1 || toAssign == 27 || toAssign == 28) middle.add(toAssign);
            if (flag == 2 && toAssign != 27 && toAssign != 28) bottom.add(toAssign);
        }

        int botSize = bottom.size;
        int midSize = middle.size;
        int topSize = top.size;

        for (int i = 0; i < botSize; i++) deck.add((int) bottom.remove(0));
        for (int i = 0; i < midSize; i++) deck.add((int) middle.remove(0));
        for (int i = 0; i < topSize; i++) deck.add((int) top.remove(0));
    }
    private static void step4(CircularLinkedList<Integer> deck) {
        int bottom = deck.read(27);
        CircularLinkedList slice = new CircularLinkedList();
        for (int i = 0; i < bottom; i++) slice.add(deck.remove(0));
        for (int i = 0; i < bottom; i++) deck.add(27 - bottom + i, (int) slice.remove(0));
    }
    private static int step5(CircularLinkedList<Integer> deck) {
        int top = deck.read(0);
        return deck.read(top);
    }

    public static CircularLinkedList resetDeck() {
        CircularLinkedList deck = new CircularLinkedList();
        int[] deckInts = new int[] {1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 3, 6, 9, 12, 15, 18, 21, 24, 27, 2, 5, 8, 11, 14, 17, 20, 23, 26};
        for (int value : deckInts) {
            deck.add(value);
        }
        return deck;
    }

    public static void main(String[] args) {
        CircularLinkedList deck = resetDeck();

        String input;
        if (args.length == 0) input = "HeLlo";
        else input = args[0];
        System.out.println(input);

        char[] encrypted = new char[input.length()];
        input = input.toLowerCase();

        char[] inputChars = input.toCharArray();

        for (int i = 0; i < inputChars.length; i++) {
            encrypted[i] = encryptChar(inputChars[i], getKey(deck));
        }

        System.out.println(String.valueOf(encrypted));

        deck = resetDeck();

        char[] decrypted = new char[encrypted.length];
        for (int i = 0; i < encrypted.length; i++) {
            decrypted[i] = decryptChar(encrypted[i], getKey(deck));
        }
        System.out.println(String.valueOf(decrypted));
    }
}






