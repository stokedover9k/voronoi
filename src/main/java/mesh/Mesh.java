package mesh;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Locations.Loc2d;
import util.Vecs.Vec;



public class Mesh<F extends Face> {
	
	Collection<F> faces = new HashSet<F>();
	
	
	
	// Copy constructor (deep copy)
	public Mesh( Mesh<F> otherMesh, Faces.Factory<F> faceFactory ) {
		Map<Vertex, Vertex> vertices = new IdentityHashMap<Vertex, Vertex>();
		Map<Edge, Edge> edges = new IdentityHashMap<Edge, Edge>();
		Map<F, F> faces = new IdentityHashMap<F, F>();
		
		Set<Edge> boundaryEdges = new HashSet<Edge>();
		
		for( F face : otherMesh.faces ) {
			F clonedFace = faceFactory.cloneFace( face );
			faces.put( face, clonedFace );
			this.faces.add(clonedFace);
			
			for( Edge edge : face.getEdges() ) {
				Vertex clonedVertex = vertices.get( edge.getVertex() );
				if( clonedVertex == null ) {
					clonedVertex = new Vertex( edge.getVertex().getLocation() );
					vertices.put( edge.getVertex(), clonedVertex );
				}
				Edge clonedEdge = new Edge(clonedFace, clonedVertex, null, null);
				if( edge.getVertex().getEdge() == edge )
					clonedVertex.setEdge( clonedEdge );
				else if( edge.getVertex().getEdge().getFace() == null ) {
					boundaryEdges.add(edge.getVertex().getEdge());
				}
				
				edges.put(edge, clonedEdge);
			}
			
			clonedFace.setEdge( edges.get(face.getEdge()) );
		}
		
//		List<Edge> clonedBoundary = new LinkedList<Edge>();
		
		for( Map.Entry<Edge, Edge> edgeMapping : edges.entrySet() ) {
			Edge originalEdge = edgeMapping.getKey();
			Edge clonedEdge = edgeMapping.getValue();
			
			/*
			Edge originalOpposite = originalEdge.getOpposite();
			if( originalOpposite.getFace() == null ) {
				Edge clonedOpposite = new Edge(null, vertices.get(originalOpposite.getVertex()), clonedEdge, null);
				clonedBoundary.add( clonedOpposite );
				clonedEdge.setOpposite( clonedOpposite );
			}
			else {
			*/
			clonedEdge.setOpposite( edges.get(originalEdge.getOpposite()) );
//			}
			clonedEdge.setNext( edges.get(originalEdge.getNext()) );
		}
		
		List<Edge> clonedBoundary = new LinkedList<Edge>();
		for( Edge boundaryEdge : boundaryEdges ) {
			Edge clonedEdge = new Edge(null, null, null, null);
			clonedEdge.setVertex( vertices.get(boundaryEdge.getVertex()) );
			clonedEdge.setOpposite( edges.get(boundaryEdge.getOpposite()) );

			edges.get(boundaryEdge.getOpposite()).setOpposite( clonedEdge );
			clonedEdge.getOpposite().getVertex().setEdge( clonedEdge );
			
			clonedBoundary.add(clonedEdge);
		}
		
		for( Edge clonedBoundaryEdge : clonedBoundary ) {
			clonedBoundaryEdge.setNext( clonedBoundaryEdge.getVertex().getEdge() );
		}
	}
	
	// Takes a Map of vertices with keys (integers), and a collection of faces represented as lists
	// of keys to the vertices in the map.
	public Mesh( Map<Integer, Vertex> vertexMapping, Collection<LinkedList<Integer>> facesList, Faces.Factory<F> faceFactory ) {
		
		Map<String, Edge> edgeMap = new HashMap<String, Edge>();
		
		for( LinkedList<Integer> faceList : facesList ) {
			
			F face = faceFactory.createFace(new Edge(null, vertexMapping.get(faceList.getLast()), null, null) );
			face.getEdge().setFace( face );
			
			Edge edge = face.getEdge();
			
			Iterator<Integer> faceItr = faceList.iterator();
			Integer previousIndex = faceList.getLast();
			
			int counter = 1;
			do {
				Integer vertexIndex = faceItr.next();
				
				edge.setNext( faceItr.hasNext() ? new Edge(face, vertexMapping.get(vertexIndex), null, null) : face.getEdge() );
				edge.getVertex().setEdge( edge.getNext() );
				// TODO: push current edge???  Nope, seems like it's different implementation
				
				System.out.println( counter++ + " " + vertexIndex + " " + edge );
				
				
				String oppositeKey = vertexIndex+","+previousIndex;
				Edge oppositeEdge = edgeMap.get(oppositeKey);
				if( oppositeEdge != null ) {
					edge.getNext().setOpposite(oppositeEdge);
					oppositeEdge.setOpposite(edge.getNext());
					edgeMap.remove(oppositeKey);
				}
				else {
					edgeMap.put(previousIndex+","+vertexIndex, edge.getNext());
				}
				
				System.out.println(edgeMap);
				
				previousIndex = vertexIndex;
				edge = edge.getNext();
				
			} while( faceItr.hasNext() );
			
			faces.add( face );
		}
		
		// handle boundaries
		// 1. create boundary edges
		for( Map.Entry<String, Edge> entry : edgeMap.entrySet() ) {
			Edge a = entry.getValue();
			a.setOpposite( new Edge(null, a.getPrevious().getVertex(), a, null) );
			a.getVertex().setEdge( a.getOpposite() );
		}
		// 2. link boundary edges to next
		for( Map.Entry<String, Edge> entry : edgeMap.entrySet() ) {
			entry.getValue().getOpposite().setNext(entry.getValue().getOpposite().getVertex().getEdge());
			// TODO: push edge (getValue.getOpposite)???  Nope, seems like it's different implementation.
		}
		
	}
	
	
	
	// Removes the edge, thus extending the face it is associated with.
	// @return: The deleted face on the other side of the edge.
	public Face removeEdge( Edge edge ) {
		if( edge.getFace() == null )
			return removeBoundaryEdge( edge );
		if( edge.getOpposite().getFace() == null )
			return removeBoundaryEdge( edge.getOpposite() );
		
		return removeInnerEdge( edge );
	}

	// Removes the edge, thus extending the null-face it is associated with.
	// @return: the deleted face onthe other side of the edge.
	private Face removeBoundaryEdge( Edge edge ) {
		if( edge.getFace() != null )
			throw new IllegalArgumentException("The edge is not a boundary edge");
		if( edge.getOpposite().getFace().getFaces().isEmpty() )
			throw new IllegalArgumentException("May not delete the last face using an edge");
		
		Edge lastHangingOuterEdge = edge;
		while( lastHangingOuterEdge.getNext().getOpposite().getFace() == edge.getOpposite().getFace() )
			lastHangingOuterEdge = lastHangingOuterEdge.getNext();
		
		Edge lastHangingInnerEdge = edge.getOpposite();
		while( lastHangingInnerEdge.getNext().getOpposite().getFace() == null )
			lastHangingInnerEdge = lastHangingInnerEdge.getNext();
		
		for( Edge e = lastHangingInnerEdge.getNext(); e.getVertex() != lastHangingOuterEdge.getVertex(); e = e.getNext() )
			if( e.getVertex().getEdge().getFace() == null )
				throw new IllegalArgumentException("May not delete this face because it connects two corners.");
		
		faces.remove(lastHangingInnerEdge.getFace());
		
		lastHangingOuterEdge.getOpposite().getPrevious().setNext( lastHangingOuterEdge.getNext() );
		lastHangingInnerEdge.getOpposite().getPrevious().setNext( lastHangingInnerEdge.getNext() );
		lastHangingInnerEdge.getVertex().setEdge( lastHangingInnerEdge.getNext() );
		
		for( Edge e = lastHangingInnerEdge.getNext(); e != lastHangingOuterEdge.getNext(); e = e.getNext() ) {
			e.setFace( null );
			e.getVertex().setEdge( e.getNext() );
		}
		
		lastHangingInnerEdge.getFace().setEdge(null);
		return lastHangingInnerEdge.getFace();
	}
	
	// Removes the edge, thus extending the face it is associated with.
	// @return: The deleted face on the other side of the edge.
	private Face removeInnerEdge( Edge edge ) {
		Face conqueredFace = edge.getOpposite().getFace();
		
		// reassign edges of the adjacent face before we start messing with the links
		for( Edge conqueredEdge : conqueredFace.getEdges() )
			conqueredEdge.setFace(edge.getFace());		
		
		edge.getPrevious().setNext( edge.getOpposite().getNext() );
		edge.getOpposite().getPrevious().setNext( edge.getNext() );
		
		// in case the face was pointing to this edge
		edge.getFace().setEdge( edge.getNext() );
		
		return conqueredFace;
	}
	
	public String toString() {
		return faces.toString();
	}
	
	public boolean validate() {
		Edge boundaryEdge = null;
		int boundaryEdgeCount = 0;
		
		for( Face face : faces ) {
			for( Edge edge : face.getEdges() ) {
				assert edge == edge.getOpposite().getOpposite() : "mismatched opposites";
				assert edge.getVertex().getEdges().contains(edge.getOpposite()) : "opposite not linked to vertex";
				if( edge.getOpposite().getFace() == null ) {
					boundaryEdge = edge.getOpposite();
					boundaryEdgeCount++;
					assert edge.getFace() != null : "both half-edges have null face";
				}
			}
		}
		
		if( boundaryEdge != null ) {
			Edge edge = boundaryEdge;
			do {
				boundaryEdgeCount--;
				edge = edge.getNext();
			} while( edge != boundaryEdge );
			assert boundaryEdgeCount == 0 : "missing boundarie edges " + boundaryEdgeCount;
		}
		
		return true;
	}
	
	
	public static void main(String[] args) {
		
		Map<Integer, Vertex> v = new HashMap<Integer, Vertex>();
		v.put(0, new Vertex(new Vec(new Loc2d(-5, -5))));
		v.put(1, new Vertex(new Vec(new Loc2d( 5, -5))));
		v.put(2, new Vertex(new Vec(new Loc2d( 5,  5))));
		v.put(3, new Vertex(new Vec(new Loc2d(-5,  5))));
		v.put(4, new Vertex(new Vec(new Loc2d( 0,  8))));
		
		Collection<LinkedList<Integer>> f = new LinkedList<LinkedList<Integer>>();
		f.add( new LinkedList<Integer>( Arrays.asList(0, 1, 2, 3 ) ) );
		f.add( new LinkedList<Integer>( Arrays.asList(3, 2, 4 ) ) );
		
		Mesh<Face> mesh = new Mesh<Face>( v, f, new Faces.FaceFactory() );
		
		System.out.println(mesh);
		
		mesh.validate();
		
		Face faceToCut = null;
		List<Vertex> vertices = null;
		for( Face face : mesh.faces ) {
			vertices = face.getVertices();
			if( vertices.size() > 3 ) {
				faceToCut = face;
				break;
			}
		}
		if( faceToCut != null ) {
			Face newFace = faceToCut.cut(vertices.get(0), vertices.get(2), new Face(null));
			mesh.faces.add( newFace );
			System.out.println("new face added: " + newFace);
			mesh.validate();
		}
		
		Face removedFace = mesh.removeEdge(mesh.faces.iterator().next().getEdge());
		removedFace.setEdge(null);
		System.out.println("removed face: " + removedFace);
		System.out.println(mesh);
		
		mesh.validate();
		//*/
		
		Mesh<Face> clonedMesh = new Mesh<Face>(mesh, new Faces.FaceFactory());
		clonedMesh.validate();
		System.out.println(clonedMesh);
		
		/*
		int cloneTimes = 10000000;
		Faces.FaceFactory faceFactory = new Faces.FaceFactory();
		for( int i=0; i<cloneTimes; i++ ) {
			new Mesh<Face>(mesh, faceFactory);
		}
		System.out.println("Done cloning mesh " + cloneTimes + " times");
		//*/
	}
}
