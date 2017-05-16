package sortkmax;

import parallel.SortKMaxParaRec;
import sequential.InsertSortable;
import sequential.SortKMaxSeq;

public class SortKMax {

    public static int[] sortKMax(int[] a, int k) {
        int len = a.length;
        InsertSortable sorter;

        if (len < 3000000) {
            sorter = new SortKMaxSeq(a, k);
        } else {
            sorter = new SortKMaxParaRec(a, k );
        }

        // Sort first k elements.
        sorter.insertSort(a, 0, k);
        // Insert sort into a[0...k-1]
        sorter.insertSortMerge(a, 0, k, a, k, len - k);
        return a;
    }
}
