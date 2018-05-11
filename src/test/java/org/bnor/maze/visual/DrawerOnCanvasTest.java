package org.bnor.maze.visual;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.bnor.euler.Fraction;
import org.bnor.maze.CircleCoordinate;
import org.bnor.maze.Maze;
import org.bnor.maze.MazeJson;
import org.bnor.maze.MazeSolver;
import org.junit.Test;

@SuppressWarnings("static-method")
public class DrawerOnCanvasTest {

	private final static String[] MAZE_RESOURCES = {
			"maze_003_1.json",
			"maze_004_1.json",
			"maze_005_1.json",
			"maze_005_2.json",
			"maze_040_1.json"
	};

	@Test
	public void drawMazesAndPaths() {
		for (String resource : MAZE_RESOURCES) {
			Maze maze = MazeJson.deserialize(MazeJsonReader.read(resource));

			drawMaze(maze);
			drawLongestPath(maze);
		}
	}

	private void drawMaze(Maze maze) {
		referenceEqualsActual(s -> s.drawMaze(maze));
	}

	private void drawLongestPath(Maze maze) {
		List<CircleCoordinate> path = new MazeSolver(maze).maxPaths().iterator().next();

		referenceEqualsActual(s -> s.drawPath(path));

		List<CircleCoordinate> reversed = new ArrayList<>(path);
		Collections.reverse(reversed);
		referenceEqualsActual(s -> s.drawPath(reversed));
	}

	@Test
	public void drawCircles() {
		referenceEqualsActual(s -> s.drawDotOnPath(CircleCoordinate.create(0, 0)));
		referenceEqualsActual(s -> s.drawDotOnPath(CircleCoordinate.create(4, 2)));
	}

	private void referenceEqualsActual(Consumer<Drawer> consumer) {
		Drawing drawingReference = new Drawing();
		Drawer drawerReference = new OldAndUglyButWorkingReferenceDrawer(25, new DrawingMaker(drawingReference));
		consumer.accept(drawerReference);

		Drawing drawingActual = new Drawing();
		Drawer drawerActual = new DrawerOnCanvas(25, new DrawingMaker(drawingActual));
		consumer.accept(drawerActual);

		assertEquals(drawingReference, drawingActual);
	}

	private static final class DrawingMaker implements Canvas {

		final Drawing drawing;

		DrawingMaker(Drawing drawing) {
			this.drawing = drawing;
		}

		@Override
		public void line(PolarCoordinate from, int length) {
			drawing.addLine(Line.create(from, from.getRadius() + length));
		}

		@Override
		public void circle(PolarCoordinate center, int diameter) {
			drawing.addCircle(Circle.create(center, diameter));
		}

		@Override
		public void arc(PolarCoordinate from, Fraction span) {
			drawing.addArc(Arc.create(from, span));
		}
	}
}
