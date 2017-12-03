/**
 * The purpose of this is to implement an efficient algorithm to find co-occurences from a dataset.
 * The co-occurences that bound beyond the threshold are returned in a list from the algorithm.
 *
 * @author Robert Saunders (NetID: 15rws, Student #: 10194030)
 * @version 1.0.0
 */

import java.io.*;
import java.util.*;

public class CopresenceNetwork {

    //value of m, represents the number of rows in matrix
    private int m;
    //value of n, represents the number of columns in matrix
    private int n;
    // the co-occurrence threshold
    private int threshold;
    // matrix that holds count of co-concurrences
    // NOTE: Could make this a dynamic 2 dimensional list.
    private int[][] matrix;
    // two dimensional vector to store all plane records
    List<List<Integer>> planeList = new ArrayList<>();

    /**
     * Copresence network constructor, reads data in from file and sets class properties.
     * @param filename The name of the file with data to search for co-occurences.
     */
    CopresenceNetwork(String filename, int threshold) {
        BufferedReader reader = null;
        String line = "";
        String delimiter = ",";
        try {
            reader = new BufferedReader(new FileReader(filename));
            int lineCounter = -1;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(delimiter);
                ArrayList<Integer> planeList = new ArrayList<Integer>();
                for (String person : values) {
                    planeList.add(Integer.parseInt(person));
                }
                setPlaneListValue(planeList);
                lineCounter++;
            }
        } catch (FileNotFoundException e) {
            System.out.print("File could not be found, cannot create network.");
        } catch (IOException e) {
            System.out.print("Items read from the file are not of type string, please use a different file.");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.print("Error closing reader.");
                }
            }
        }

        setThreshold(threshold); // set the threshold

        int maxValue = getMaxValue(getPlaneList()); // find the total number of people, used to create matrix

        setM(maxValue+1); // set the values of the matrix
        setN(maxValue+1);

        this.matrix = new int[getM()][getN()]; // create the copresence matrix
    }

    //////////////
    /* GETTERS */
    ////////////

    /**
     * Gets the value for m class attribute, represents rows in the matrix.
     * @return Integer value for m class attribute.
     */
    private int getM() {
        return m;
    }

    /**
     * Gets the value for n class attribute, represents columns in the matrix.
     * @return Integer value for n class attribute.
     */
    private int getN() {
        return n;
    }

    /**
     * Gets the value in the matrix at the i and j entry, where i is row and j is column.
     * @param i Represents the row in the matrix.
     * @param j Represents the column in the matrix.
     * @return The integer value in the matrix at i and j.
     */
    private int get(int i, int j) {
        return matrix[i][j];
    }

    /**
     * Gets the plane list.
     * @return The plane list.
     */
    private List<List<Integer>> getPlaneList() { return planeList; }

    /**
     * Getter for the co-occurrence threshold.
     * @return The co-occurrence threshold.
     */
    public int getThreshold() { return threshold; }

    //////////////
    /* SETTERS */
    ////////////

    /**
     * Sets the co-occurrence threshold.
     * @param value The value to be set as the threshold.
     */
    private void setThreshold(int value) { this.threshold = value; }

    /**
     * Sets the value in the matrix at the i and j entry, where i is row and j is column.
     * @param i Represents the row in the matrix.
     * @param j Represents the column in the matrix.
     */
    private void setMatrixValue(int i, int j) {
        this.matrix[i][j] = get(i,j) + 1;
    }

    /**
     * Sets a value in the plane list.
     * @param plane A plane, represented as a list of integers (people).
     */
    private void setPlaneListValue(ArrayList<Integer> plane) { this.planeList.add(plane); }

    /**
     * Sets the value of m.
     * @param value The value that m is going to be set to.
     */
    private void setM(int value) {
        this.m = value;
    }

    /**
     * Sets the value of n.
     * @param value The value that n is going to be set to.
     */
    private void setN(int value) {
        this.n = value;
    }

    //////////////////////
    /* UTILITY METHODS */
    ////////////////////

    /**
     * Computes all of the cooccured pairs in the data.
     * @return A list of all the cooccured pairs that cooccur over the threshold.
     */
    private List<List<Integer>> getCooccuredPairs() {

        // iterate every plane
        for (int i = 0; i < planeList.size(); i++) {
            List<Integer> plane = planeList.get(i);

            // find all combinations (subsets) in the plane
            List<List<Integer>> planeSubsets = getSubsets(2, plane);

            // represent each subset in the matrix
            for (int j = 0; j < planeSubsets.size(); j++) {
                List<Integer> subset = planeSubsets.get(j);
                setMatrixValue(subset.get(0), subset.get(1));
            }
        }

        // create new array to hold all pairs over threshold.
        List<List<Integer>> pairs = new ArrayList<>();

        // check matrix values to see if over threshold, if so add to pair array
        for (int row = 0; row < getM(); row++) {
            for (int column = 0; column < getN(); column++) {
                int occurrence = get(row, column);
                if (occurrence >= this.threshold) {
                    ArrayList<Integer> pair = new ArrayList<>();
                    pair.add(row);
                    pair.add(column);
                    pairs.add(pair);
                }
            }
        }

        // return the pairs
        return pairs;
    }

    /**
     * Finds the subsets (combinations) in the plane list.
     * @param k The size of the combinations.
     * @param input The input to get combinations from.
     * @return A list of subsets.
     */
    public List<List<Integer>> getSubsets(int k, List<Integer> input) {
        List<List<Integer>> subsets = new ArrayList<>();

        int[] s = new int[k];

        if (k <= input.size()) {
            for (int i = 0; (s[i] = i) < k - 1; i++);
            subsets.add(getSubsetHelper(input, s));
            for(;;) {
                int i;
                for (i = k - 1; i >= 0 && s[i] == input.size() - k + i; i--);
                    if (i < 0) {
                        break;
                    }
                s[i]++;
                for (++i; i < k; i++) {
                    s[i] = s[i - 1] + 1;
                }
                subsets.add(getSubsetHelper(input, s));
            }
        }
        return subsets;
    }

    /**
     * Helper to the getSubsets method.
     * @param input Input to get subsets from.
     * @param subset The subset.
     * @return List of subsets.
     */
    private List<Integer> getSubsetHelper(List<Integer> input, int[] subset) {
        int[] result = new int[subset.length];
        for (int i = 0; i < subset.length; i++)
            result[i] = input.get(subset[i]);
        List<Integer> intList = new ArrayList<Integer>();
        for (int index = 0; index < result.length; index++)
            intList.add(result[index]);
        return intList;
    }

    /**
     * Gets the max value from a two dimensional array.
     * @param numbers
     * @return
     */
    public static int getMaxValue(List<List<Integer>> numbers) {
        int maxValue = numbers.get(0).get(0);
        for (int j = 0; j < numbers.size(); j++) {
            for (int i = 0; i < numbers.get(j).size(); i++) {
                if (numbers.get(j).get(i) > maxValue) {
                    maxValue = numbers.get(j).get(i);
                }
            }
        }
        return maxValue;
    }

    /**
     * Returns a string representation of the matrix.
     * @return The string representation of the matrix.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("\nCo-ocurrence matrix dimensions:"));
        stringBuilder.append(String.format(" %d",getM()-1));
        stringBuilder.append(",");
        stringBuilder.append(String.format(" %d",getN()-1));
        stringBuilder.append("\n\n");

        for (int row = 1; row < getM(); row++) {
            for (int column = 1; column < getN(); column++) {
                stringBuilder.append(String.format("%d",get(row,column)));
                if (column+1 != getN()) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /////////////////////
    /* TESTING METHOD */
    ///////////////////

    /**
     * Used to test the copresence network algorithm.
     * @param args Arguments passed into the execution of the program.
     */
    public static void main(String[] args) {

        // start a timer
        final long startTime = System.nanoTime();

        int threshold = 20;

        // create a copresence network from the input data
        CopresenceNetwork network = new CopresenceNetwork("./lists.csv", threshold);

        // get the cooccured pairs that do not satisfy the threshold
        List<List<Integer>> myPairs = network.getCooccuredPairs();

        // calculate algorithm execution time
        final long duration = System.nanoTime() - startTime;

        // output the copresence matrix
        System.out.println(network.toString());

        // print the threshold
        System.out.printf("\nThreshold: %d", network.getThreshold());

        // print the number of pairs above the threshold
        System.out.printf("\nNumber of Pairs Above Threshold: %d", myPairs.size());

        // output the total execution time
        System.out.printf("\nTotal execution time: %d milliseconds\n", duration/1000000);
    }
}
