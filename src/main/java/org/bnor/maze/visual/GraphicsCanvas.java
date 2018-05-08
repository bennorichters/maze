package org.bnor.maze.visual;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

import org.bnor.euler.Fraction;

public final class GraphicsCanvas implements Canvas {

	private final int center;
	private final Graphics2D graphics;
	
	public GraphicsCanvas(int center, Graphics2D graphics) {
		this.center = center;
		this.graphics = graphics;
	}
	
	@Override
	public void circle(PolarCoordinate circleCenter, int diameter) {
		XYCoordinate position = position(circleCenter);
		
		int offset = diameter / 2;
		
		graphics.fillOval(position.x - offset, position.y - offset, diameter, diameter);
	}

	@Override
	public void line(PolarCoordinate from, int length) {
		XYCoordinate start = position(from);
		XYCoordinate end = position(from.extrapolate(length));
		
		graphics.drawLine(start.x, start.y, end.x, end.y);
	}

	@Override
	public void arc(PolarCoordinate from, Fraction span) {
		int radius = from.getRadius();
		int corner = calcCorner(radius);

		float startAngleFloat = fraction2float(from.getAngle());
		float totalAngleFloat = fraction2float(span);

		Arc2D arc2d = new Arc2D.Float(corner, corner, 2 * radius, 2 * radius, startAngleFloat, totalAngleFloat, Arc2D.OPEN);
		graphics.draw(arc2d);
	}
	
	private XYCoordinate position(PolarCoordinate polar) {
		int radius = polar.getRadius();
		Fraction angle = polar.getAngle();
		
		float angleFloat = fraction2float(angle);
		double rad = Math.toRadians(angleFloat);

		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		int x = (int) Math.round(center + radius * cos);
		int y = (int) Math.round(center - radius * sin);

		return new XYCoordinate(x, y);
	}

	private int calcCorner(int radius) {
		return center - radius;
	}
	
	private static float fraction2float(Fraction fraction) {
		return (float) fraction.getNumerator().intValue() / fraction.getDenominator().intValue();
	}

	private static final class XYCoordinate {
		final int x;
		final int y;

		XYCoordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
