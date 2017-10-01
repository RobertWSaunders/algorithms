import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Robert Saunders, 10194030, 15rws

public class BestEmployee {

    private static String uniform_path = "us1.csv"; // path to uniform dataset
    private static String exponential_path = "es1.csv"; // path to exponential dataset

    /**
     * Main execution method of the program.
     * @param args
     */
	public static void main(String[] args){

     double[] uniformData = fetchData(uniform_path);
     double[] exponentialData = fetchData(exponential_path);

     System.out.println("Max Possible Value (Uniform Data) = " + getLargestNumber(uniformData));
     System.out.println("Max Possible Value (Exponential Data) = " + getLargestNumber(exponentialData));
     System.out.println("Max Employee Score Found is (Uniform Data): " + findBestEmployee(uniformData));
     System.out.println("Max Employee Score Found is (Exponential Data): " + findBestEmployee(exponentialData));
    }

    /**
     * Get the largest number from the array.
     * @return The largest number from the array.
     */
    public static double getLargestNumber(double[] numberArray) {
        double maxValue = 0;
        for (int i = 0; i < numberArray.length; i++) {
            if (numberArray[i] > maxValue) {
                maxValue = numberArray[i];
            }
        }
        return maxValue;
    }


    /**
     * Fetches the data from the csv.
     * @param path The path to the file with the numbers in.
     * @return An array of numbers from the csv file.
     */
    public static double[] fetchData(String path) {
        String line;
        double[] data = new double[1000];

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                for(int i = 0; i < data.length; i++){
                    data[i] = Double.parseDouble(values[i]);
                }
            }
            bufferedReader.close();
            return data;
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + path + "'");
        }
        catch(IOException e) {
            System.out.println("Error reading file '" + path + "'");
        }
        return data;
    }

    /**
     * Finds the best employee (number) from the array.
     * @param numberArray The array of numbers.
     * @return The maximum number.
     */
    public static double findBestEmployee(double[] numberArray) {
        double temp;
        double maxFound = 0;
        int floor = (int) (numberArray.length / Math.E); // create sample size

        for(int i=0; i<floor; i++) { // find max value of the sample size
            temp = numberArray[i];
            if(temp > maxFound){
                maxFound = temp;
            }
        }

        for(int i = floor; i<numberArray.length; i++){ // compare employees and find best
            temp = numberArray[i];
            if(temp>maxFound){
                break;
            }
        }
        return maxFound;
    }
}