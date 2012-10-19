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
		Edge e = this.edge;
		do {
			e = e.next;
			edges.add(e);
		} while( e != this.edge );
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
	
	public String toString() {
		return "F" + getEdge().getVertex();
	}
}
