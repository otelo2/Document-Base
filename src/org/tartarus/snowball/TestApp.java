package org.tartarus.snowball;

import java.lang.reflect.Method;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class TestApp {

    //If the Application is run incorrectly (no input on console)
    private static void usage()
    {
        //Print the correct usage format
        System.err.println("Usage: TestApp <algorithm> [<input file>] [-o <output file>]");
    }

    //If the Application is run correctly (input on console)
    public static void main(String [] args) throws Throwable {
        //If there is not enough information (at least the information about language and input file)
        //There is no point in validating the strings
        if (args.length < 2) {
            //Call usage
            usage();
            return;
        }
        
        //---   String Validation
        //---   <algorithm>
        //If the input seems right, the class for the language stemmer is created
        //The ClassName is constructed using the first string on the argument <algorithm>
        Class stemClass = Class.forName("org.tartarus.snowball.ext." + args[0] + "Stemmer");
	//Direct declaration
        //Class stemClass = Class.forName("org.tartarus.snowball.englishStemmer");
        //Create an instance of the class
        SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

        //---   String Validation
        //--    [<input file>]
	int arg = 1;
	InputStream instream;
	//To select the name of the file that will be read
        if (args.length > arg && !args[arg].equals("-o")) {
	    instream = new FileInputStream(args[arg++]);
	} 
        //To use the string from console (?)
        else {
	    instream = System.in;
	}
        
        //---   String Validation
        //---   [-o <output file>]
        OutputStream outstream;
	if (args.length > arg) {
            //If there is no name for the output file
            if (args.length != arg + 2 || !args[arg].equals("-o")) {
                //Call usage
                usage();
                return;
            }
            //If there is a name, use that one as the output file name
	    outstream = new FileOutputStream(args[arg + 1], true);
	} 
        //To print the string on console (?)
        else {
	    outstream = System.out;
	}

        //Create a reader from the input file
	Reader reader = new InputStreamReader(instream, StandardCharsets.UTF_8);
	reader = new BufferedReader(reader);
        
        //Create a writer to the output file
	Writer output = new OutputStreamWriter(outstream, StandardCharsets.UTF_8);
	output = new BufferedWriter(output);

	StringBuffer input = new StringBuffer();
	int character;
        int count = 0;
	//while the buffer has information
        //Read the next byte of data
        while (count==0){//To read the last string of the document((character = reader.read()) != -1) {
            character = reader.read();
            //Convert the byte into a character
            char ch = (char) character;
            //To read the last string of the document
            if(character == -1)
            {
                input.setCharAt(input.length()-1, ' ');
                ch = (char) ' ';
                count++;
            }
            //if there is a whitespace the word has finished
            //the word is sent to the stemmer and processed
            //the result of this process is written on the output file
	    if (Character.isWhitespace(ch)) {
                stemmer.setCurrent(input.toString());
		stemmer.stem();
		output.write(stemmer.getCurrent());
                //to delete blank spaces left by the prepositions
                if(stemmer.getCurrent() != null && !stemmer.getCurrent().trim().isEmpty())
                {
                    output.write('\n');   
                }
		input.delete(0, input.length());
	    } 
            //if the word has not finished yet
            else {
		//concatenate the next letter in a StringBuffer
                //delete UpperCase letters
                input.append(ch < 127 ? Character.toLowerCase(ch) : ch);
	    }
	}

        //Empty Buffers
	output.flush();
    }
}
