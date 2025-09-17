import java.util.ArrayList;
import java.util.List;

public class ListExercises {

    /** Returns the total sum in a list of integers */
    public static int sum(List<Integer> L) {
        int sum = 0;
        for (int i : L) {
            sum += i;
        }
        return sum;
    }

    /** Returns a list containing the even numbers of the given list */
    public static List<Integer> evens(List<Integer> L) {
        List<Integer> evenArr = new ArrayList<>();
        for (int i : L) {
            if (i % 2 == 0) {
                evenArr.add(i);
            }
        }
        return evenArr;
    }

    /** Returns a list containing the common item of the two given lists */
    public static List<Integer> common(List<Integer> L1, List<Integer> L2) {
        List<Integer> smaller = L1;
        List<Integer> matches = new ArrayList<>();
        if (L2.size() > L1.size()) {
            smaller = L2;
        }
        for (int i : smaller) {
            if (L1.contains(i) && L2.contains(i)) {
                matches.add(i);
            }
        }
        return matches;
    }


    /** Returns the number of occurrences of the given character in a list of strings. */
    public static int countOccurrencesOfC(List<String> words, char c) {
        int count = 0;
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == c) {
                    count++;
                }
            }
        }
        return count;
    }
}
