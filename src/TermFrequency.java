import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    //Creates a frequency matrix out of the terms in the file.
    //TODO: Change the void into the correct type
    public static void calculateFrequency(String fileNum) throws FileNotFoundException, IOException
    {
        String line, word = "";    
        int count = 0, maxCount = 0;    
        ArrayList<String> words = new ArrayList<String>();    
            
        //Opens file in read mode    
        filename="english "+fileNum+".txt -o output"+fileNum+".txt";
        FileReader file = new FileReader(filename);    
        BufferedReader br = new BufferedReader(file);    
            
        //Reads each line    
        while((line = br.readLine()) != null) {    
            String string[] = line.toLowerCase().split("\n");    
            //Adding all words generated in previous step into words    
            for(String s : string){    
                words.add(s);    
            }    
        }    
            
        //Determine the most repeated word in a file    
        for(int i = 0; i < words.size(); i++){    
            count = 1;    
            //Count each word in the file and store it in variable count    
            for(int j = i+1; j < words.size(); j++){    
                if(words.get(i).equals(words.get(j))){    
                    count++;    
                }     
            }    
            System.out.println(word + " " + count);
        }    
        br.close();    
    }
    
    
}