import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.io.*;
public class threeSumproject
{


    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );



    /* define constants */

    static long MAXV =  2000000000;
    static long MINV = -2000000000;
    static int numberOfTrials = 100;
    static int MAXINPUTSIZE  = (int) Math.pow(2,11);
    static int MININPUTSIZE  =  1;

    // static int SIZEINCREMENT =  10000000; // not using this since we are doubling the size each time



    static String ResultsFolderPath = "/home/cody/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;





    public static void main(String[] args) {


        // test before hand
        test();
        // run the whole experiment at least twice, and expect to throw away the data from the earlier runs, before java has fully optimized
        runFullExperiment("thre-Exp1-ThrowAway.txt");
       runFullExperiment("thre-Exp2.txt");
       runFullExperiment("three-Exp3.txt");

    }



    static void runFullExperiment(String resultsFileName){



        try {

            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);

        } catch(Exception e) {

            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...

        }



        ThreadCpuStopWatch BatchStopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial


        resultsWriter.println("#InputSize    AverageTime"); // # marks a comment in gnuplot data
        resultsWriter.flush();
        /* for each size of input we want to test: in this case starting small and doubling the size each time */
        for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize*=2) {
            // progress message...
            System.out.println("Running test for input size "+inputSize+" ... ");


            /* repeat for desired number of trials (for a specific size of input)... */
            long batchElapsedTime = 0;
            // generate a list of randomly spaced integers in ascending sorted order to use as test input
            // In this case we're generating one list to use for the entire set of trials (of a given input size)
            // but we will randomly generate the search key for each trial
            System.out.print("    Generating test data...");
            long[] testList = createRandomListOfIntegers(inputSize) ;
            System.out.println("...done.");
            System.out.print("    Running trial batch...");


            /* force garbage collection before each batch of trials run so it is not included in the time */
            System.gc();




            // instead of timing each individual trial, we will time the entire set of trials (for a given input size)
            // and divide by the number of trials -- this reduces the impact of the amount of time it takes to call the
            // stopwatch methods themselves
            BatchStopwatch.start(); // comment this line if timing trials individually


            // run the tirals
            for (long trial = 0; trial < numberOfTrials; trial++) {
                // generate a random key to search in the range of a the min/max numbers in the list
               // long testSearchKey = (long) (0 + Math.random() * (testList[testList.length-1]));
                /* force garbage collection before each trial run so it is not included in the time */
                // System.gc();


                //TrialStopwatch.start(); // *** uncomment this line if timing trials individually
                /* run the function we're testing on the trial input */
               long foundIndex = count(0, testList);
               //long counted = fastestthree(testList);
                // batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing trials individually

            }

            batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if timing trials individually
            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials; // calculate the average time per trial in this batch


            /* print data for this size of input */
            resultsWriter.printf("%12d  %15.2f \n",inputSize, averageTimePerTrialInBatch); // might as well make the columns look nice
            resultsWriter.flush();
            System.out.println(" ....done.");

        }

    }






    public static int count(long sum, long[] a )
    {
        //three some function start up
        int N = a.length;
        int count = 0;
        //sort function to be un commented
       // sort(a);
        // beginning of brute force algorithm also carrys the binary search function
        //
        //
        for (int i = 0; i < N; i++)
        {
            for (int j = i+1; j < N; j++)
            {
                //binary search function used to find in terms of O(N^2logN)
               /*if(binarySearch(-(a[i]+a[j]),a) > j )
               {

                   count++;
               }*/
               // last line in function for O(N^3)
                for(int k = j + 1; k < N; k++)
                {
                    if(a[i]+a[k]+a[j] == sum) {

                        count++;
                    }
                }
            }
        }
        return count;
    }


    public static long[] createRandomListOfIntegers(int size)
    {
        // random list generates used in many projects includes negative numbers as well
        long [] randomArray = new long[size];
        for(int j=0; j<size;j++)
        {
            randomArray[j] = (long)(Math.random() *(MAXV - MINV));
        }
        return randomArray;

    }

    public static void test()
    {
        //testing array currently modded for the 3sum
        long[] intArray = new long[]{ 1,2,3,-3,4,6,5,7,-5,8,10,9};
        long sum = 0;
        long cnt;
        //cnt = count (sum, intArray  );
        int cont = fastestthree(intArray);

        System.out.println("Found  "+cont+" amount ");
    }

    //bubble sort for the 3sum sorting requirments for faster speed
    public static void sort(long[] list)
    {
        for(int i = 0; i < list.length ; i++)
        {
            for(int j = 0; j < list.length - 1; j++)
            {
                if (list[j] > list[j+1])
                {
                    long tmp = list[j];
                    list[j] = list[j+1];
                    list[j+1] = tmp;
                }
            }
        }
    }

    //Binary Search coded by  scott graham
    public static int binarySearch(long key, long[] list) {

        int i = 0;

        int j= list.length-1;

        if (list[i] == key) return i;

        if (list[j] == key) return j;

        int k = (i+j)/2;

        while(j-i > 1){

            //resultsWriter.printf("%d %d %d %d %d %d\n",i,k,j, list[0], key, list[list.length-1]); resultsWriter.flush();

            if (list[k]== key) return k;

            else if (list[k] < key) i=k;

            else j=k;

            k=(i+j)/2;

        }

        return -1;

    }

    // fastest function going to O(N^2)
    public static int fastestthree(long a[])
    {
        int start;
        int end;
        int b;
        int c;
        int count = 0;

        //sort function so everything is in order
        sort(a);
        for(int i = 0; i < a.length; i++)
        {
            int n = (int)a[i];
            start = i+1;
            end = a.length - 1;
            while( start < end)
                {
                    // while sorted start from the ends and work inward to find int equaling 0
                    b = (int)a[start];
                    c = (int)a[end];
                    if( n+b+c == 0) {
                        count++;

                        start = start + 1;
                        end = end - 1;
                    }
                    // if greater goes down from the highest point else goes up
                    else if(n+b+c > 0)
                    {
                        end = end -1;
                    }
                    else
                    {
                        start = start +1;
                    }

                }


        }
        return count;
    }


}

