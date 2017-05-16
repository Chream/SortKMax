package sequential;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public abstract class SortKMaxAbs implements InsertSortable, Benchmarkable {

    private int[] a;
    protected int n;
    private int k;

    public SortKMaxAbs (int n, int k) {
        this.n = n;
        this.k = k;
        this.a = new int[n];
        this.fillArray();
    }

    public SortKMaxAbs (int[] a, int k) {
        this.a = a;
        this.n = a.length;
        this.k = k;
    }

    public int[] getArray () {return a;}
    public int getN () {return n;}
    public int getK () {return k;}

    public double doBenchmark () {
        String className = getClass().getName();
        // print info
        System.out.printf ("Running for %s n = %d, k = %d%n",className, n, k);
        this.fillArray();
        // System.out.printf("Originale Array: ");
        // this.print();

        /* Main sorting routine.
         * Start benchmark.
         */
        long startTime = System.nanoTime();
        // Sort k first elements.
        this.insertSort (a, 0, k);
        // Insert sort tail into a[0...k-1].
        this.insertSortMerge (a, 0, k, a, k, n - k);
        long endTime = System.nanoTime();
        // In milliseconds.
        double totalTime = (double)(endTime - startTime) / 1000000;
        /* End benchmark.*/

        System.out.printf ("%s Total time: %f ms.%n",className, totalTime);
        return totalTime;
    }

    private void fillArray () {
        Random r = new Random(7654);
        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(a.length);
        }
    }

    public void print () {
        for (int i = 0; i < a.length && i < 50; i++) {
            System.out.printf ("%-2d, ", a[i]);
        }
        System.out.printf ("%n");
    }

    public void writeResult (double TotalTime) {
        String fileName = String.format("%s-k=%d.txt",
                                        this.getClass().getName(), k);
        File f = null;
        FileWriter fw = null;
        BufferedWriter bw = null;;
        PrintWriter out = null;
        try {
            f = new File (fileName);
            fw = new FileWriter(f, true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
            if (f.length() == 0) {
                out.printf("n[count]_k=%d time[ms]%n", k);
                out.printf("0 0%n");
            }
            out.printf("%d %f%n", n, TotalTime);
        } catch (IOException e) {
            System.out.println ("Could not write to file!");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
