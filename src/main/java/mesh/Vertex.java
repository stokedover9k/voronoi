package mesh;

import java.util.LinkedList;
import java.util.List;

import util.Vecs.Vec;



public class Vertex {
	Vec location = null;
	Edge edge = null;     // Vertex points to an OUTGOING edge

	public Vertex(Vec location) {
		this.location = location;
	}

	public Vertex(Vec location, Edge edge) {
		this.location = location;
		this.edge = edge;
	}

	////////////////////////////////////////////////////////////////
	
	public List<Edge> getEdges() {
		List<Edge> edges = new LinkedList<Edge>();
		Edge e = this.edge;
		do {
			e = e.opposite.next;
			edges.add(e);
		} while( e != this.edge );
		return edges;
	}

	public List<Face> getFaces() {
		List<Face> faces = new LinkedList<Face>();
		Edge e = this.edge;
		do {
			e = e.opposite.next;
			if( e.face != null )
				faces.add(e.face);
		} while( e != this.edge );
		return faces;
	}
	
	public String toString() {
		return "V{" + location.toString() + "}";
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
	
	public int compareTo(Vertex v) {
		return location.compareTo(v.location);
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public Edge getEdge() {
		return edge;
	}
	
	public Vec getLocation() {
		return location;
	}
	
	public Vertex midpointTo(Vertex vertex) {
		return new Vertex(location.midpointTo(vertex.location));
	}
}

