import deque.Maximizer61B;
import deque.ArrayDeque61B;

import org.junit.jupiter.api.*;

import java.util.Comparator;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class Maximizer61BTest {
    private static class StringLengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    private static class IntComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    @Test
    public void basicTest() {
        ArrayDeque61B<String> ad = new ArrayDeque61B<>();
        ad.addFirst("");
        ad.addFirst("2");
        ad.addFirst("fury road");
        assertThat(Maximizer61B.max(ad, new StringLengthComparator())).isEqualTo("fury road");
    }

    @Test
    public void basicTest2() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(0);
        ad.addFirst(1);
        ad.addFirst(2);
        assertThat(Maximizer61B.max(ad, new IntComparator())).isEqualTo(2);
    }

    @Test
    public void maxDefault() {
        ArrayDeque61B<String> ad = new ArrayDeque61B<>();
        ad.addFirst("");
        ad.addFirst("2");
        ad.addFirst("fury road");
        assertThat(Maximizer61B.max(ad)).isEqualTo("fury road");
    }

    @Test
    public void maxEmpty() {
        ArrayDeque61B<String> ad = new ArrayDeque61B<>();
        assertThat(Maximizer61B.max(ad)).isEqualTo(null);
    }
}
