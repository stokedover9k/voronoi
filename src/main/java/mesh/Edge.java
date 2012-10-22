package mesh;

import util.Vecs;
import util.Vecs.Ray;
import util.Vecs.Vec;


public class Edge {
	Face face;
	Vertex vertex;
	Edge opposite;
	Edge next;

	public Edge(Face face, Vertex vertext, Edge opposite, Edge next) {
		this.face = face;
		this.vertex = vertext;
		this.opposite = opposite;
		this.next = next;
	}

	public Edge(Edge next, Edge opposite) {
		this.next = next;
		this.opposite = opposite;
		this.face = next.face;
		this.vertex = next.opposite.vertex;
	}

	///////////////////////////////////////////////////////////////////
	
	public void setFace(Face face) {
		this.face = face;
	}

	public void setNext(Edge next) {
		this.next = next;
	}
	
	public void setOpposite(Edge opposite) {
		this.opposite = opposite;
	}
	
	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public Vertex getVertex() {
		return vertex;
	}
	
	public Edge getNext() {
		return next;
	}
	
	public Edge getOpposite() {
		return opposite;
	}
	
	public Face getFace() {
		return face;
	}
	
	public Edge getPrevious() {
		Edge edge = this.next;
		while( edge.next != this )
			edge = edge.next;
		return edge;
	}
	
	public Vertex split() {
		Vertex newVertex = vertex.midpointTo(opposite.vertex);
		return split(newVertex);
	}
	
	public Vertex split(Vertex newVertex) {
		Edge newNext     = new Edge(face, vertex, opposite, next);
		Edge newOpposite = new Edge(opposite.face, opposite.vertex, this, opposite.next);
		
		this.opposite.next = newOpposite;
		this.opposite.vertex = newVertex;
		this.opposite.opposite = newNext;
		
		this.next = newNext;
		this.vertex = newVertex;
		this.opposite = newOpposite;
		
		newVertex.setEdge( face == null ? newNext : newOpposite );
		
		return newVertex;
	}
	
	public String toString() {
		return "E{" + (opposite == null ? "null" : opposite.vertex) + " -> " + vertex + "}";
	}

	public Vec getIntersect(Ray intersectingRay) {
		Vec a = getVertex().getLocation();
		Vec b = getOpposite().getVertex().getLocation();
		// Note, that we move point "a" to be relative to the intersecting ray's origin
		Ray edgeAsRay = new Ray( a.minus(intersectingRay.getPoint()), b.minus(a).normalize() );
		
		// the last "add" is to return the point to the original coordinate system (from being relative to intersecting ray)
		Vec intersection = Vecs.intersection(intersectingRay.getDir(), edgeAsRay).add(intersectingRay.getPoint());
		if( intersection == null )  return null;
		
		// only checking against one vertex because the previous edge should take care of the other
		if( intersection.equals(a) )  return intersection;
		
		// now check if we've hit within the edge.  Imagine points on a line (3 cases):
		// 1)         a --- T --- b
		// 2)  T' --- a --------- b
		// 3)         a --------- b --- T'
		// Now consider lengths of vectors: a->T, b->T, and (a->T + b->T)
		Vec aT = intersection.minus(a);
		Vec bT = intersection.minus(b);
		if( aT.plus(bT).norm() < aT.norm() + bT.norm() )
			return intersection;
		else
			return null;
	}
}