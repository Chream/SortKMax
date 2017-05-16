package parallel;

import sequential.SortKMaxSeq;

import java.util.concurrent.atomic.AtomicInteger;

public class SortKMaxParaRec extends SortKMaxSeq {

    int maxThreadCount;
    boolean debug = false;
    AtomicInteger threadCount = new AtomicInteger(1);

    public SortKMaxParaRec (int n, int k, int maxThreads) {
        super(n, k);
        this.maxThreadCount = maxThreads;
    }

    public SortKMaxParaRec(int[] a, int k, int maxThreads) {
        super(a, k);
        this.maxThreadCount = maxThreads;
    }

    public SortKMaxParaRec(int[] a, int k) {
        super(a, k);
        this.maxThreadCount = Runtime.getRuntime().availableProcessors();
    }

    @Override
    public void insertSortMerge (int[] a1,
                                 int start1,
                                 int length1,
                                 int[] a2,
                                 int start2,
                                 int length2) {

        Thread t = new Thread
            (new InsertSortMergeRecWorker(a1,
                                          start1,
                                          length1,
                                          a2,
                                          start2,
                                          length2));
        t.start();
        try {
            t.join();
            threadCount.set(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void singleThreadInsertSortMerge (int[] a1,
                                             int start1,
                                             int length1,
                                             int[] a2,
                                             int start2,
                                             int length2) {
        super.insertSortMerge(a1, start1, length1, a2, start2, length2);
    }
    public void singleThreadInsertSort (int[] a, int start, int length) {
        super.insertSort(a, start, length);
    }


    /*
     * Thread Worker class
     */
    private class InsertSortMergeRecWorker implements Runnable {

        int [] a1, a2;
        int start1, length1, start2, length2;

        public InsertSortMergeRecWorker (int[] a1,
                                         int start1,
                                         int length1,
                                         int[] a2,
                                         int start2,
                                         int length2) {

            this.a1 = a1;
            this.a2 = a2;
            this.start1 = start1;
            this.length1 = length1;
            this.start2 = start2;
            this.length2 = length2;
        }

        public void run () {

            /*
             * BASE CASE. If tail is short enough or if max threads is reached.
             * Sort A1 and merge A2 into A1.
             */
            if (length2 <= length1 * 2 | threadCount.get() >= maxThreadCount) {

                singleThreadInsertSort(a1, start1, length1);
                singleThreadInsertSortMerge(a1, start1, length1,
                                            a2, start2, length2);
                return;
            } else {
                /*
                 * Recursive case. If tail is long recursivly create two new threads and
                 * do work. After the child threads are complete sort A1 and insertMerge
                 * sort the results.
                 */
                threadCount.addAndGet(2);
                Thread t1, t2;
                int childStart1 = start2;
                int childStart2 = start2 + length1;
                int childTailStart1 = childStart2 + length1;
                int childTailLength1 = (length2 - 2 * length1) / 2;
                int childTailStart2 = childTailStart1 + childTailLength1;
                int childTailLength2 = length2 - childTailLength1 - (2 * length1);

                t1 = new Thread(new InsertSortMergeRecWorker(a2,
                                                             childStart1,
                                                             length1,
                                                             a2,
                                                             childTailStart1,
                                                             childTailLength1));
                t2 = new Thread(new InsertSortMergeRecWorker(a2,
                                                             childStart2,
                                                             length1,
                                                             a2,
                                                             childTailStart2,
                                                             childTailLength2));
                t1.start();
                t2.start();
                try {
                    singleThreadInsertSort(a1, start1, length1);
                    // wait for child threads.
                    t1.join();
                    t2.join();
                }
                catch (InterruptedException e) {
                    System.out.println("Error " + e.getMessage());
                    e.printStackTrace();
                }

                /*
                 * Merge the k first elements of the tail (A2) into A1.
                 */
                int sortedTailStart = childStart1;
                for (int i = 0; i < 2; i++) {
                    singleThreadInsertSortMerge(a1, start1, length1,
                                                a2, sortedTailStart, length1);
                    sortedTailStart = childStart2;
                }
            }
            return;

        }
    }
}
