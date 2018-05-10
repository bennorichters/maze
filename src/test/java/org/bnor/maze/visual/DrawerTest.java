package org.bnor.maze.visual;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.bnor.euler.Fraction;
import org.bnor.maze.CircleCoordinate;
import org.bnor.maze.Maze;
import org.bnor.maze.MazeJson;
import org.bnor.maze.MazeSolver;
import org.junit.Test;

@SuppressWarnings("static-method")
public class DrawerTest {

	@Test
	public void drawSimpleMaze() {
		Maze maze = MazeJson.deserialize(MazeJsonReader.read("maze_3_1.json"));

		assertEquals(drawReferenceMaze(maze), drawActualMaze(maze));
	}
	
	private Drawing drawReferenceMaze(Maze maze) {
		Drawing drawing = new Drawing();
		new OldAndUglyButWorkingReferenceDrawer(25, new DrawingMaker(drawing)).drawMaze(maze);
	
		return drawing;
	}

	private Drawing drawActualMaze(Maze maze) {
		Drawing drawing = new Drawing();
		new Drawer(25, new DrawingMaker(drawing)).drawMaze(maze);
	
		return drawing;
	}

	@Test
	public void drawSolution() {
		Maze maze = MazeJson.deserialize(MazeJsonReader.read("maze_3_1.json"));

		List<CircleCoordinate> path = new MazeSolver(maze).maxPaths().iterator().next();
		
		Drawing drawingReference = new Drawing();
		new OldAndUglyButWorkingReferenceDrawer(25, new DrawingMaker(drawingReference)).drawPath(path);

		Drawing drawingActual = new Drawing();
		new Drawer(25, new DrawingMaker(drawingActual)).drawPath(path);
		
		assertEquals(drawingReference, drawingActual);
	}

	@Test
	public void drawCircles() {
		CircleCoordinate center = CircleCoordinate.create(4, 2);
		
		Drawing drawingReference = new Drawing();
		new OldAndUglyButWorkingReferenceDrawer(25, new DrawingMaker(drawingReference)).drawDotOnPath(center);
		
		Drawing drawingActual = new Drawing();
		new Drawer(25, new DrawingMaker(drawingActual)).drawDotOnPath(center);
		
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
