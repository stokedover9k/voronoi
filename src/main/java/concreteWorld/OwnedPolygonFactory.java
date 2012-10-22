package concreteWorld;

import gameworld.Actor;
import mesh.Edge;
import mesh.Faces;

public class OwnedPolygonFactory implements Faces.Factory<OwnedPolygon> {
	
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

