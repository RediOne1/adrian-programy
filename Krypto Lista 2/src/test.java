import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 

public class test {
	 public static void start_test(List<Kryptogram> _kryptogramy) throws Exception {
			List <Integer> szyfry = new ArrayList <Integer>();// tab szyfrow
			//File file = new File("kryptogramy.txt");
			//Scanner in = new Scanner(file);
			String temporary[] = new String[17];
			int i,j,max;
			//for( i=0;i<17;i++)	{
			//	temporary[i] = in.nextLine(); 
				// Kazdy kryptogram to element tej tablicy
				//System.out.println(temporary[i]);
			//}
			i = 0; j = 0;
			String tmp[];
			int kryptogramy[][] = new int [17][256];
			for ( i = 0; i < 17; i ++)	{
				for ( j = 0; j <_kryptogramy.get(i).length(); j++)
					kryptogramy[i][j] = _kryptogramy.get(i).znaki.get(j).znak_int;
			}
			
			System.out.print(kryptogramy[15][255]);
			for(i=0;i<256;i++)	{
				szyfry.add(SzukajSpacji(kryptogramy,i));	
			}
			//System.out.print(kryptogramy[15][2]);
			
			/*int xor;
			String[] first;
			String[] second;
			int one, two;
			for ( i = 0; i < 16; i++)	{
				for (j = i ; j < 16 ; j++)	{
					if ( i != j)	{
						first = kryptogramy[i].split(" ");
						second = kryptogramy[j].split(" ");
						for (k = 0; k < first.length; k++ ){
							for (l = 0; l < second.length; l++ ){
								one = Integer.parseInt(first[k],2);
								two = Integer.parseInt(second[l],2);
								xor = one ^ two;
								if ( xor <= 90 && xor >= 65)
									System.out.println("Spacja dla i=" + i + " j = "+ j + " w miejscu k=" + k +" l=" + l
											+ " dla klucza " + xor);	
							}
						}
					
						//System.out.println(second[0]);
					}
				}
			}*/
			//int b = Integer.parseInt(kryptogramy[1],2);
			//int x = a ^ b;
		//System.out.println(x);
       
		// w tym momencie zauwazylem fragment z Pana tadeusza :) dodajac brakujace szyfry lub zmieniajac zle

		szyfry.set(0,115^82);
		szyfry.set(13,30^101);
		szyfry.set(17, 62^101);
		szyfry.set(20, 44^107);
		szyfry.set(22, 87^101);
		szyfry.set(41, 156^101);
		szyfry.set(42, 226^32);
		szyfry.set(43, 174^105);
		szyfry.set(44, 103^101);
		szyfry.set(46, 186^105);
		szyfry.set(47, 141^101);
		szyfry.set(51, 181^108);
		
		Dup(kryptogramy,szyfry);
	}
	static void Dup (int kryptogramy [][],List<Integer> list) throws IOException{ // wysylanie szukania spacji rezultatow do pliku
		String fileOutput = "test.txt";
		int temp, i , j;
		FileOutputStream fos = new FileOutputStream(fileOutput);
		for(j=0;j<17;j++){
			fos.write(10);
			for(i=0;i<190;i++){		
				if(list.get(i)!=-1)
					temp = kryptogramy[j][i]^list.get(i);
				else
					temp=32;
				fos.write(temp);
				
			}
		}
		fos.close();
	}

	static int SzukajSpacji(int krypto [][],int m){// wyszukiwanie spacji poprzez xorowanie wszystkich elementow na danej pozycji (xor spacji z mala literka nalezy do [65,90]
		int result=0;
		int index=0;
		for(int i=0;i<17;i++){
			int temp=0;;
			for(int j=0;j<17;j++){
				
				if(((krypto[j][m]^krypto[i][m])<=90)&&((krypto[j][m]^krypto[i][m])>=65)){
					temp++;
				}
			}
			if(temp>=10){
				index++;
				result=krypto[i][m]^32;
				break;
			}			
		}
		if(index==0)
			return -1;
		return result;		
	}
}