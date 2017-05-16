package sequential;

public interface InsertSortable {
    public void insertSort(int[] a, int start, int length);
    public void insertSortMerge(int[] a1, int start1, int length1,
                                int[] a2, int start2, int length2);
}
