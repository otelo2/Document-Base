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
        
        //3. Matrix creation
        
        //4. Matrix transformation
        
        //5. Load the table to MySQL
        
        //6. Make the query
        
        
    }
    
}
