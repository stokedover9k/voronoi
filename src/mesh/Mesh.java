package mesh;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import mesh.Locations.Loc2d;

public class Mesh<F extends Face> {
	
	Collection<F> faces = new HashSet<F>();
	
	
	
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
				
				System.out.println( counter++ + " " + vertexIndex + " " + edge );
				
				
				edge.setNext( faceItr.hasNext() ? new Edge(face, vertexMapping.get(vertexIndex), null, null) : face.getEdge() );
				edge.getVertex().setEdge( edge.getNext() );
				// TODO: push current edge???  Nope, seems like it's different implementation
				
				String oppositeKey = vertexIndex+","+previousIndex;
				Edge oppositeEdge = edgeMap.get(oppositeKey);
				if( oppositeEdge != null ) {
					edge.setOpposite(oppositeEdge);
					oppositeEdge.setOpposite(edge);
					edgeMap.remove(oppositeKey);
				}
				else {
					edgeMap.put(previousIndex+","+vertexIndex, edge);
				}
				
				previousIndex = vertexIndex;
				edge = edge.getNext();
				
			} while( faceItr.hasNext() );
			
			faces.add( face );
		}
		
		System.out.println(faces);
		
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
	
	
	
	public static void main(String[] args) {
		
		Map<Integer, Vertex> v = new HashMap<Integer, Vertex>();
		v.put(0, new Vertex(new Loc2d(-5, -5)));
		v.put(1, new Vertex(new Loc2d( 5, -5)));
		v.put(2, new Vertex(new Loc2d( 5,  5)));
		v.put(3, new Vertex(new Loc2d(-5,  5)));
		v.put(4, new Vertex(new Loc2d( 0,  8)));
		
		Collection<LinkedList<Integer>> f = new LinkedList<LinkedList<Integer>>();
		f.add( new LinkedList<Integer>( Arrays.asList(0, 1, 2, 3 ) ) );
		f.add( new LinkedList<Integer>( Arrays.asList(3, 2, 4 ) ) );
		
		Mesh<Face> mesh = new Mesh<Face>( v, f, new Faces.FaceFactory() );
		
		System.out.println(mesh);
		
	}
}
