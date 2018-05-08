package org.bnor.maze.visual;

import java.util.Iterator;
import java.util.List;

import org.bnor.euler.Fraction;
import org.bnor.maze.ArcDirection;
import org.bnor.maze.CircleCoordinate;
import org.bnor.maze.Maze;

public final class Drawer {

	private final int distance;
	private final Canvas canvas;

	public Drawer(int distance, Canvas canvas) {
		this.distance = distance;
		this.canvas = canvas;
	}

	public void drawMaze(Maze maze) {
		for (int circle = 1; circle <= maze.getCircles(); circle++) {
			for (int arc = 0; arc < CircleCoordinate.calcTotalArcs(circle); arc++) {
				CircleCoordinate coordinate = CircleCoordinate.create(circle, arc);

				if (!maze.isArcOpen(coordinate)) {
					arcBoundary(coordinate);
				}

				if (!maze.isLineOpen(coordinate)) {
					lineBoundary(coordinate);
				}
			}
		}
	}

	public void drawPath(List<CircleCoordinate> path) {
		Iterator<CircleCoordinate> it = path.iterator();

		CircleCoordinate firstInCircle = it.next();
		Fraction firstAngle = null;
		ArcDirection arcDirection = null;

		CircleCoordinate prev = firstInCircle;
		while (it.hasNext()) {
			CircleCoordinate next = it.next();

			int circle = firstInCircle.getCircle();
			if (next.getCircle() == circle) {
				if (arcDirection == null) {
					arcDirection = arcDirectionBetween(firstInCircle, next);
				}

				if (!it.hasNext()) {
					firstArc(firstAngle, arcDirection, next, circle);
				}
			} else {
				Fraction lastAngle = linePath(prev, next);

				if (circle > 0) {
					if (firstAngle == null) {
						Fraction halfStep = CircleCoordinate.calcAngleStep(circle).divide(Fraction.valueOf(2));
						firstAngle = firstInCircle.getAngle().add(halfStep);
					}

					if (arcDirection == null) {
						arcDirection = firstAngle.compareTo(lastAngle) < 0 ? ArcDirection.CLOCKWISE : ArcDirection.COUNTERCLOCKWISE;
					}

					arcPath(circle, firstAngle, lastAngle, arcDirection);
				}

				if (!it.hasNext()) {
					lastArc(prev, next, lastAngle);
				}

				firstInCircle = next;
				firstAngle = lastAngle;
				arcDirection = null;
			}

			prev = next;
		}
	}

	public void drawDotOnPath(CircleCoordinate coordinate) {
		int circle = coordinate.getCircle();
		int radius = calcRadius(circle) + (circle == 0 ? 0 : distance / 2);
		
		Fraction halfStep = CircleCoordinate.calcAngleStep(circle).divide(Fraction.valueOf(2));
		Fraction angle = coordinate.getAngle().add(halfStep);
		
		canvas.circle(PolarCoordinate.create(radius, angle), (3 * distance) / 4);
	}

	private void firstArc(Fraction firstAngle, ArcDirection arcDirection, CircleCoordinate next, int circle) {
		Fraction halfStep = CircleCoordinate.calcAngleStep(circle).divide(Fraction.valueOf(2));
		Fraction lastAngle = next.getAngle().add(halfStep);

		arcPath(circle, firstAngle, lastAngle, arcDirection);
	}

	private void lastArc(CircleCoordinate prev, CircleCoordinate next, Fraction fromAngle) {
		if (CircleCoordinate.calcTotalArcs(next.getCircle()) < CircleCoordinate.calcTotalArcs(prev.getCircle())) {
			Fraction halfStep = CircleCoordinate.calcAngleStep(next.getCircle()).divide(Fraction.valueOf(2));
			Fraction to = next.getAngle().add(halfStep);

			ArcDirection arcDirection = fromAngle.compareTo(to) < 0 ? ArcDirection.CLOCKWISE : ArcDirection.COUNTERCLOCKWISE;

			arcPath(next.getCircle(), fromAngle, to, arcDirection);
		}
	}

	private void arcBoundary(CircleCoordinate start) {
		int circle = start.getCircle();
		int radius = calcRadius(circle);

		Fraction startAngle = start.getAngle();
		Fraction angleStep = CircleCoordinate.calcAngleStep(circle);

		drawArc(radius, startAngle, angleStep);
	}

	private void arcPath(int circle, Fraction fromAngle, Fraction toAngle, ArcDirection arcDirection) {
		Fraction startAngle = (arcDirection == ArcDirection.CLOCKWISE) ? fromAngle : toAngle;
		Fraction endAngle = (arcDirection == ArcDirection.CLOCKWISE) ? toAngle : fromAngle;

		int compare = endAngle.compareTo(startAngle);
		if (compare == 0) {
			return;
		}

		Fraction angleStep = (compare < 0)
				? Fraction.valueOf(360).subtract(startAngle).add(endAngle)
				: endAngle.subtract(startAngle);

		int radius = calcRadius(circle) + distance / 2;

		drawArc(radius, startAngle, angleStep);
	}

	private void drawArc(int radius, Fraction startAngle, Fraction angleStep) {
		canvas.arc(PolarCoordinate.create(radius, startAngle), angleStep);
	}

	private void lineBoundary(CircleCoordinate coordinate) {
		int radiusStart = calcRadius(coordinate.getCircle());
		drawLine(radiusStart, coordinate.getAngle());
	}

	private Fraction linePath(CircleCoordinate from, CircleCoordinate to) {
		Fraction angle = angleInBiggestCircle(from, to);

		if (from.equals(CircleCoordinate.ORIGIN) || to.equals(CircleCoordinate.ORIGIN)) {
			drawLine(0, (distance * 3) / 2, angle);
		} else {
			drawLine(calcRadius(smallest(from.getCircle(), to.getCircle())) + distance / 2, angle);
		}

		return angle;
	}

	private void drawLine(int radiusStart, Fraction angle) {
		drawLine(radiusStart, radiusStart + distance, angle);
	}

	private void drawLine(int radiusStart, int radiusEnd, Fraction angle) {
		canvas.line(PolarCoordinate.create(radiusStart, angle), radiusEnd - radiusStart);
	}
	
	private int calcRadius(int circle) {
		return circle * distance;
	}

	private static int smallest(int fromCircle, int toCircle) {
		return (fromCircle < toCircle) ? fromCircle : toCircle;
	}

	private static ArcDirection arcDirectionBetween(CircleCoordinate from, CircleCoordinate to) {
		Fraction fromAngle = from.getAngle();
		Fraction toAngle = to.getAngle();
		Fraction diff = toAngle.subtract(fromAngle);

		Fraction halfCircle = Fraction.valueOf(180 * (diff.compareTo(Fraction.ZERO) < 0 ? -1 : 1));
		return (diff.compareTo(halfCircle) < 0) ? ArcDirection.CLOCKWISE : ArcDirection.COUNTERCLOCKWISE;
	}

	private static Fraction angleInBiggestCircle(CircleCoordinate from, CircleCoordinate to) {
		CircleCoordinate outer = from.getCircle() > to.getCircle() ? from : to;

		Fraction angleStep = CircleCoordinate.calcAngleStep(outer.getCircle());
		Fraction halfStep = angleStep.divide(Fraction.valueOf(2));

		return outer.getAngle().add(halfStep);
	}
}
