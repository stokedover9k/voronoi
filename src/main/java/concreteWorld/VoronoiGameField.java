package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;
import gameworld.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import mesh.Edge;
import mesh.Face;
import mesh.Faces;
import mesh.Faces.Factory;
import mesh.Mesh;
import mesh.Vertex;
import util.Locations.Loc2d;
import util.Vecs.Vec;
import concreteWorld.VoronoiGameField.OwnedPolygon;

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
	
	public static class OwnedPolygon extends Face {
		
		Actor owner = null;
		Vec stone = null;

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
	}

	public static class OwnedPolygonFactory implements Factory<OwnedPolygon> {
		
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
}
