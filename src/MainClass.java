/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.tartarus.snowball.*;

/**
 *
 * @author ferfl
 */
public class MainClass {
    
    public static void readFile(String file) throws Throwable
    {
        TestApp lemmatizer = new TestApp();
        String filename = new String();
        String[] fileArray = null;
        filename="english "+file+".txt -o output"+file+".txt";
        fileArray = filename.split(" ");
        lemmatizer.Lemmatization(fileArray);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable
    {
        //1. Lemmatization of the documents
        readFile("1");
        readFile("2");
        readFile("3");
        /*readFile("4");
        readFile("5");
        readFile("6");
        readFile("7");
        readFile("8");
        readFile("9");
        readFile("10");*/
        
        //2. Count the frequency of the term in each outputX file
        TermFrequency.calculateFrequency("1");
        TermFrequency.calculateFrequency("2");
        TermFrequency.calculateFrequency("3");
        /*TermFrequency.calculateFrequency("4");
        TermFrequency.calculateFrequency("5");
        TermFrequency.calculateFrequency("6");
        TermFrequency.calculateFrequency("7");
        TermFrequency.calculateFrequency("8");
        TermFrequency.calculateFrequency("9");
        TermFrequency.calculateFrequency("10");*/

        //3. Matrix creation
        double[][] matrix = MatrixCreator.createMatrix(3); //Argument is the amount of files we have
        
        //4. Matrix transformation
        LSI transformation;
        transformation = new LSI();
        //LSI.LSITransformation(test);
        LSI.LSITransformation(matrix);
        
        //5. Load the table to MySQL
        
        //6. Make the query        
        
    }
    
}
