public class Znak {

	public String znak_bin;
	public int znak_int;
	public char znak_ascii;

	public Znak(String binary) {
		this.znak_bin = binary;
		this.znak_int = Integer.parseInt(binary, 2);
		this.znak_ascii = (char) znak_int;
	}

	@Override
	public String toString() {
		return "" + znak_ascii;
	}
}
