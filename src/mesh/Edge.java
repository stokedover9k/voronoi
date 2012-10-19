package mesh;


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
	
	public String toString() {
		return "E<" + vertex + "->" + (Object)next + ":" + (Object)face + ">"; 
	}
}