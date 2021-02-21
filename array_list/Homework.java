import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class Homework {
    public static <E> boolean unique(List<E> list) {
        for (int i = 0; i < list.size(); i++) {
            E first = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                if (first.equals(list.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Integer> allMultiples(List<Integer> A, int i) {
        List<Integer> output = new ArrayList<Integer>();
        for (Integer element : A) {
            if (element % i == 0) {
                output.add(element);
            }
        }
        return output;
    }

    public static List<String> allStringsOfSize(List<String> A, int size) {
        List<String> output = new ArrayList<String>();
        for (String element : A) {
            if (element.length() == size) {
                output.add(element);
            }
        }
        return output;
    }

    public static <E> HashMap<E, Integer> listToCounter(List<E> A) {
        HashMap<E, Integer> output = new HashMap<>();
        for (E element : A) {
            if (!output.containsKey(element)) {
                output.put(element, 1);
            } else {
                output.replace(element, output.get(element) + 1);
            }
        }
        return output;
    }

    public static <E> boolean isPermutation(List<E> A, List<E> B) {
        HashMap<E, Integer> counterA = listToCounter(A);
        HashMap<E, Integer> counterB = listToCounter(B);
        return (counterA.equals(counterB));
    }

    public static List<String> stringToListOfWords(String str) {
        // https://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java
        // Happened upon slightly by accident while searching 
        // "remove all punctuation from string java"
        // I don't know regex. 
        return new ArrayList(Arrays.asList(str.replaceAll("[^a-zA-Z ]", "").split("\\s+")));
    }

    public static <E> void removeAllInstances(List<E> list, E item) {
        // Per java documentation... 
        list.removeAll(Arrays.asList(item));
    }

    /*
    public static <E> void removeAllInstances(List<E> list, E item) {
        int i = 0; 
        while (i < list.size()) {
            if (list.get(i).equals(item)) {
                list.remove(i);
            } else {
                i++;
            }
        }
    }
    */

    public static void main(String[] args) {
        System.out.println("Unique 0: " + 
                    unique(Arrays.asList(1, 2, 3, 4, 5, 6)));
        System.out.println("Unique 1: " + 
                    !unique(Arrays.asList(1, 1, 1, 1, 1, 1, 1)));
        System.out.println("Unique 2: " + 
                    unique(Arrays.asList(
                        "This is a String", 
                        "This is a different String")));
        System.out.println("Unique 3: " + 
                    !unique(Arrays.asList(
                            "This string will be the same", 
                            "This String will be different", 
                            "This string will be the same")));
        System.out.println("Unique 4: " + 
                    unique(Arrays.asList(6, 5, 4, 3, 2, 1)));
        System.out.println();

        System.out.println("allMultiples0: " + 
                    allMultiples(Arrays.asList(1, 25, 2, 5, 30, 19, 57, 2, 25), 5)
                        .equals(Arrays.asList(25, 5, 30, 25)));
        System.out.println("allMultiples1: " + 
                    allMultiples(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3)
                        .equals(Arrays.asList(3, 6, 9)));
        System.out.println();

        System.out.println("allStringsOfSize0: " + 
                    allStringsOfSize(Arrays.asList("apple", "banana", "orange", "5lstr"), 5)
                        .equals(Arrays.asList("apple", "5lstr")));
        System.out.println();

        System.out.println("isPermutation0: " + 
                    isPermutation(Arrays.asList(1, 2, 3), Arrays.asList(3, 2, 1)));
        System.out.println("isPermutation1: " + 
                    isPermutation(Arrays.asList(1, 1, 1, 2), Arrays.asList(1, 2, 1, 1)));
        System.out.println("isPermutation2: " + 
                    !isPermutation(Arrays.asList(1, 1), Arrays.asList(1)));
        System.out.println("isPermutation3: " + 
                    !isPermutation(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3, 4)));
        System.out.println();

        System.out.println("stringToListOfWords0: " + 
                    stringToListOfWords("This is a string.").equals(Arrays.asList("This", "is", "a", "string")));
        System.out.println();

        List list = new ArrayList<Integer>(Arrays.asList(1, 4, 5, 6, 5, 5, 2));
        removeAllInstances(list, 5);
        System.out.println("removeAllInstances0: " + 
                    list.equals(Arrays.asList(1, 4, 6, 2)));
        System.out.println();
    }
}





