package org.bnor.euler;

import java.math.BigInteger;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("static-method")
public class FractionTest {

	private static final Fraction ZERO = Fraction.ZERO;
	private static final Fraction ONE = Fraction.ONE;
	private static final Fraction TWO = Fraction.valueOf(2);
	private static final Fraction THREE = Fraction.valueOf(3);
	private static final Fraction FOUR = Fraction.valueOf(4);
	private static final Fraction FIVE = Fraction.valueOf(5);
	private static final Fraction SIX = Fraction.valueOf(6);
	private static final Fraction SEVEN = Fraction.valueOf(7);
	private static final Fraction EIGHT = Fraction.valueOf(8);
	private static final Fraction NINE = Fraction.valueOf(9);
	
	@Test
	public void getters() {
		Fraction twoThird = Fraction.valueOf(2, 3);
		assertEquals(BigInteger.valueOf(2), twoThird.getNumerator());
		assertEquals(BigInteger.valueOf(3), twoThird.getDenominator());
	}

	@Test
	public void constants() {
		assertTrue(Fraction.ZERO.equals(Fraction.valueOf(0)));
		assertTrue(Fraction.ONE.equals(Fraction.valueOf(1)));
	}

	@Test
	public void smallIntegers() {
		Fraction minusTwoF = Fraction.valueOf(-2);
		assertEquals(BigInteger.valueOf(-2), minusTwoF.getNumerator());
		assertEquals(BigInteger.ONE, minusTwoF.getDenominator());

		Fraction minusOneF = Fraction.valueOf(-1);
		assertEquals(BigInteger.valueOf(-1), minusOneF.getNumerator());
		assertEquals(BigInteger.ONE, minusOneF.getDenominator());

		Fraction zeroF = Fraction.valueOf(0);
		assertEquals(BigInteger.ZERO, zeroF.getNumerator());
		assertEquals(BigInteger.ONE, zeroF.getDenominator());

		Fraction oneF = Fraction.valueOf(1);
		assertEquals(BigInteger.ONE, oneF.getNumerator());
		assertEquals(BigInteger.ONE, oneF.getDenominator());

		Fraction twoF = Fraction.valueOf(2);
		assertEquals(BigInteger.valueOf(2), twoF.getNumerator());
		assertEquals(BigInteger.ONE, twoF.getDenominator());
	}

	@Test
	public void simplifying() {
		assertEquals(ZERO, Fraction.valueOf(0, 2));
		assertEquals(ONE, Fraction.valueOf(2, 2));
		assertEquals(TWO, Fraction.valueOf(4, 2));
		assertEquals(THREE, Fraction.valueOf(6, 2));
		assertEquals(FOUR, Fraction.valueOf(8, 2));
		assertEquals(FIVE, Fraction.valueOf(10, 2));
		assertEquals(SIX, Fraction.valueOf(12, 2));
		assertEquals(SEVEN, Fraction.valueOf(14, 2));
		assertEquals(EIGHT, Fraction.valueOf(16, 2));
		assertEquals(NINE, Fraction.valueOf(18, 2));

		Fraction twoThird = Fraction.valueOf(4, 6);
		assertEquals(BigInteger.valueOf(2), twoThird.getNumerator());
		assertEquals(BigInteger.valueOf(3), twoThird.getDenominator());
		
		Fraction zero = Fraction.valueOf(0, 15);
		assertEquals(BigInteger.valueOf(0), zero.getNumerator());
		assertEquals(BigInteger.valueOf(1), zero.getDenominator());
	}

	@Test
	public void simplifyingNegative() {
		assertEquals(ONE.negate(), Fraction.valueOf(-2, 2));
		assertEquals(TWO.negate(), Fraction.valueOf(-4, 2));
		assertEquals(THREE.negate(), Fraction.valueOf(-6, 2));
		assertEquals(FOUR.negate(), Fraction.valueOf(-8, 2));
		assertEquals(FIVE.negate(), Fraction.valueOf(-10, 2));
	}

	@Test
	public void denominatorAlwaysPositive() {
		Fraction minusHalf = Fraction.valueOf(-1, 2);
		assertEquals(BigInteger.valueOf(-1), minusHalf.getNumerator());
		assertEquals(BigInteger.valueOf(2), minusHalf.getDenominator());

		Fraction minusTwo = Fraction.valueOf(2, -1);
		assertEquals(BigInteger.valueOf(-2), minusTwo.getNumerator());
		assertEquals(BigInteger.ONE, minusTwo.getDenominator());
		
		Fraction minusFive = Fraction.valueOf(20, -4);
		assertEquals(BigInteger.valueOf(-5), minusFive.getNumerator());
		assertEquals(BigInteger.ONE, minusFive.getDenominator());

		Fraction minusTwoThird = Fraction.valueOf(2, -3);
		assertEquals(BigInteger.valueOf(-2), minusTwoThird.getNumerator());
		assertEquals(BigInteger.valueOf(3), minusTwoThird.getDenominator());
	}

	@Test
	public void singleValueConstructorMethods() {
		Fraction bi5 = Fraction.valueOf(BigInteger.valueOf(5));
		Fraction long5 = Fraction.valueOf(5);
		assertTrue(bi5.equals(long5));

		Fraction biMillion = Fraction.valueOf(BigInteger.valueOf(1_000_000));
		Fraction longMillion = Fraction.valueOf(1_000_000);
		assertTrue(biMillion.equals(longMillion));
	}

	@Test(expected = ArithmeticException.class)
	public void longConstructorMethodDivideByZero() {
		Fraction.valueOf(1, 0);
	}

	@Test(expected = ArithmeticException.class)
	public void bigIntegerConstructorMethodDivideByZero() {
		Fraction.valueOf(BigInteger.ONE, BigInteger.ZERO);
	}

	@Test
	public void longContructorMethod() {
		Fraction two = Fraction.valueOf(1_000, 500);
		assertTrue(TWO.equals(two));
		
		Fraction tenThird = Fraction.valueOf(1_000, 300);
		assertEquals(BigInteger.valueOf(10), tenThird.getNumerator());
		assertEquals(BigInteger.valueOf(3), tenThird.getDenominator());
	}

	@Test
	public void bigIntegerContructorMethod() {
		Fraction two = Fraction.valueOf(BigInteger.valueOf(1_000), BigInteger.valueOf(500));
		assertTrue(TWO.equals(two));

		Fraction tenThird = Fraction.valueOf(BigInteger.valueOf(1_000), BigInteger.valueOf(300));
		assertEquals(BigInteger.valueOf(10), tenThird.getNumerator());
		assertEquals(BigInteger.valueOf(3), tenThird.getDenominator());
	}
	
	@Test
	public void add() {
		assertTrue(FIVE.equals(TWO.add(THREE)));
		assertTrue(FIVE.equals(FIVE.add(ZERO)));
		assertTrue(ONE.equals(Fraction.valueOf(-5, 1).add(SIX)));

		Fraction twoThird = Fraction.valueOf(2, 3);
		Fraction fourFifth = Fraction.valueOf(4, 5);
		Fraction twentytwoFifteenth = Fraction.valueOf(22, 15);
		assertTrue(twentytwoFifteenth.equals(twoThird.add(fourFifth)));

		Fraction oneThird = Fraction.valueOf(1, 3);
		assertTrue(ONE.equals(oneThird.add(twoThird)));
	}

	@Test
	public void subtract() {
		assertTrue(FIVE.equals(NINE.subtract(FOUR)));
		assertTrue(FIVE.equals(FIVE.subtract(ZERO)));
		assertTrue(ONE.equals(Fraction.valueOf(-5, 1).subtract(Fraction.valueOf(-6, 1))));

		Fraction twoThird = Fraction.valueOf(2, 3);
		Fraction fourFifth = Fraction.valueOf(4, 5);
		Fraction minusTwoFifteenth = Fraction.valueOf(-2, 15);
		assertTrue(minusTwoFifteenth.equals(twoThird.subtract(fourFifth)));

		Fraction oneSixth = Fraction.valueOf(1, 6);
		Fraction fiveSixth = Fraction.valueOf(5, 6);
		assertTrue(twoThird.equals(fiveSixth.subtract(oneSixth)));
	}

	@Test
	public void multiply() {
		assertTrue(SIX.equals(TWO.multiply(THREE)));
		assertTrue(THREE.equals(ONE.negate().multiply(THREE.negate())));
		assertTrue(ZERO.equals(EIGHT.multiply(ZERO)));

		Fraction twoThird = Fraction.valueOf(2, 3);
		Fraction fiveSixth = Fraction.valueOf(5, 6);
		Fraction fiveNineth = Fraction.valueOf(5, 9);
		assertTrue(fiveNineth.equals(twoThird.multiply(fiveSixth)));
	}

	@Test
	public void divide() {
		assertTrue(THREE.equals(SIX.divide(TWO)));
		assertTrue(THREE.equals(SIX.negate().divide(TWO.negate())));

		Fraction twoThird = Fraction.valueOf(2, 3);
		Fraction fiveSixth = Fraction.valueOf(5, 6);
		Fraction fourFifth = Fraction.valueOf(4, 5);
		assertEquals(fourFifth, twoThird.divide(fiveSixth));
	}

	@Test(expected = ArithmeticException.class)
	public void divideByZero() {
		THREE.divide(Fraction.ZERO);
	}

	@Test
	public void negate() {
		assertTrue(ZERO.equals(ZERO.negate()));
		assertTrue(Fraction.valueOf(-1, 1).equals(ONE.negate()));
		assertTrue(Fraction.valueOf(-2, 1).equals(TWO.negate()));
		assertTrue(ONE.equals(Fraction.valueOf(-1, 1).negate()));
		assertTrue(TWO.equals(Fraction.valueOf(-2, 1).negate()));
		assertTrue(Fraction.valueOf(5, 6).equals(Fraction.valueOf(-5, 6).negate()));
		assertTrue(Fraction.valueOf(-5, 6).equals(Fraction.valueOf(5, 6).negate()));
	}

	@Test
	public void abs() {
		assertTrue(ZERO.equals(ZERO.abs()));
		assertTrue(ONE.equals(ONE.abs()));
		assertTrue(TWO.equals(TWO.abs()));

		assertTrue(Fraction.valueOf(5, 6).equals(Fraction.valueOf(-5, 6).abs()));
	}

	// Comparable
	
	@Test
	public void compareToEqualIsZero() {
		assertEquals(0, ZERO.compareTo(ZERO));
		assertEquals(0, ONE.compareTo(ONE));
		assertEquals(0, NINE.compareTo(NINE));

		assertEquals(0, Fraction.valueOf(1, 2).compareTo(Fraction.valueOf(50, 100)));

		assertEquals(0, Fraction.valueOf(-2, 3).compareTo(Fraction.valueOf(4, -6)));
	}
	
	@Test
	public void compareToSmallerIsMinusOne() {
		assertEquals(-1, ZERO.compareTo(ONE));
		assertEquals(-1, Fraction.valueOf(1, 4).compareTo(Fraction.valueOf(1, 3)));
	}
	
	
	@Test
	public void compareToBiggerIsOne() {
		assertEquals(1, ONE.compareTo(ZERO));
		assertEquals(1, Fraction.valueOf(1, 3).compareTo(Fraction.valueOf(1, 4)));
	}
	
	// Equals Contract
	
	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(Fraction.class)
	    		.suppress(Warning.NULL_FIELDS)
	    		.verify();
	}

	// NPE

	@Test(expected = NullPointerException.class)
	public void singleValueNull() {
		Fraction.valueOf(null);
	}

	@Test(expected = NullPointerException.class)
	public void numeratorNull() {
		Fraction.valueOf(null, BigInteger.ONE);
	}

	@Test(expected = NullPointerException.class)
	public void denominatorNull() {
		Fraction.valueOf(BigInteger.ONE, null);
	}

	@Test(expected = NullPointerException.class)
	public void addNull() {
		ONE.add(null);
	}

	@Test(expected = NullPointerException.class)
	public void subtractNull() {
		ONE.subtract(null);
	}

	@Test(expected = NullPointerException.class)
	public void multiplyNull() {
		ONE.multiply(null);
	}

	@Test(expected = NullPointerException.class)
	public void divideNull() {
		ONE.divide(null);
	}
}
