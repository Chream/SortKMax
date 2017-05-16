package assignment;

import parallel.SortKMaxPara;
import parallel.SortKMaxParaRec;
import sequential.SortKMaxAbs;
import sequential.SortKMaxBuiltInn;
import sequential.SortKMaxSeq;

import java.util.Arrays;

public class Oppgave2 {
    public static void main(String args[]) {

        boolean checkResult = true;

        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        int algoType = Integer.parseInt(args[2]);
        int threadCount;
        try {
            threadCount = Integer. parseInt (args[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            threadCount = Runtime.getRuntime().availableProcessors();
        }
        int runCount = 7;

        System.out.println ("ALGO: " + algoType +
                            " MaxThreads: " + (algoType == 1 |
                                               algoType == 2 ?
                                               "NA" :
                                               threadCount));
        double[] runResults = new double[runCount];

        SortKMaxAbs a;
        switch (algoType) {
        case 1:
            a = new SortKMaxBuiltInn(n,k);
            break;
        case 2:
            a = new SortKMaxSeq(n,k);
            break;
        case 3:
            a = new SortKMaxParaRec(n,k, threadCount);
            break;
        case 4:
            a = new SortKMaxPara(n,k, threadCount);
            break;

        default:
            System.out.println ("Invalid algorithm type!");
            return;
        }

        for (int i = 0; i < runCount; i++) {
            runResults[i] = a.doBenchmark();
        }
        Arrays.sort(runResults);
        a.writeResult(runResults[runCount / 2]);

        if (checkResult) {
            checkSortResultP(a);
        }
    }

    private static boolean checkSortResultP (SortKMaxAbs a) {
        // // Helper array used for checking correctness of sorting routine
        System.out.println ("CHECKING RESULT.");
        if (a instanceof SortKMaxBuiltInn) {
            return true;
        } else {
            int n = a.getN();
            int k = a.getK();
            SortKMaxBuiltInn b = new SortKMaxBuiltInn(n, k);
            b.insertSort(a.getArray(), 0, n);
            int [] aArr = a.getArray();
            int [] bArr = b.getArray();

            // Test that array is properly sorted.
            int i, j;
            for (i = 0; i < k; i++) {
                j = aArr.length - 1 - i;
                if (aArr[i] != bArr[j]) {
                    System.out.printf ("NOT EQUAL. aArr[%d] = %d != bArr[%d] = %d!%n",
                                       i, aArr[i], j, bArr[j]);
                    a.print();
                    b.print();
                    return false;
                }
            }
            return true;
        }
    }
}
