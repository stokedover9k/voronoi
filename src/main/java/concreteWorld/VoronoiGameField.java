package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;
import gameworld.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mesh.Edge;
import mesh.Face;
import mesh.Faces;
import mesh.Mesh;
import mesh.Vertex;
import util.Locations.Loc;
import util.Locations.Loc2d;
import util.Vecs;
import util.Vecs.Ray;
import util.Vecs.Vec;

public class VoronoiGameField extends Mesh<VoronoiGameField.OwnedPolygon> {
	
	public VoronoiGameField(Map<Integer, Vertex> vertexMapping,
			Collection<LinkedList<Integer>> facesList,
			Faces.Factory<OwnedPolygon> faceFactory) {
		super(vertexMapping, facesList, faceFactory);
	}
	
	public VoronoiGameField() {
		this( defaultVertexMapping(), defaultFacesList(), new OwnedPolygonFactory( null ) );
	}
	
	public VoronoiGameField( VoronoiGameField fieldToClone ) {
		super( fieldToClone, new OwnedPolygonFactory(null) );
	}

	private static Map<Integer, Vertex> defaultVertexMapping() {
		Map<Integer, Vertex> vertexMapping = new HashMap<Integer, Vertex>();
		vertexMapping.put(0, new Vertex( new Vec(new Loc2d(0, 0) )));
		vertexMapping.put(1, new Vertex( new Vec(new Loc2d(World.WIDTH, 0) )));
		vertexMapping.put(2, new Vertex( new Vec(new Loc2d(World.WIDTH, World.HEIGHT) )));
		vertexMapping.put(3, new Vertex( new Vec(new Loc2d(0, World.HEIGHT) )));
		return vertexMapping;
	}
	
	private static Collection<LinkedList<Integer>> defaultFacesList() {
		Collection<LinkedList<Integer>> facesList = new ArrayList<LinkedList<Integer>>(1);
		facesList.add( new LinkedList<Integer>( Arrays.asList( 0, 1, 2, 3 )) );
		return facesList;
	}
	
	
	public OwnedPolygon placeStone( Stone stone, Actor owner ) {
		double minDistance = Double.POSITIVE_INFINITY;
		OwnedPolygon nearestPolygon = null;
		Loc newStoneLoc = new Loc2d((double)stone.x, (double)stone.y);
		
		for( OwnedPolygon polygon : getFaces() ) {
			double distance = polygon.getStone().minus(newStoneLoc).norm();
			if( distance < minDistance ) {
				minDistance = distance;
				nearestPolygon = polygon;
			}
		}
		
		return insertStoneIntoPolygon( nearestPolygon, stone, owner );
	}
	
	private OwnedPolygon insertStoneIntoPolygon(OwnedPolygon polygon, Stone stone, Actor owner) {
		// TODO Auto-generated method stub
		
		Vec newStone = stone.toVec();
		Vec midpoint = newStone.midpointTo(polygon.getStone());
		Vec perpendicular = Vecs.perpendicular(midpoint.minus(newStone));
		Ray intersectingRay = new Ray(midpoint, perpendicular);
		
		Map<Edge, Vec> intersections = polygon.getIntersections(intersectingRay);
		if( intersections.size() != 2 ) {
			throw new RuntimeException("expected 2 intersect points");
		}
		
		Iterator<Edge> edgeIterator = intersections.keySet().iterator();
		Edge e1 = edgeIterator.next();
		Edge e2 = edgeIterator.next();
		
		Vertex v1 = e1.split( new Vertex( intersections.get(e1) ) );
		Vertex v2 = e2.split( new Vertex( intersections.get(e2) ) );
		
		System.err.println( "Intersects: " + v1 + " and " + v2 );
		
		OwnedPolygon newPolygon = new OwnedPolygon(null, null, newStone); 
		if( polygon.cut(v1, v2, newPolygon) != newPolygon ) {
			throw new RuntimeException( "Was supposed to return newPolygon" );
		}
		registerFace(newPolygon);
		return newPolygon;
	}

	public static class OwnedPolygon extends Face {
		
		Actor owner = null;
		Vec stone = null;
		List<GridForOwnedPolygon> grids;

		public OwnedPolygon(Edge edge, Actor owner, Vec stone) {
			super(edge);
			this.owner = owner;
			this.stone = stone;
		}
		
		public OwnedPolygon(Edge edge, Actor owner) {
			this( edge, owner, null );
		}
		
		public Actor getOwner() {
			return owner;
		}
		
		public Vec getStone() {
			return stone;
		}
		
		public void setOwner(Actor owner) {
			this.owner = owner;
		}

		public void setStone(Stone stone) {
			this.stone = new Vec( new Loc2d((double)stone.x, (double)stone.y));
		}
	}

	public static class OwnedPolygonFactory implements Faces.Factory<OwnedPolygon> {
		
		final Actor owner;

		public OwnedPolygonFactory(Actor owner) {
			this.owner = owner;
		}

		@Override
		public OwnedPolygon createFace(Edge edge) {
			return new OwnedPolygon(edge, owner);
		}

		@Override
		public OwnedPolygon cloneFace(OwnedPolygon face) {
			return new OwnedPolygon(face.getEdge(), face.getOwner());
		}

	}
	
	
	
	public static void main(String[] args) {
		VoronoiGameField game = new VoronoiGameField();
		
		System.out.println(game);
		
		game.getFaces().iterator().next().setStone(new Stone(1,1));
		
		game.placeStone(new Stone(3, 5), null);
		
		game.validate();
		
		System.out.println(game);
	}
}
