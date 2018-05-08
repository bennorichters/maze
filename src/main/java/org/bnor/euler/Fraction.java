package org.bnor.euler;

import java.math.BigInteger;

/**
 * A fraction with an integer numerator and an integer denominator. The numerator and
 * the denominator are represented by a {@code BigInteger}. The numerator and the 
 * denominator are relative prime, i.e. their greatest common divisor is one. The 
 * denominator is always greater than or equal to one. The numerator can be negative
 * zero or positive; respectively representing a negative fraction, zero or a positive
 * fraction.
 * 
 * <p>
 * Static constructor methods can be used to obtain instances of this class. These 
 * constructor methods will return a {@code Fraction} that is equivalent to a fraction 
 * with the given numerator and denominator, but these values might be changed in 
 * order to comply to the restrictions as described above.
 * 
 * <p>
 * <em>Example</em><br>
 * {@code Fraction f = Fraction.valueOf(2, -4);} <br>
 * {@code System.out.println(f.getNumerator()); // -1} <br>
 * {@code System.out.println(f.getDenominator()); // 2}
 * 
 * <p>
 * The constructor methods will throw an {@code ArithmeticException} if a zero value 
 * is passed in for the denominator. All methods in this class throw a 
 * {@code NullPointerException} when a {@code null} reference is given as an argument.   
 * 
 * <p>
 * A {@code Fraction} is considered equal to another {@code Fraction} if and only if
 * its numerators are equal and its denominators are equal.
 * 
 * <p>
 * Instances of this class are immutable.
 */
public final class Fraction implements Comparable<Fraction> {
	private static final Fraction[] INTEGER_CACHE = {
			Fraction.valueOfNoCache(0),
			Fraction.valueOfNoCache(1),
			Fraction.valueOfNoCache(2),
			Fraction.valueOfNoCache(3),
			Fraction.valueOfNoCache(4),
			Fraction.valueOfNoCache(5),
			Fraction.valueOfNoCache(6),
			Fraction.valueOfNoCache(7),
			Fraction.valueOfNoCache(8),
			Fraction.valueOfNoCache(9) };

	/**
	 * A {@code Fraction} with numerator zero and denominator one.
	 */
	public static final Fraction ZERO = INTEGER_CACHE[0];

	/**
	 * A {@code Fraction} with numerator one and denominator one.
	 */
	public static final Fraction ONE = INTEGER_CACHE[1];

	private final BigInteger numerator;
	private final BigInteger denominator;

	private Fraction(BigInteger numerator, BigInteger denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * Creates a {@code Fraction}.
	 * 
	 * <p>
	 * See the documentation on the class level for a more detailed explanation of
	 * the behavior of the constructor methods.
	 * 
	 * @param numerator the numerator of the {@code Fraction}
	 * @param denominator the denominator of the {@code Fraction}  
	 * @return the {@code Fraction}
	 */
	public static Fraction valueOf(BigInteger numerator, BigInteger denominator) {
		if (denominator.equals(BigInteger.ZERO)) {
			throw new ArithmeticException("/0");
		}

		if (denominator.compareTo(BigInteger.ZERO) < 0) {
			return valueOf(numerator.negate(), denominator.negate());
		}

		BigInteger gcd = numerator.gcd(denominator);
		return new Fraction(numerator.divide(gcd), denominator.divide(gcd));
	}

	/**
	 * Creates a {@code Fraction} with a denominator with the value one. 
	 * 
	 * @param number the numerator of the {@code Fraction}
	 * @return the {@code Fraction}
	 */
	public static Fraction valueOf(BigInteger number) {
		return valueOf(number, BigInteger.ONE);
	}

	/**
	 * Creates a {@code Fraction} with a denominator with the value one.
	 *  
	 * @param number the numerator of the {@code Fraction}
	 * @return the {@code Fraction}
	 */
	public static Fraction valueOf(long number) {
		if (number >= 0 && number < INTEGER_CACHE.length) {
			return INTEGER_CACHE[(int) number];
		}

		return valueOfNoCache(number);
	}

	/**
	 * Creates a {@code Fraction}.
	 * 
	 * <p>
	 * See the documentation on the class level for a more detailed explanation of
	 * the behavior of the constructor methods.

	 * @param numerator the numerator of the {@code Fraction}
	 * @param denominator the denominator of the {@code Fraction}  
	 * @return the {@code Fraction}
	 */
	public static Fraction valueOf(long numerator, long denominator) {
		return valueOf(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	private static Fraction valueOfNoCache(long number) {
		return valueOf(BigInteger.valueOf(number), BigInteger.ONE);
	}

	/**
	 * @return the numerator of this fraction
	 */
	public BigInteger getNumerator() {
		return numerator;
	}

	/**
	 * @return the denominator of this fraction
	 */
	public BigInteger getDenominator() {
		return denominator;
	}

	/**
	 * Negates the {@code Fraction}.
	 * 
	 * @return a negated {@code Fraction}
	 */
	public Fraction negate() {
		return valueOf(numerator.negate(), denominator);
	}

	/**
	 * @return the absolute value of this {@code Fraction}.
	 */
	public Fraction abs() {
		return (numerator.compareTo(BigInteger.ZERO) < 0) ?
				valueOf(numerator.negate(), denominator) :
				this;
	}

	/**
	 * Returns a {@code Fraction} that is the result of adding {@code other} to this {@code Fraction}.
	 * 
	 * @param other the {@code Fraction} to add to {@code this}
	 * @return {@code this} + {@code other}
	 */
	public Fraction add(Fraction other) {
		return valueOf(numerator.multiply(other.denominator).add(other.numerator.multiply(denominator)),
				denominator.multiply(other.denominator));
	}

	/**
	 * Returns a {@code Fraction} that is the result of subtracting {@code other} from this {@code Fraction}.
	 * 
	 * @param other the {@code Fraction} to subtract from {@code this}
	 * @return {@code this} - {@code other}
	 */
	public Fraction subtract(Fraction other) {
		return add(other.negate());
	}

	/**
	 * Returns a {@code Fraction} that is the result of multiplying {@code other} with this {@code Fraction}.
	 * 
	 * @param other the {@code Fraction} to multiply with {@code this}
	 * @return {@code this} * {@code other}
	 */
	public Fraction multiply(Fraction other) {
		return valueOf(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
	}

	/**
	 * Returns a {@code Fraction} that is the result of dividing this {@code Fraction} by {@code other}.
	 * 
	 * @param other the {@code Fraction} to divide {@code this} by.
	 * @return {@code this} / {@code other}
	 * @throws ArithmeticException if {@code other} equals {@link #ZERO}
	 */
	public Fraction divide(Fraction other) {
		return multiply(valueOf(other.denominator, other.numerator));
	}

	/**
	 * @return -1, 0 or 1 as this {@code Fraction} is numerically less than, equal
	 *         to, or greater than {@code other}.
	 */
	@Override
	public int compareTo(Fraction other) {
		return subtract(other).getNumerator().compareTo(BigInteger.ZERO);
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + numerator.hashCode();
		result = prime * result + denominator.hashCode();

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Fraction)) {
			return false;
		}

		Fraction other = (Fraction) obj;
		return numerator.equals(other.numerator) && denominator.equals(other.denominator);
	}

	@Override
	public String toString() {
		return numerator + "/" + denominator;
	}

}