README for FindKMax

To Build:

./gradlew build

To Use:

Put ./build/libs/SortKMax.jar into your classpath.

Put "import sortkmax.sortKMax;" into the top of your java file.

The main method is:

public static int[] sortKMax(int[] a, int k, int start, int end);

It accepts an array and will find and sort the largest k elements of a into the
beginning of the array.


int[] a = new int[100]
fillArray(a); // psuedo method.
int[] aSorted = sortKMax(a, k); // Note: Will also modify a.
