package sequential;

public class SortKMaxSeq extends SortKMaxAbs {

    boolean debug = false;

    public SortKMaxSeq (int n, int k) {
        super(n, k);
    }

    public SortKMaxSeq(int[] a, int k) {
        super(a, k);
    }

    public void insertSort (int[] a, int start, int length) {
        int end = start + length;
        int i, t;
        for (int current = start; current < end - 1; current++) {
            // invariant: a[start..current] er no sortert stigende (minste forst)
            t = a[current + 1]; // next element
            i = current; // current index
            while (i >= start && a[i] < t) {
                a[i + 1] = a[i];
                i--;
            }
            a[i + 1] = t;
        } // end insertSort
    }

    public void insertSortMerge (int[] a1,
                                 int start1,
                                 int length1,
                                 int[] a2,
                                 int start2,
                                 int length2) {
        if (debug) {
            System. out. println ("A2:insetSortMerge:::IN A2 insertSortMerge.");
            System.out.printf
                ("A2:inserSortMerge:::start1: %d, start2: %d length1: %d Length2: %d%n",
                 start1, start2, length1, length2);
        }

        int end1 = start1 + length1;
        int end2 = start2 + length2;
        int lowest, current;

        // Insert sort the rest of the array.
        for (int i = start2; i < end2; i++) {
            lowest = a1[end1 - 1];
            current = a2[i];
            if (current > lowest) {
                // exchange arr[k - 1] and arr[i].
                a1 [end1 - 1] = current;
                a2 [i] = lowest;

                // insert sort the new element.
                int j = end1 - 2;
                while (j >= start1 && a1[j] < current) {
                     a1[j + 1] = a1[j];
                    j--;
                }
                a1[j + 1] = current;
            }
        }
    }
}
