package sequential;

import java.util.Arrays;


public class SortKMaxBuiltInn extends SortKMaxAbs {

    public SortKMaxBuiltInn (int n, int k) {
        super(n, k);
    }

    public void insertSort (int[]a1, int start1, int length1) {
        Arrays.sort(this.getArray());
    }
    public void insertSortMerge (int[] a1, int start1, int length1,
                                 int[] a2, int start2, int length2)
    {}

    public void print () {
        int startIndex = this.getArray().length - 1;
        int printLength = 50;
        for (int i = startIndex; i > startIndex - printLength && i >= 0; i--) {
            System.out.printf ("%-2d, ", this.getArray()[i]);
        }

        System.out.printf ("%n");
    }
}
