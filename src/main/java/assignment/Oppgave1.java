package assignment;

import sequential.Benchmarkable;
import sequential.SortKMaxBuiltInn;
import sequential.SortKMaxSeq;

import java.util.Arrays;

public class Oppgave1 {
    public static void main(String args[]) {

        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        int algoType = Integer.parseInt(args[2]);
        int runCount = 7;

        double[] runResults = new double[runCount];

        Benchmarkable a;
        switch (algoType) {
        case 1:
            a = new SortKMaxBuiltInn(n,k);
            break;
        case 2:
            a = new SortKMaxSeq(n,k);
            break;
        default:
            System.out.println ("Invalid algorithm type!");
            return;
        }

        for (int i = 0; i < runCount; i++) {
            runResults[i] = a.doBenchmark();
            Arrays.sort(runResults);
        }

        a.writeResult(runResults[runCount / 2]);
    }
}
