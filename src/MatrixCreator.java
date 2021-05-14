import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Otelo
 * TODO: Print the final term list
 */
public class MatrixCreator {
    
    public static double[][] createMatrix(int numOfFiles) throws FileNotFoundException, IOException
    {
        //Dynamic arrays
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<String> frequency = new ArrayList<String>();
        ArrayList<String> index = new ArrayList<String>();
        
        //Creation of the frequency matrix
        int rows = getRows(numOfFiles);
        double[][] tempFreqT = new double[rows][numOfFiles];
        
        //Initialize matrix with zeroes
        for(int i=0; i<rows;i++)
        {
            for(int j=0; j<numOfFiles; j++)
            {
                tempFreqT[i][j] = 0;
            }
        }
        
        int rowIndex = 0;
        for (int fileNum = 1; fileNum<=numOfFiles; fileNum++)
        {
            //Declare initial variables for each file
            String line1, line2, word = "";
            int count;
            
            //Opens terms file in read mode    
            String filename="terms"+fileNum;
            //System.out.println("Starting to read file: " + filename);
            FileReader fileTerms = new FileReader(filename);    
            BufferedReader brTerms = new BufferedReader(fileTerms);
            
            //Opens frequency file in read mode    
            String filenameF="freq"+fileNum;
            //System.out.println("Starting to read file: " + filenameF);
            FileReader fileFreq = new FileReader(filenameF);    
            BufferedReader brFreq = new BufferedReader(fileFreq);
            
            //Reads each term    
            while((line1 = brTerms.readLine()) != null) 
            {    
                String string[] = line1.toLowerCase().split("\n");    
                //Adding all words generated in previous step into words    
                for(String s : string)
                {    
                    words.add(s);    
                }    
            }
            
            //Reads each frequency    
            while((line2 = brFreq.readLine()) != null) 
            {    
                String string[] = line2.toLowerCase().split("\n");    
                //Adding all words generated in previous step into words    
                for(String s : string)
                {    
                    frequency.add(s);    
                }    
            }
            
            //Place the frequency in the temp freqT
            for(rowIndex = rowIndex; rowIndex < words.size(); rowIndex++)
            {    
                //Add the frequency value to the matrix at its corresponding column
                tempFreqT[rowIndex][fileNum-1] = Float.parseFloat(frequency.get(rowIndex));
            }    
            brFreq.close();
            brTerms.close();
            
            //Find duplicate terms
            for(int i=0; i<words.size();i++)
            {
                for(int j=i+1; j<words.size(); j++)
                {
                    //If the term is in more than one document, save the location of the original term and the duplicate terms
                    if(words.get(i).equals(words.get(j)))
                    {    
                        //0.Index of original. k-1
                        index.add(Integer.toString(i));
                        //1.Index of duplicate. k
                        index.add(Integer.toString(j));
                        //2.Current document (Column). k+1
                        index.add(Integer.toString(fileNum-1));
                        //3.Get the frequency. k+2
                        index.add(frequency.get(j));
                        System.out.println("Frequency of word " + words.get(i) + " in document " + fileNum + " is " + frequency.get(j) + " with i as: "+ i +" and j as: "+ j);
                        //Remove duplicate word from list
                        words.remove(j);
                        frequency.remove(j);
                        //j--;
                    } 
                }
            }
        } 
        
        //The rows that the final frequency matrix must have
        int finalRows = rows - index.size()/4;
        //Create the variable for the final frequency matrix
        double[][] finalFreqT = new double[finalRows][numOfFiles];
        
        for(int i=0;i<finalRows;i++)
        {
            //Loop the index list to find duplicate terms
            for(int k=1;k<index.size();k=k+4)
            {
                //We are in a duplicate row
                if(i==Integer.parseInt(index.get(k)))
                {
                    //[destination row][column] = frequency in frequency array with index k
                    finalFreqT[Integer.parseInt(index.get(k-1))][Integer.parseInt(index.get(k+1))] = Double.parseDouble(index.get(k+2));
                }
                //Not in a duplicate, copy freqT
                else
                {
                    for(int z=0; z<numOfFiles; z++)
                        {
                            finalFreqT[i][z] = tempFreqT[i][z];
                        }
                }
            }
            
        }
        
        //Print the final frequency matrix
        //LSI.printMatrix(finalFreqT);
        MatrixCreator.showFreqT(finalFreqT, words, Boolean.TRUE);
        System.out.println("Final frequency Matrix of: " + finalRows + " x " + numOfFiles);
        
        return finalFreqT;
    }
    
    //Calculates how many terms there are so we can create the matrix accordingly
    private static int getRows(int numOfFiles) throws FileNotFoundException, IOException
    {
        int rows = 0;
        ArrayList<String> words = new ArrayList<String>();
        
        for (int fileNum = 1; fileNum<=numOfFiles; fileNum++)
        {
            //Declare initial variables for each file
            String line, word = "";
            int count;
            
            //Opens terms file in read mode    
            String filename="terms"+fileNum;
            //System.out.println(filename);
            FileReader fileTerms = new FileReader(filename);    
            BufferedReader brTerms = new BufferedReader(fileTerms);
            
            //Reads each term    
            while((line = brTerms.readLine()) != null) 
            {    
                String string[] = line.toLowerCase().split("\n");    
                //Adding all words generated in previous step into words    
                for(String s : string)
                {    
                    words.add(s);    
                }    
            }
        }
        return words.size();
    }
    
    private static void showFreqT(double[][] freqT, ArrayList<String> words, Boolean toFile) throws FileNotFoundException, IOException
    {
        //For the output of the terms
        OutputStream outstream;
        outstream = new FileOutputStream("FrequencyMatrix.txt");
        Writer output = new OutputStreamWriter(outstream, StandardCharsets.UTF_8);
	output = new BufferedWriter(output);
        
        //Write the frequency matrix to the screen
        System.out.println();
        for(int i=0; i<freqT.length; i++)
        {
            System.out.printf(words.get(i) + "\t");
            for(int j=0;j<freqT[i].length;j++)
            {
                System.out.printf("%2.2f\t", freqT[i][j]);
            }
            System.out.println();
        }
        
        if(toFile)
        {
            output.write("\n");
            for(int i=0; i<freqT.length; i++)
            {
                output.write(words.get(i));
                for(int j=0;j<freqT[i].length;j++)
                {
                    output.write(String.valueOf(freqT[i][j]));
                }
                System.out.println();
            }
            output.write("\n");
        }
        
        output.flush();
    }
}