import java.math.BigInteger;

public class Tuple {
	private BigInteger x, y, mcd;

	public Tuple(BigInteger mcd, BigInteger x, BigInteger y) {
		this.mcd = mcd;
		this.x = x;
		this.y = y;
	}
	public BigInteger getX() {
		return this.x;
	}
	public BigInteger getY() {
		return this.y;
	}
	public BigInteger getMCD() {
		return this.mcd;
	}
}