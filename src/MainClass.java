/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.tartarus.snowball.*;
import java.util.Scanner;

/**
 *
 * @author ferfl
 */
public class MainClass {
    
    //CHANGE THE term number!!
    static int termno = 3;
    static String query, temp = new String();
    static String title1, title2 = new String();
            
    public static void readFile(String file) throws Throwable
    {
        TestApp lemmatizer = new TestApp();
        String filename = new String();
        String[] fileArray = null;
        filename="english "+file+".txt -o output"+file+".txt";

        fileArray = filename.split(" ");
        
        lemmatizer.Lemmatization(fileArray);
        
        System.out.println(filename+"\t\tDONE");
    }
    
    private static void EuclideanDistanceCompare() 
    {
        //Dissimilarity
        /*
        //create the string as a cycle
        select SQRT(pow((f.term1-d.term1),2) + pow((f.term2-d.term2),2) + pow((f.term3-d.term3),2)) as dissimilarityDegree 
        from freqtt as f, freqtt as d
        where f.title="title1" and d.title="title3";
        */
        //change the table names and attributes
        query = "select SQRT(";
        for(int i=0; i<termno;i++)
        {
            temp = "pow((f.term"+(i+1)+"-d.term"+(i+1)+"),2)";
            if(i!=termno-1)
            {
                temp = temp.concat(" + ");
            }
            query = query.concat(temp);
        }
        query=query.concat(") as dissimilarityDegree from freqtt as f, freqtt as d ");
        query=query.concat("where f.title=\"");
        query=query.concat(title1);
        query=query.concat("\" and d.title=\"");
        query=query.concat(title2);
        query=query.concat("\";");
        //System.out.println(query);
        System.out.println("\nEuclidean distance between "+title1+" and "+title2+":");
        QuerySQL q1 = new QuerySQL(query);
    }
    
    private static void InnerProdCompare() 
    {
        //change the table names and attributes
        query = "select ";
        for(int i=0; i<termno;i++)
        {
            temp = "(f.term"+(i+1)+"*d.term"+(i+1)+")";
            if(i!=termno-1)
            {
                temp = temp.concat(" + ");
            }
            query = query.concat(temp);
        }
        query=query.concat(" as similarityDegree from freqtt as f, freqtt as d ");
        query=query.concat("where f.title=\"");
        query=query.concat(title1);
        query=query.concat("\" and d.title=\"");
        query=query.concat(title2);
        query=query.concat("\";");
        //System.out.println(query);
        System.out.println("Internal product between "+title1+" and "+title2+":");
        QuerySQL q1 = new QuerySQL(query);
    }
    
    private static void CosineCompare() 
    {
        String temp1, temp2, temp3 = new String();
        temp1 = "SQRT(";
        temp2 = "SQRT(";
        //change the table names and attributes
        query = "select temporal.innp/(temporal.norm1*temporal.norm2) as cosine from (select ";
        for(int i=0; i<termno;i++)
        {
            temp = "(f.term"+(i+1)+"*d.term"+(i+1)+")";
            temp1 = temp1.concat("(pow(f.term"+(i+1)+", 2))");
            temp2 = temp2.concat("(pow(d.term"+(i+1)+", 2))");
            if(i!=termno-1)
            {
                temp = temp.concat(" + ");
                temp1 = temp1.concat(" + ");
                temp2 = temp2.concat(" + ");
            }
        }
        temp1 = temp1.concat(") as norm1,");
        temp2 = temp2.concat(") as norm2, ");
        temp3 = temp1+temp2;
        query=query.concat(temp3);
        query = query.concat(temp);
        query=query.concat(" as innp from freqtt as f, freqtt as d ");
        query=query.concat("where f.title=\"");
        query=query.concat(title1);
        query=query.concat("\" and d.title=\"");
        query=query.concat(title2);
        query=query.concat("\") as temporal;");
        //System.out.println(query);
        System.out.println("Cosine between "+title1+" and "+title2+":");
        QuerySQL q1 = new QuerySQL(query);
    }
    
    private static void MixedCompare() 
    {
        EuclideanDistanceCompare();
        InnerProdCompare();
        CosineCompare();
    }
    
    public static void printMenu()
    {
        Scanner in = new Scanner(System.in);
        int election = 0, election1 = 0;
        do
        {
            System.out.println();
            System.out.println("1. Compare to documents");
            System.out.println("2. Give a query and retrieve the most relevant documents");
            System.out.println("3. EXIT");
            election1 = in.nextInt();
            in.nextLine();
            do
            {
                switch(election1)
                {
                    case 1:
                        System.out.println();
                        
                        //Ask for the name of the documents
                        //Show the information of the documents
                        System.out.println("Here is the information of the documents, use it to make your comparisson!");
                        QuerySQL q1 = new QuerySQL("select * from documentInformation;");

                        System.out.println("ID of the first document:");
                        title1 = in.nextLine();
                        System.out.println("ID of the second document:");
                        title2 = in.nextLine();
                        System.out.println();
                        
                        System.out.println("Choose a function to compare the documents:");
                        System.out.println("1. Euclidean Distance");
                        System.out.println("2. Inner Product");
                        System.out.println("3. Cosines");
                        System.out.println("4. All of the above to compare");
                        System.out.println("5. Abort operation");
                        election = in.nextInt();
                        in.nextLine();
                        switch(election)
                        {
                            case 1:
                                EuclideanDistanceCompare();
                                election = election * 5;                                
                                break;
                            case 2:
                                InnerProdCompare();
                                election = election * 5;
                                break;                                
                            case 3:
                                CosineCompare();
                                election = election * 5;
                                break;
                            case 4:
                                MixedCompare();
                                election = election * 5;
                                break;
                            case 5: 
                                election = 48;
                                System.out.println("Okay, would you like to realize another operation? Here are the valid options");
                                break;
                            default:
                                election = 0;
                                System.out.println("Oops, it was an invalid option. Try again, please!");
                                break;
                        }
                        break;
                    case 2:
                        System.out.println();
                        System.out.println("Choose a function for the query:");
                        System.out.println("1. Euclidean Distance");
                        System.out.println("2. Inner Product");
                        System.out.println("3. Cosines");
                        System.out.println("4. Abort operation");
                        election = in.nextInt();
                        in.nextLine();
                        switch(election)
                        {
                            case 1:
                                election = election * 6;
                                break;
                            case 2:
                                election = election * 6;
                                break;
                            case 3:
                                election = election * 6;
                                break;
                            case 4: 
                                election = 48;
                                break;
                            default:
                                election = 0;
                                System.out.println("Oops, it was an invalid option. Try again, please!");
                                break;
                        }
                        break;
                    case 3:
                        election = 58;
                        break;
                    default:
                        election = 48;
                        System.out.println("Oops, it was an invalid option. Try again, please!");
                        break;
                }
                if((election!=58)&&(election!=48)&&(election!=0))
                {
                    System.out.println("SUCCESS! Let's get to a new comparisson");                            
                    System.out.println("If you want to get out, leave the titles on blank and enter 5 after the menu.");                            
                }
                //System.out.println("election1"+election1+"\t election"+election);
            }while ((election != 58) && (election != 48));
        
        }while(election == 48);// || (election1 ==0));
        System.out.println(election);
        //return election;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable
    {
        //1. Lemmatization of the documents
        System.out.println("Lemmatization");
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
        System.out.println();
        
        System.out.println("Count Frequency");
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
        System.out.println();
        
        System.out.println("Matrix Creation");
        //3. Matrix creation
        
        System.out.println();
        
        System.out.println("Matrix Transformation");
        //4. Matrix transformation
        LSI transformation;
        transformation = new LSI();
        //LSI.LSITransformation(test);
        LSI.LSITransformation();
        
        System.out.println();
        
        System.out.println("Load to MySQL");
        //5. Load the table to MySQL
   
        System.out.println();
        
        System.out.println("Query time");
        //PRINT THE MENU
        printMenu();
        
        //6. Make the query     
        //A. Using a query
        //Euclidean distance
        //Dissimilarity
        /*select title, 
        SQRT(pow((f.term1-q.term1),2) + pow((f.term2-q.term2),2) + pow((f.term3-q.term3),2)) as euc 
        from freqtt as f, query as q
        order by euc;*/
        //Similarity
        
        //B. Compare two documents.
        termno = 3;
        title1 = "title1";
        title2 = "title3";
        
    }
    
}
