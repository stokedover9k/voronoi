package mesh;

import java.util.LinkedList;
import java.util.List;

public class Face {
	Edge edge;

	public Face(Edge edge) {
		this.edge = edge;
	}
	
	/////////////////////////////////////////////////////////////
	
	public List<Edge> getEdges() {
		List<Edge> edges = new LinkedList<Edge>();
		if( edge == null ) return edges;
		Edge e = edge;
		do {
			e = e.next;
			edges.add(e);
		} while( e != edge );
		return edges;
	}
	
	public List<Vertex> getVertices() {
		List<Vertex> vertices = new LinkedList<Vertex>();
		Edge e = this.edge;
		do {
			e = e.next;
			vertices.add(e.vertex);
		} while( e != this.edge );
		return vertices;
	}
	
	public List<Face> getFaces() {
		List<Face> faces = new LinkedList<Face>();
		Edge e = this.edge;
		do {
			e = e.next;
			if( e.opposite.face != null )
				faces.add(e.opposite.face);
		} while( e != this.edge );
		return faces;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public Edge getEdge() {
		return edge;
	}
	
	public Face cut(Vertex fromVertex, Vertex toVertex, Face newFace) {
		Edge fromEdge = null, toEdge = null;
		for( Edge edge : getEdges() ) {
			if( edge.getVertex().equals(fromVertex) )
				fromEdge = edge;
			else if( edge.getVertex().equals(toVertex) )
				toEdge = edge;
		}
		if( fromEdge == null || toEdge == null ) 
			throw new IllegalArgumentException("at least one of the vertices doesn't belong to this face.");
		
		return cut( fromEdge, toEdge, newFace );
	}	
	
	// the two edges point to vertices used to make the cut
	public Face cut(Edge edgeFrom, Edge edgeTo, Face newFace) {
		
		Edge newEdge = new Edge(this, edgeTo.getVertex(), null, edgeTo.getNext());
		newEdge.setOpposite( new Edge(newFace, edgeFrom.getVertex(), newEdge, edgeFrom.getNext()) );
		newFace.setEdge(newEdge.getOpposite());
		
		edgeFrom.setNext(newEdge);
		edgeTo.setNext(newEdge.getOpposite());
		
		for( Edge edge = newFace.getEdge().getNext(); edge != newFace.getEdge(); edge = edge.getNext() )
			edge.setFace(newFace);
		
		return newFace;
	}
	
	public String toString() {
		return "F{" + getEdges() + "}";
	}
	
	public int hashCode() {
		return getEdges().hashCode();
	}
	
	public boolean equals(Object o) {
		return getEdges().equals(((Face)o).getEdges());
	}
}
