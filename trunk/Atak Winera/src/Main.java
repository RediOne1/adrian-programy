
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 
 * @author root
 */

public class Main {

	static ArrayList<BigInteger> lista = new ArrayList<BigInteger>();
	static ArrayList<BigInteger> lista_k = new ArrayList<BigInteger>();
	static ArrayList<BigInteger> lista_d = new ArrayList<BigInteger>();
	static ArrayList<BigInteger> euler = new ArrayList<BigInteger>();

	public static Wymierna setW(BigInteger a, BigInteger b) {
		BigInteger temp_c = a.divide(b);
		BigInteger temp_u = a.subtract(b.multiply(temp_c));
		return new Wymierna(temp_c, temp_u);
	}

	public static void funkcja(int i, BigInteger p, BigInteger q) {
		if (i == 0) {
			return;
		} else {
			BigInteger y = lista.get(i - 1);
			BigInteger d = (q.multiply(y)).add(p);
			BigInteger k = q;
			if (i == 1) {
				lista_d.add(d);
				lista_k.add(k);
			}
			funkcja(i - 1, k, d);
		}
	}

	static BigInteger pierwiastek(BigInteger n) {

		BigInteger a = BigInteger.ONE;
		BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8"))
				.toString());

		while (b.compareTo(a) >= 0) {
			BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
			if (mid.multiply(mid).compareTo(n) > 0)
				b = mid.subtract(BigInteger.ONE);
			else
				a = mid.add(BigInteger.ONE);
		}
		return a.subtract(BigInteger.ONE);
	}

	public static void main(String[] args) {

		BigInteger N = new BigInteger("90581"); // argumenty wejsciowe
		BigInteger e = new BigInteger("17993");
		BigInteger x = N, y = e, z;
		Wymierna temp;
		System.out.println("N : " + N + "\ne: " + e);
		while (true) {
			try {
				temp = setW(x, y);
				z = temp.getC();
				x = y;
				y = temp.getU();
				lista.add(z);
			} catch (Exception ex) {
				break;
			}
		}

		lista_k.add(new BigInteger("1"));
		lista_d.add(lista.get(0));

		for (int i = lista.size() - 1; i >= 0; i--) {
			funkcja(lista.size() - (i + 1), new BigInteger("1"),
					lista.get(lista.size() - (i + 1)));
			if ((e.multiply(lista_d.get((lista.size() - i) - 1))
					.subtract(new BigInteger("1"))).mod(
					lista_k.get((lista.size() - i) - 1))
					.equals(new BigInteger("0"))) {
				BigInteger euler_v = ((e
						.multiply(lista_d.get((lista.size() - i) - 1))
						.subtract(new BigInteger("1")))).divide(lista_k.get((lista
						.size() - i) - 1));
				euler.add(euler_v);
			}

		}
		for (int i = 0; i < lista_k.size(); i++) {
			System.out.println(lista_k.get(i) + " / " + lista_d.get(i));
		}

		for (int i = 0; i < euler.size(); i++) {
			BigInteger a = new BigInteger("1");
			BigInteger b = (N.subtract(euler.get(i))).add(new BigInteger("1"));
			BigInteger c = N;
			BigInteger delta = pierwiastek(b.pow(2).subtract(
					a.multiply(c.multiply(new BigInteger("4")))));
			BigInteger x1 = (b.subtract(delta)).divide(a
					.multiply(new BigInteger("2")));
			BigInteger x2 = (b.add(delta)).divide(a
					.multiply(new BigInteger("2")));
			System.out.println("φ(n) = " + euler.get(i));
			System.out.println("p :" + x1 + "\nq: " + x2);
		}

	}

}
