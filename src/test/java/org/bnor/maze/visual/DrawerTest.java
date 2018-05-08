package org.bnor.maze.visual;

import static org.junit.Assert.assertEquals;

import org.bnor.euler.Fraction;
import org.bnor.maze.Maze;
import org.bnor.maze.MazeJson;
import org.junit.Test;

@SuppressWarnings("static-method")
public class DrawerTest {

	@Test
	public void one() {
		Maze maze = MazeJson.deserialize(MazeJsonReader.read("maze_3_1.json"));

		assertEquals(drawReference(maze), drawActual(maze));
	}

	private Drawing drawActual(Maze maze) {
		Drawing drawing = new Drawing();
		new Drawer(25, new DrawingMaker(drawing)).drawMaze(maze);

		return drawing;
	}

	private Drawing drawReference(Maze maze) {
		Drawing drawing = new Drawing();
		new OldAndUglyButWorkingReferenceDrawer(25, new DrawingMaker(drawing)).drawMaze(maze);

		return drawing;
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
			throw new IllegalStateException("just drawing a maze");
		}

		@Override
		public void arc(PolarCoordinate from, Fraction span) {
			drawing.addArc(Arc.create(from, span));
		}
	}
}
