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
 */
public class TermFrequency 
{
    //Create variables for the array lists
    ArrayList<String> terms = new ArrayList<String>();
    ArrayList<Double> frequency = new ArrayList<Double>();
    
    //Creates a frequency matrix out of the terms in the file.
    //TODO: Change the void into the correct type
    public static void calculateFrequency(String fileNum) throws FileNotFoundException, IOException
    {
        String line, word = "";    
        int count = 0;
        ArrayList<String> words = new ArrayList<String>();    
            
        //Opens file in read mode    
        String filename="output"+fileNum+".txt";
        System.out.print("Starting to read file: " + filename);
        FileReader file = new FileReader(filename);    
        BufferedReader br = new BufferedReader(file);
                
        //For the output of the terms
        OutputStream outstream;
        outstream = new FileOutputStream("terms"+fileNum);
        Writer outputT = new OutputStreamWriter(outstream, StandardCharsets.UTF_8);
	outputT = new BufferedWriter(outputT);
        
        //For the output of the terms frequency
        OutputStream outstream1;
        outstream1 = new FileOutputStream("freq"+fileNum);
        Writer outputF = new OutputStreamWriter(outstream1, StandardCharsets.UTF_8);
	outputF = new BufferedWriter(outputF);
            
        //Reads each line    
        while((line = br.readLine()) != null) {    
            String string[] = line.toLowerCase().split("\n");    
            //Adding all words generated in previous step into words    
            for(String s : string){    
                words.add(s);    
            }    
        }    
        
        System.out.println("\tSize:" + words.size());
        //Determine the most repeated word in a file    
        for(int i = 0; i < words.size(); i++){    
            count = 1;    
            //Count each word in the file and store it in variable count    
            for(int j = i+1; j < words.size(); j++){    
                if(words.get(i).equals(words.get(j))){    
                    count++; 
                    words.remove(j);
                }     
            }
            //Build the frequency result for the current word
            String result = words.get(i) + " " + Integer.toString(count);
            //System.out.println(result);
            //Write que frequency column to a file
            outputT.write(words.get(i));
            outputT.write("\n");
            outputF.write(Integer.toString(count));
            outputF.write("\n");
        }    
        br.close();
        outputT.flush();
        outputF.flush();
    }
}
