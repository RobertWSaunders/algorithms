import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Algorithms Lab 3 - UAV Image Classification Problem
// Robert Saunders, 10194030, 15rws

public class UAVImageSearch {

    private static int numImages = 10; // the number of interesting images to save
    private static String images_path = "images.csv"; // path to image bit dataset
    private static String coords_path = "coords.csv"; // path to coords dataset
    private static ArrayList<String> images; // array to hold the original set of images and later re-assigned for interesting images
    private static ArrayList<String> coords; // array to hold the original set of coordinates and late re-assigned for interesting images coordinates
    private static ArrayList<Integer> imageIndexes; // array to hold the indexes of the images we are interested in.

    /**
     * Main execution method of the program.
     * @param args Any arguments to pass into the main method.
     */
    public static void main(String[] args){

        // fetch the entire image data
        images = fetchData(images_path);
        // fetch the coordinates that correspond to images
        coords = fetchData(coords_path);
        // array to store the indexes of the images that are chosen to keep
        imageIndexes = new ArrayList<>();

        // re-assign the images array to the images that we are interested in
        images = getInterestingImages(numImages);
        // re-assign the coords array to only have the coordinates of the images we care about
        coords = getInterestingImagesCoordinates();

        //print out the interesting images and their coordinates

        System.out.println("Below are the " + numImages + " most interesting images and their coordinates:");

        for (int i = 0; i < images.size(); i++) {
            System.out.println("Image: " + images.get(i) + " Coordinate: " + coords.get(i));
        }
    }

    /**
     * Retrieves the interesting images from the dataset.
     * See further explanation and visual in lab report.
     * @param numImages The number of interesting images to retrieve.
     * @return Array of the interesting images as a result of algorithm.
     */
    public static ArrayList<String> getInterestingImages(int numImages) {

        numImages = numImages/2; // algorithm takes two elements from every group so divide by 2

        ArrayList<String> bestImages = new ArrayList<String>(); // array to store the best images

        int numGroups = (images.size()/numImages); // calculate the number of groups to split dataset
        int beginningIndex = 0; // flag to keep track of beginning index
        int endingIndex = numGroups-1; // flag to keep track of ending index

        for (int i = 0; i < numImages; i++) { // iterate k/2 times for every group

            List<String> group = images.subList(beginningIndex, endingIndex); // create the group

            int maxHammingDistance = 0; // flag to store maximum hamming distance so far
            String imageOne = ""; // flag to store first best image from group
            String imageTwo = ""; // flag to store second best image from group
            int indexOne = 0; // flag to store index of first best image
            int indexTwo = 0; // flag to store index of second best image

            // compare every element in the group and find the two images that have the biggest hamming distance, these are most interesting
            for (int j = 0; j < group.size(); j++) {
                for (int k = j + 1; k < group.size(); k++) {
                    if (group.get(j) != group.get(k)) {
                        int hammingDistance = getHammingDistance(group.get(j), group.get(k));
                        if (hammingDistance > maxHammingDistance) { // check if this is the best hamming distance

                            maxHammingDistance = hammingDistance; // assign the new maximum hamming distance

                            imageOne = group.get(j); // set image flags to currently selected images
                            imageTwo = group.get(k);

                            indexOne = j + beginningIndex; // update the image indexes with the correct index of interesting images from original collection of images
                            indexTwo = k + beginningIndex;
                        }
                    }
                }
            }

            // add the group two best images from group into array list.
            bestImages.add(imageOne);
            bestImages.add(imageTwo);

            // add the indexes of the best images from the group to the imageIndexes array list.
            imageIndexes.add(indexOne);
            imageIndexes.add(indexTwo);

            // update the indexes for the next group
            beginningIndex = endingIndex + 1;
            endingIndex = beginningIndex + numGroups-1;
        }

        return bestImages; // return the best images
    }

    /**
     * Reduces the coordinates array to the coordinates we care about.
     * i.e. the coordinates of the interesting images
     * @return An array of interesting coordinates.
     */
    public static ArrayList<String> getInterestingImagesCoordinates() {
        ArrayList<String> interestingCoordinates = new ArrayList<String>();

        for (int i = 0; i < imageIndexes.size(); i++) {
            interestingCoordinates.add(coords.get(imageIndexes.get(i)));
        }

        return interestingCoordinates;
    }

    /**
     * Retrieves the hamming distance between two strings.
     * NOTE: the strings must be the same length.
     * @param stringOne The first string to compare with.
     * @param stringTwo The second string to compare with.
     * @return The humming distance between the strings.
     */
    public static int getHammingDistance(String stringOne, String stringTwo) {
        // if the strings are  not the same length then cannot compare
        if (stringOne.length() != stringTwo.length())
            return -1;

        int counter = 0; // counter to count differences in strings

        for (int i = 0; i < stringOne.length(); i++) { // iterate through characters and count differences
            if (stringOne.charAt(i) != stringTwo.charAt(i)) counter++;
        }

        return counter;
    }


    /**
     * Fetches the data from the csv.
     * @param path The path to the file with the numbers in.
     * @return An array of numbers from the csv file.
     */
    public static ArrayList<String> fetchData(String path) {
        String line;
        ArrayList<String> data = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split("\n");
                data.add(values[0]);
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
}