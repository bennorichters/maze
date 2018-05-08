package org.bnor.maze.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bnor.euler.Fraction;
import org.bnor.maze.Maze;
import org.bnor.maze.MazeJson;
import org.junit.Test;

@SuppressWarnings("static-method")
public class DrawerTest {

	@Test
	public void one() {
		Maze maze = MazeJson.deserialize(MazeJsonReader.read("maze_3_1.json"));

		drawIndirect(maze);
		drawDirect(maze);
	}

	private void drawDirect(Maze maze) {
		BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics.setPaint(Color.BLACK);
		
		Drawer drawer = new Drawer(25, new GraphicsCanvas(500 / 2, graphics));
		drawer.drawMaze(maze);
		
		try {
			ImageIO.write(image, "PNG", new File("d:\\temp\\direct.png"));
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	private void drawIndirect(Maze maze) {
		BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics.setPaint(Color.BLACK);

		Drawing drawing = new Drawing();
		Drawer drawingMaker = new Drawer(25, new Canvas() {

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
		});

		drawingMaker.drawMaze(maze);

		GraphicsCanvas canvas = new GraphicsCanvas(250, graphics);
		for (Arc arc : drawing.getArcs()) {
			canvas.arc(arc.getFrom(), arc.getSpan());
		}

		for (Line line : drawing.getLines()) {
			PolarCoordinate from = line.getFrom();
			canvas.line(from, line.getEndRadius() - from.getRadius());
		}

		try {
			ImageIO.write(image, "PNG", new File("d:\\temp\\indirect.png"));
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

}
