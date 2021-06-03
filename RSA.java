import java.util.ArrayList;
import java.math.BigInteger;

public class RSA {
	
	// Método para obtener el máximo común divisor (Algoritmo euclidiano extendido)
	// ed = 1 mod(phi)
	// d = e^-1 mod(phi)
	// ax + by = mcd(a, b)
	// e*d + phi*y = 1
	// (e*d + phi*y = 1)*mod(phi)   phi*y = 0, ya que es un múlitplo de mod(phi)
	// ed = 1 mod(phi)
	// d = e^-1 mod(phi)
	public static Tuple mcdExtended(BigInteger a, BigInteger b) {
		if (a.compareTo(BigInteger.valueOf(0)) == 0) {
			return new Tuple(b, BigInteger.valueOf(0), BigInteger.valueOf(1));
		} else {
			Tuple mcd = mcdExtended(b.mod(a), a);
			return new Tuple(mcd.getMCD(), mcd.getY().subtract(b.divide(a).multiply(mcd.getX())), mcd.getX());
		}
	}
	
	// Método para obtener el máximo común divisor (Algoritmo euclideano básico)
		public static BigInteger mcd(BigInteger a, BigInteger b) {
			if (a.compareTo(BigInteger.valueOf(0)) == 0) {
				return b;
			} else {
				return mcd(b.mod(a), a);
			}
		}
	
	// Exponenciación rápida
	public static BigInteger fastExponent(BigInteger a, BigInteger b, BigInteger m) {
		BigInteger result = BigInteger.valueOf(1);
		// mientras b sea mayor que cero
		while(b.compareTo(BigInteger.valueOf(0)) == 1) {
			// si (b % 2 == 0), checamos si el dígito binario es 1
			if(b.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(1)) == 0) {
				result = result.multiply(a).mod(m);
			}
			a = a.multiply(a).mod(m);
			// Dividimos para sacar los dígitos del número binario que corresponden al número decimal
			b = b.divide(BigInteger.valueOf(2));
		}
		return result;
	}

	public static ArrayList<BigInteger> encripta(String mensaje, BigInteger e, BigInteger n) {
		ArrayList<BigInteger> resultado = new ArrayList<BigInteger>();

		// (ch^e) mod n
		for (Character ch : mensaje.toCharArray()) {
			BigInteger base = BigInteger.valueOf((int) ch);
			BigInteger exponente = e;
			BigInteger mod = n;
			BigInteger c = fastExponent(base,exponente,mod);

			resultado.add(c);
		}
		return resultado;
	}

	public static String desencripta(ArrayList<BigInteger> mensaje, BigInteger d, BigInteger n) {
		String resultado = "";
		// (ch^d) mod n
		// Nota: ch ya está encriptado
		for (BigInteger num : mensaje) {
			BigInteger base = num;
			BigInteger exponente = d;
			BigInteger mod = n;
			BigInteger c = fastExponent(base,exponente,mod);

			char character = (char) (c.intValue());
			resultado += character;
		}
		return resultado;
	}

	public static void main(String[] args) {
		// p y q son números primos
		BigInteger p = BigInteger.valueOf(1999), q = BigInteger.valueOf(2003);

		// Primera parte por hacer para obtener la llave pública
		// n será el módulo a usar para encriptar y desencriptar mensajes
		// n = p * q
		BigInteger n = p.multiply(q);

		// Calcular phi que es la segunda parte para obtener la llave pública
		// "e" será la llave pública y sufrirá cambios al encontrar el coprimo con respecto de phi
		// phi = (p - 1) * (q - 1)
		BigInteger phi = p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));
		BigInteger e = BigInteger.valueOf(2);

		// "e" tienes que ser menor y coprimo de phi y también mayor a 1
		// 1 < e < phi
		// -1 less, 0 equal, 1 greater
		while (e.compareTo(phi) == -1) {
			if (mcd(e, phi).compareTo(BigInteger.valueOf(1)) == 0) {
				break;
			} else {
				e = e.add(BigInteger.valueOf(1));
			}
		}

		// Obtención de la llave privada
		BigInteger d = mcdExtended(e, phi).getX().mod(phi);
		String mensaje = "proyecto final para matematicas computacionales";

		ArrayList<BigInteger> mEncriptado = encripta(mensaje, e, n);
		System.out.println(mEncriptado);
		System.out.println(desencripta(mEncriptado, d, n));
	}
}