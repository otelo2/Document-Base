/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Jama.*;
import java.util.Arrays;

/**
 *
 * @author ferfl
 */
public class LSI {
    
    //Diferencia entre ambos documentos
    public static double euclidean(double a[], double b[])
    {
        double similarity = 0.0;
        //System.out.println(a.length+"\t"+b.length);
        if(a.length==b.length)
        {
            for(int i=0; i<a.length-1;i++)
            {
                similarity = similarity + ((a[i]-b[i])*(a[i]-b[i]));
            }
        }
        else
        {
            similarity = 0;
        }
        similarity = Math.sqrt(similarity);
        return similarity;
    }
    
    public static double[][] nRelevant(int n, double freqt[][], double q[][])
    {
        double[][] relevant = new double[n][2];
        double[][] similarity = new double[freqt[0].length][2];
        double [] query = new double[freqt[0].length];
        for(int i=0;i<freqt[0].length-1;i++)
        {
            query[i] = q[i][0];
        }
        for(int j=0; j<freqt[0].length;j++)
        {
            //puede estar tomando mal la sección de la matriz
            similarity[j][0] = j;
            similarity[j][1] = euclidean(freqt[j], query);
        }
        printMatrix(similarity);
        BubbleSort(similarity);
        for (int i = 0; i < n; i++)
        {
            relevant[i][0] = similarity[i][0];
            relevant[i][1] = similarity[i][1];
        }
        return relevant;
    }
    
    //https://www.researchgate.net/publication/228930026_Finding_the_Optimal_Rank_for_LSI_Models
    public static int findCriticalK(double s[][])
    {
        //recommended value
        //double thresholdValue = 0.001;
        int i;
        double thresholdValue = 3;
        //busca el lugar en la diagonal donde el cambio entre los valores deja de ser grande
        for(i=s.length-1; i>0; i--)
        {
            if(Math.abs(s[i][i]-s[i-1][i-1]) < thresholdValue)
            {
                System.out.println("K" +(s[i][i]-s[i-1][i-1])+ "s.lenght"+i);
                return i;
            }
        }
        return s.length-1;
    }
    
    public static double[][] cut(Matrix matrix, int k, int r)
    {
        
        double cMatrix[][], aMatrix[][];
        aMatrix = matrix.getArrayCopy();
        if((matrix.getColumnDimension()==r)&&(matrix.getRowDimension()==r))
        {
            //column [k][k]
            cMatrix = new double[k][k];
            for(int i=0; i<k; i++)
            {
                for(int j=0; j<k ; j++)
                {
                    cMatrix[i][j] = aMatrix[i][j];
                }
            }
            return cMatrix;
        }
        else if((matrix.getColumnDimension()==r)&&(matrix.getRowDimension()!=r))
        {
            //column [][k]
            cMatrix = new double[matrix.getRowDimension()][k];
            for(int i=0; i<matrix.getRowDimension(); i++)
            {
                for(int j=0; j<k ; j++)
                {
                    cMatrix[i][j] = aMatrix[i][j];
                }
            }
            return cMatrix;
        }
        else if((matrix.getColumnDimension()!=r)&&(matrix.getRowDimension()==r))
        {
            //row [k][]
            cMatrix = new double[k][matrix.getColumnDimension()];
            for(int i=0; i<k; i++)
            {
                for(int j=0; j<matrix.getColumnDimension(); j++)
                {
                    cMatrix[i][j] = aMatrix[i][j];
                }
            }
            return cMatrix;
        }
        else
            return null;
    }
    
    public static double[][] randomMatrix(double matriz[][])
    {
        //to generate an sparse matrix
        for (int x=0; x < matriz.length; x++) 
        {
            for (int y=0; y < matriz[x].length; y++)
            {
                if(((Math.random()*100+1)) > 50)
                    matriz[x][y] = (int) (Math.random()*50+1);
                else
                    matriz[x][y] = 0;
            }
        }
        return matriz;
    }

    public static void printMatrix(double a[][])
    {
        System.out.println();
        for(int i=0; i<a.length; i++)
        {
            for(int j=0;j<a[i].length;j++)
            {
                System.out.printf("%2.2f\t", a[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static void LSITransformation(double test[][])//double test[][]) 
        {
        int k, r;
        
        //JAVA is ROW FIRST
        
        /*//----      JAMA TEST
        //Test Values: https://web.mit.edu/be.400/www/SVD/Singular_Value_Decomposition.htm#:~:text=The%20SVD%20represents%20an%20expansion,up%20the%20columns%20of%20U.
        //Checked
        double[][] test = {{2.,4.},{1.,3.},{0.,0.},{0.,0.}};
        //double[][] test = {{2.},{1.},{0.},{0.}};
        Matrix C = new Matrix(test);
        System.out.println("Original Matrix");
        //C.print(2, 0);
        printMatrix(C.getArrayCopy());
        
        Matrix E = C.svd().getU();
        //E.print(Math.min(C.getRowDimension(),C.getColumnDimension()), 0);
        System.out.println('T');
        printMatrix(E.getArrayCopy()); //puntos decimales

        //Los resultados de la matriz están redondeados hacia abajo 
        Matrix D = C.svd().getS();
        r = D.getColumnDimension();
        //D.print(C.getColumnDimension(), 0);
        System.out.println('S');
        printMatrix(D.getArrayCopy()); //puntos decimales
        //NOTE: S non-increasing

        Matrix F = C.svd().getV();
        //F.print(C.getColumnDimension(), 0);
        System.out.println("D\u1d40"); //unicode for the T superscript
        printMatrix(F.getArrayCopy()); //puntos decimales
        
        */
        
        //----      RANDOM MATRIX
        //ERROR: [m][n] m<n por la definición de la función SVD
        //[documentos][terminos]
        //double[][] test = new double[10][8];
        test = randomMatrix(test);
        Matrix C = new Matrix(test);
        System.out.println("Original Matrix");
        printMatrix(C.getArrayCopy());
        
        Matrix E = C.svd().getU();
        System.out.println('T');
        printMatrix(E.getArrayCopy()); //puntos decimales

        //Los resultados de la matriz están redondeados hacia abajo 
        Matrix D = C.svd().getS();
        r = D.getColumnDimension();
        System.out.println('S');
        printMatrix(D.getArrayCopy()); //puntos decimales
        //NOTE: S non-increasing

        Matrix F = C.svd().getV();
        System.out.println("D\u1d40"); //unicode for the T superscript
        printMatrix(F.getArrayCopy()); //puntos decimales
       
        //get Critical K
        k = findCriticalK(D.getArrayCopy());
        System.out.println("__________________________________________________________________________________________");
        System.out.println("Critical k="+k);
        
        //Cut a Matrix
        System.out.println("T*");        
        printMatrix(cut(E, k, r));
        System.out.println("S*");       
        printMatrix(cut(D, k, r));
        System.out.println("D\u1d40*"); //unicode for the T superscript
        printMatrix(cut(F, k, r));//*/
        
        double[][] q = {{5.0}, {6.0}, {0.0}, {8.0}, {1.0}, {2.0}, {0.0}, {0.0}};
        //printMatrix(nRelevant(3, C.getArrayCopy() , q));
        
        Matrix query = new Matrix(q);
        q = query.svd().getU().getArrayCopy();
        printMatrix(q);
        printMatrix(q=cut(query.svd().getU(), k, r));
        
        printMatrix(nRelevant(3, cut(E, k, r), q));
        //System.out.println(nRelevant(3, cut(F, k, r), q).length);
    }

    public static void BubbleSort(double[][] similarity) 
    {
        int n = similarity.length;
        for (int i = 0; i < n-1; i++)
        {
            for (int j = 0; j < n-i-1; j++)
            {
                if (similarity[j][1] > similarity[j+1][1])
                {
                    // swap arr[j+1] and arr[j]
                    double temp = similarity[j][0];
                    similarity[j][0] = similarity[j+1][0];
                    similarity[j+1][0] = temp;
                    temp = similarity[j][1];
                    similarity[j][1] = similarity[j+1][1];
                    similarity[j+1][1] = temp;
        
                }
            }
        }
    }
}