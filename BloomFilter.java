
import java.io.PrintWriter;//just for I/O
import java.io.File;

public class BloomFilter {

	public static int n, k, H[], xor[];

	/**
	 * A Bloom Filter needs to know how large it's supposed to be, n. For 
	 * simulation I also need to know k = c*ln2, calculable given m.
	 * @param n The size of H
	 * @param m The number of things to be put in the Bloom Filter
	 */
	public void setup(int n, int m) {
		this.n = n;//keep it because there is no way to reconstruct it later
		H = new int[(n+31)/32];//store n bits with ceil(n/32) ints = (n + 31)/32 ints
								//note this makes indexing properly a mental exercise
		k = (int)Math.round(Math.log(2)*n/m);//Set k = cln2; careful division of doubles
		xor = new int[k];//we need k random numbers to xor with as a simulated hash function
		for (int i = 0; i < k; i++) {
			xor[i] = (int)(Math.random()*Integer.MAX_VALUE);//in [0, max int) -> |universe| = max int
		}
	}

	/**
	 * To insert: hash input x k times (known from setup), and set the corresponding
	 * k bits in H to 1.
	 * @param x A number we want to add to the Bloom Filter, assumed positive for simplicity
	 */
	public void insert(int x) {//x in Universe
		int hx;
		for (int i = 0; i < k; i++) {//set k bits in H
			hx = (x^xor[i])%n;//hash(x) in [0..n-1]. Note hash functions should output a
				//uniform distribution, so all that matters is that this function do the same.
				//By my analysis, it does.
			H[hx/32] = H[hx/32] | (1 << (31-(hx%32)));//set the hxth bit (of [0..n-1]) in H
		}
	}

	/**
	 * Answers "Is y in H?" To query: hash input y k times, and check whether the
	 * corresponding k bits in H are 1. Return whether all k bits at hash(y) in H are 1.
	 * @param y A number we want to look up in the Bloom Filter 
	 * @return whether the number is (likely) in the Bloom Filter
	 */
	public boolean query(int y) {//is y in H?
		int hy;
		for (int i = 0; i < k; i++) {//check all hashed locations for a flipped bit
			hy = (y^xor[i])%n;//reproduce the same hash as during insertion (no setup() between insertion and queries)
			if ( ( H[hy/32] & (1 << (31-(hy%32))) ) == 0 ) return false;//if any bit is unflipped,
		}															//y wasn't added
		return true;//if all bits were found flipped, y was probably added, but we could just
	}				//be seeing bits flipped during unrelated insertions.

	public static void main(String ... args) throws Exception {
		BloomFilter BF = new BloomFilter();
		
		//test routine
		BF.setup(100,2);
		BF.insert(100);
		BF.insert(1000);
		System.out.println(BF.query(100));
		System.out.println(BF.query(1000));//it works!
		System.out.println(BF.query(3));
		System.out.println(BF.query(500));
		System.out.println(BF.query(800000));

		//simulations
		PrintWriter pw = new PrintWriter(new File("/home/pvlkmrv/Desktop/output4.csv"));

		int x, t, m, c[] = {100, 50, 25, 10, 5, 2, 1};
		double lnFPrate, U = (double)Integer.MAX_VALUE;//U is the size of the universe
		for (int n = 10; n <= 1000000; n *= 10) {//interate through n
			for (int i = 0; i < c.length; i++) {//iterate through c = 100, 50, 25, 10, 5, 2, 1
				if (n/c[i] == 0) { continue; }
				m = n/c[i];// -> algebra from c = n/m
				for (int q = 0; q < 5; q++) {//do each (n,c) pair 5 times for insurance
					BF.setup(n, m);
					
					for (int j = 0; j < m; j++) {//add m (random) things
						x = (int)(Math.random()*Integer.MAX_VALUE);//x in [0, max int-1]
						BF.insert(x);
					}

					t = 0;//find the number of "true"s encountered on all possible inputs
					for (int y = 0; y < Integer.MAX_VALUE; y++) {//valid inputs are [0, max int-1]
						if (BF.query(y)) {
							t++;
						}
					}

					//FPrate = FP/(FP + TN) -> (t-m)/(t-m + U-t) -> (t-m)/(U-m)
					//But these values can be absurdly small and result in underflow, so:
					//ln(FPrate) = ln((t-m)/(U-m)) = ln(t-m)-ln(U-m);
					lnFPrate = Math.log(t-m)-Math.log(U-m);//true for arguments > 0
					
					pw.println(n + "," + n/m + "," + (t-m) + "," + lnFPrate);//output "n,c,FP" to a file
					System.out.println("finished n = " + n + ", c = " + n/m);
				}
			}
			pw.flush();//make sure it's on the disk
		}
	}
}

