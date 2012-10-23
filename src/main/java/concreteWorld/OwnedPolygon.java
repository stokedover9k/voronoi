package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;

import java.util.List;

import mesh.Edge;
import mesh.Face;
import mesh.Faces;
import util.Locations.Loc2d;
import util.Vecs.Vec;


public  class OwnedPolygon extends Face {
	
	Actor owner = null;
	Vec stone = null;
	List<GridForOwnedPolygon> grids;
	double weight;//was suppose to represent weight for evaluation but now 
	//represents area

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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}

