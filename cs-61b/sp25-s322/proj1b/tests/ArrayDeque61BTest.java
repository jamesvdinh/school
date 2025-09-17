import deque.ArrayDeque61B;

import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

     @Test
     @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
     void noNonTrivialFields() {
         List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
     }

    @Test
    void addFirstTest1() {
         ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

         lld1.addFirst(1);
         lld1.addFirst(2);
         lld1.addFirst(3);

         assertThat(lld1.toList()).containsExactly(3, 2, 1).inOrder();
    }

    @Test
    void addFirstTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        assertThat(lld1.toList()).containsExactly(3, 2, 1).inOrder();
    }

    @Test
    void addFirstOversizeTest() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        assertThat(lld1.toList()).containsExactly(3, 2, 1, 3, 2, 1, 3, 2, 1).inOrder();
    }

    @Test
    void addLastTest1() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);

        assertThat(lld1.toList()).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    void addLastTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);

        assertThat(lld1.toList()).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    void addLastOversizeTest() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);

        assertThat(lld1.toList()).containsExactly(1, 2, 3, 1, 2, 3, 1, 2, 3).inOrder();
    }

    @Test
    void addFirstAndLastTest() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        assertThat(lld1.toList()).containsExactly(3, 2, 1, 3, 2, 1, 1, 2, 3).inOrder();
    }

    @Test
    void getTest1() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addLast(2);

        assertThat(lld1.get(2)).isEqualTo(null);
    }

    @Test
    void getTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addLast(2);

        assertThat(lld1.get(0)).isEqualTo(1);
    }

    @Test
    void getTest3() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addLast(2);

        assertThat(lld1.get(-2)).isEqualTo(null);
    }

    @Test
    void getRecursiveTest1() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addLast(2);

        assertThat(lld1.getRecursive(2)).isEqualTo(null);
    }

    @Test
    void getRecursiveTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addLast(2);

        assertThat(lld1.getRecursive(0)).isEqualTo(1);
    }

    @Test
    void getRecursiveTest3() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addLast(2);

        assertThat(lld1.getRecursive(-2)).isEqualTo(null);
    }

    @Test
    void isEmptyTest1() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        assertThat(lld1.isEmpty()).isTrue();
    }

    @Test
    void isEmptyTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addFirst(2);

        assertThat(lld1.isEmpty()).isFalse();
    }

    @Test
    void sizeTest1() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        assertThat(lld1.size()).isEqualTo(0);
    }

    @Test
    void sizeTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addFirst(2);

        assertThat(lld1.size()).isEqualTo(2);
    }

    @Test
    void sizeTest3() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addFirst(2);
        lld1.removeFirst();
        lld1.removeFirst();

        assertThat(lld1.size()).isEqualTo(0);
    }

    @Test
    void sizeTest4() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.removeFirst();

        assertThat(lld1.size()).isEqualTo(0);
    }

    @Test
    void removeFirstTest1() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addFirst(2);
        lld1.removeFirst();

        assertThat(lld1.toList()).containsExactly(1).inOrder();
    }

    @Test
    void removeFirstTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addFirst(2);
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    void removeFirstTest3() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.removeFirst();

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    void removeFirstTest4() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        for (int i = 0; i < 9; i++) {
            lld1.addFirst(1);
        }
        for (int i = 0; i < 9; i++) {
            lld1.removeFirst();
        }

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    void removeLastTest1() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addFirst(2);
        lld1.removeLast();

        assertThat(lld1.toList()).containsExactly(2).inOrder();
    }

    @Test
    void removeLastTest2() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addLast(1);
        lld1.addFirst(2);
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    void removeLastTest3() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        for (int i = 0; i < 9; i++) {
            lld1.addFirst(1);
        }
        for (int i = 0; i < 9; i++) {
            lld1.removeLast();
        }

        assertThat(lld1.toList()).containsExactly().inOrder();
    }

    @Test
    void removeFirstAndLastTest() {
        ArrayDeque61B<Integer> lld1 = new ArrayDeque61B();

        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);
        lld1.removeFirst();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.removeLast();

        assertThat(lld1.toList()).containsExactly(2, 1, 1, 2).inOrder();
    }
}
