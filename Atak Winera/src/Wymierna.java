
import java.math.BigInteger;


public class Wymierna {
	private BigInteger c, u;// calkowita ulamek

	Wymierna() {

	}

	Wymierna(BigInteger c, BigInteger u) {
		this.c = c;
		this.u = u;
	}

	private void setU(BigInteger u) {
		this.u = u;
	}

	private void setC(BigInteger c) {
		this.c = c;
	}

	public BigInteger getU() {
		return this.u;
	}

	public BigInteger getC() {
		return this.c;
	}

}
