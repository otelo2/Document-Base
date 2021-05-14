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
    
    public static int number=0, ColumnDimension, RowDimension, kvalue;
    public static double[][] MatrixSD, MatrixT;
    
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
    
    public static double[][] ordermulMatrix(double[][] A, double[][] B, int row1, int column1, int column2)
    {
        double[][] C = new double[row1][column2];
        for(int i=0; i<row1;i++)
        {
            for(int j=0;j<column2;j++)
            {
                for(int k=0;k<column1;k++)
                {
                    C[i][j]=C[i][j]+(A[i][k]*B[k][j]);
                }
            }
        }
        return C;
    }
    
    public static double[][] cut(Matrix matrix, int k, int r)
    {
        
        double cMatrix[][], aMatrix[][];
        aMatrix = matrix.getArrayCopy();
        cMatrix = new double[k][r];
        for(int i=0; i<k; i++)
        {
            for(int j=0; j<r ; j++)
            {
                cMatrix[i][j] = aMatrix[i][j];
            }
        }
        return cMatrix;
    }

    public static void printMatrix(double a[][])
    {
        for(int i=0; i<a.length; i++)
        {
            for(int j=0;j<a[0].length;j++)
            {
                System.out.printf("%2.2f\t", a[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static void LSITransformation(double test[][]) 
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
        //test = randomMatrix(test);
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
        double[][] T = new double[C.getRowDimension()][k];
        T= cut(E, C.getRowDimension(), k);
        printMatrix(T);
        MatrixT=T;
        RowDimension=C.getRowDimension();
        System.out.println("S*");     
        double[][] S = new double[k][k];                  
        S=cut(D, k, k);
        printMatrix(S);
        System.out.println("D\u1d40*"); //unicode for the T superscript
        double[][] D1 = new double[k][C.getColumnDimension()];
        D1=cut(F, k, C.getColumnDimension());
        ColumnDimension=C.getColumnDimension();
        kvalue=k;
        printMatrix(D1);//*/
        double [][] t1 = ordermulMatrix(S, D1, k, k, number=C.getColumnDimension());
        printMatrix(ordermulMatrix(ordermulMatrix(T, S, C.getRowDimension(), k, k), D1, C.getRowDimension(), k, C.getColumnDimension()));
        printMatrix(t1);
        MatrixSD=t1;
        
        //Multiply Matrices to get FreqT*
        
        
        double[][] q = {{5.0}, {6.0}, {0.0}, {8.0}, {1.0}, {2.0}, {0.0}, {0.0}};
        //printMatrix(nRelevant(3, C.getArrayCopy() , q));
        
        /*Matrix query = new Matrix(q);
        q = query.svd().getU().getArrayCopy();
        printMatrix(q);
        printMatrix(q=cut(query.svd().getU(), k, r));
        
        printMatrix(nRelevant(3, cut(E, k, r), q));*/
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