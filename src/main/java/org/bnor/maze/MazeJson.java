package org.bnor.maze;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public final class MazeJson {

	public static String serialize(Maze maze) {
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(Maze.class, new MazeSerializer());
		
		return gb.create().toJson(maze);
	}
	
	public static Maze deserialize(String json) {
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(Maze.class, new MazeDeserializer());
		return gb.create().fromJson(json, Maze.class);
	}
	
	private static final class MazeSerializer implements JsonSerializer<Maze> {
		@Override
		public JsonElement serialize(Maze src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject obj = new JsonObject();
			int circles = src.getCircles();

			obj.add("circles", new JsonPrimitive(circles));

			JsonArray arcs = new JsonArray();
			JsonArray lines = new JsonArray();
			for (int circle = 1; circle <= circles; circle++) {
				for (int arc = 0; arc < CircleCoordinate.calcTotalArcs(circle); arc++) {
					CircleCoordinate coordinate = CircleCoordinate.create(circle, arc);
					if (!src.isArcOpen(coordinate)) {
						arcs.add(circleCoordinate(coordinate));
					}
					if (!src.isLineOpen(coordinate)) {
						lines.add(circleCoordinate(coordinate));
					}
				}
			}
			
			obj.add("arcs", arcs);
			obj.add("lines", lines);

			return obj;
		}

		private static JsonElement circleCoordinate(CircleCoordinate src) {
			JsonObject obj = new JsonObject();
			obj.add("circle", new JsonPrimitive(src.getCircle()));
			obj.add("arc", new JsonPrimitive(src.getArcIndex()));

			return obj;
		}
	}
	
	private static final class MazeDeserializer implements JsonDeserializer<Maze> {

		@Override
		public Maze deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject obj = json.getAsJsonObject();
			int circles = obj.getAsJsonPrimitive("circles").getAsInt();
			
			Set<CircleCoordinate> arcs = array2Set(obj, "arcs");
			Set<CircleCoordinate> lines = array2Set(obj, "lines");
			
			return new Maze(circles, arcs, lines);
		}

		private static Set<CircleCoordinate> array2Set(JsonObject obj, String array) {
			Set<CircleCoordinate> result = new HashSet<>();
			JsonArray arcs = obj.getAsJsonArray(array);
			for (Iterator<JsonElement> it = arcs.iterator(); it.hasNext(); ) {
				JsonElement next = it.next();
				JsonObject coordinate = next.getAsJsonObject();
				int circle = coordinate.getAsJsonPrimitive("circle").getAsInt();
				int arc = coordinate.getAsJsonPrimitive("arc").getAsInt();
				
				result.add(CircleCoordinate.create(circle, arc));
			}
			
			return result;
		}
	}
}
