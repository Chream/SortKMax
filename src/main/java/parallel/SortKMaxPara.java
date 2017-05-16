package parallel;

import sequential.SortKMaxSeq;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SortKMaxPara extends SortKMaxSeq {

    boolean debug = false;
    int threadCount;
    int childThreadCount;
    final int maxReasonableThreads;

    public SortKMaxPara (int n, int k, int threadCount) {
        super(n, k);
        maxReasonableThreads = (n - k) / k;
        this.threadCount = Math.min(threadCount,
                                    (maxReasonableThreads == 0 ?
                                     1 :
                                     maxReasonableThreads));
        this.childThreadCount = this.threadCount - 1;
    }

    public SortKMaxPara(int[] a, int k, int threadCount) {
        super(a, k);
        maxReasonableThreads = (n - k) / k;
        this.threadCount = Math.min(threadCount,
                                    (maxReasonableThreads == 0 ?
                                     1 :
                                     maxReasonableThreads));
        this.childThreadCount = this.threadCount - 1;
    }

    public SortKMaxPara(int[] a, int k) {
        super(a, k);
        maxReasonableThreads = (n - k) / k;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.threadCount = Math.min(availableProcessors,
                                    (maxReasonableThreads == 0 ?
                                     1 :
                                     maxReasonableThreads));
        this.childThreadCount = this.threadCount - 1;
    }

    @Override
    public void insertSortMerge (int[] a1,
                                 int start1,
                                 int length1,
                                 int[] a2,
                                 int start2,
                                 int length2) {
        Thread t;
        int partSize = length2 / threadCount;
        CyclicBarrier cb = new CyclicBarrier(threadCount);
        int newStart1 = start2;
        int newStart2 = start2 + length1;
        int newLength2 = partSize - length1;

        /*
         * Start (THREADCOUNT - 1) workers, each insertSorting and Merging
         * their tail into the first k elements of their respective
         * array part.
         */
        for (int i = 0; i < childThreadCount; i++) {

            t = new Thread
                (new InsertSortMergeWorker(a2,
                                           newStart1,
                                           length1,
                                           a2,
                                           newStart2,
                                           (i == childThreadCount - 1 ?
                                            (a2.length - newStart2) :
                                            newLength2)));
            t.start();
            newStart1 += partSize;
            newStart2 += partSize;
        }

        try {
            // Sort the first k elements of A1.
            singleThreadInsertSort(a1, start1, length1);
            cb.await();

            /*
             * InsertSort the sorted parts of a2 (each of length LENGTH1)
             * size into the * first [0...k - 1] slots of A1.
             */
            for (int i = 0; i < threadCount; i++) {

                singleThreadInsertSortMerge(a1, start1, length1,
                                            a2, start2, (threadCount == 1 ?
                                                         length2 :
                                                         length1));
                start2 += partSize;
            }
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
     * Thread Worker Class.
     */
    private class InsertSortMergeWorker implements Runnable {

        SortKMaxPara sorter;
        int [] a1, a2;
        int start1, length1, start2, length2;
        CyclicBarrier cb;

        public InsertSortMergeWorker (int[] a1,
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
            sorter.singleThreadInsertSort(a1, start1, length1);
            sorter.singleThreadInsertSortMerge(a1, start1, length1,
                                             a2, start2, length2);
            try {
                cb.await();
            }
            catch (BrokenBarrierException | InterruptedException e) {
                System.out.println("Error " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
