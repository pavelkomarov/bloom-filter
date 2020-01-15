import java.io.PrintWriter;
import java.io.File;

/**
 * Does a monte carlo simulation of the hasher to ensure the outputs are
 * randomly distrubuted given random inputs.
 */
public class Analysis {

    public static void main(String []args) throws Exception {
      
      	//make a vector of k random numbers
      	int[] rand = new int[20];
    	for (int k = 0; k < 20; k++) {
    		rand[k] = (int)(Math.random()*Integer.MAX_VALUE);
    	}

    	//fill an n x k array with counts of the number of times the kth
    	//"hash" results in an hx = hash(x) that falls on a given i in [0, n-1]
      	int x;
    	int[][] arr = new int[1000][20];
     	for (int i = 0; i < 1000000; i++) {//1 million times means the ith bucket should hold 1M/1k = 1k
     		x = (int)(Math.random()*Integer.MAX_VALUE);//get any random number

			int hx;
			for (int k = 0; k < 20; k++) {
				hx = (x^rand[k])%1000;//hash that number by xor with kth random number % n  
				arr[hx][k] += 1;		//works because random xor random is random
			}
		}

		//output the array to a file, so I can read in to Matlab and visualize
		PrintWriter pw = new PrintWriter(new File("/home/pvlkmrv/Desktop/thing.csv"));
		for (int a = 0; a < 1000; a++) {
			for (int b = 0; b < 20; b++) {
				pw.print(arr[a][b] + ",");
			}
			pw.println(0);
		}
		pw.flush();
    }
}
