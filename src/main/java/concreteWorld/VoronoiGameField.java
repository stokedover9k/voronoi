package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;
import gameworld.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mesh.Edge;
import mesh.Face;
import mesh.Faces;
import mesh.Mesh;
import mesh.Vertex;
import util.Locations.Loc;
import util.Locations.Loc2d;
import util.Vecs;
import util.Vecs.Ray;
import util.Vecs.Vec;

public class VoronoiGameField extends Mesh<OwnedPolygon> {

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


	public OwnedPolygon placeStone( Stone stone, Actor owner ) {
		double minDistance = Double.POSITIVE_INFINITY;
		OwnedPolygon nearestPolygon = null;
		Loc newStoneLoc = new Loc2d((double)stone.x, (double)stone.y);

		for( OwnedPolygon polygon : getFaces() ) {
			double distance = polygon.getStone().minus(newStoneLoc).norm();
			if( distance < minDistance ) {
				minDistance = distance;
				nearestPolygon = polygon;
			}
		}

		try {
			return insertStoneIntoPolygon( nearestPolygon, stone, owner );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Data Structure collapsed");
			return new OwnedPolygon(null, null);
		}
	}

	private OwnedPolygon insertStoneIntoPolygon(OwnedPolygon polygon, Stone stone, Actor owner) throws Exception {

		OwnedPolygon newPolygon = null;
		try{
			Vec newStone = stone.toVec();
			Vec midpoint = newStone.midpointTo(polygon.getStone());
			Vec perpendicular = Vecs.perpendicular(midpoint.minus(newStone));
			Ray intersectingRay = new Ray(midpoint, perpendicular);

			Map<Edge, Vec> intersections = polygon.getIntersections(intersectingRay);
			if( intersections.size() != 2 ) {
				throw new RuntimeException("expected 2 intersect points");
			}

			Iterator<Edge> edgeIterator = intersections.keySet().iterator();
			Edge e1 = edgeIterator.next();
			Edge e2 = edgeIterator.next();

			Vertex v1 = e1.split( new Vertex( intersections.get(e1) ) );
			Vertex v2 = e2.split( new Vertex( intersections.get(e2) ) );

			Ray l = new Ray(newStone, midpoint.minus(newStone));
			if( isLeftOrRight( l, v1.getLocation() ) > 0 ) {  // if v1 is on the left ---> swap v1 and v2
				Vertex tmp = v1;
				v1 = v2;
				v2 = tmp;
			}

			System.err.println("cut from and to:  " + v2 + " " + v1);

			newPolygon = new OwnedPolygon(null, null, newStone);

			if( polygon.cut(v2, v1, newPolygon) != newPolygon ) {
				throw new RuntimeException( "Was supposed to return newPolygon" );
			}

			registerFace(newPolygon);
			System.err.println("registerd new face: " + newPolygon);
			System.err.println("while cutting this face: " + polygon);

			validate();

			/*
			 *  current = start
			 *  
			 *  while not cut to v1
			 *  	
			 *  	if next.opp.face = null
			 *  		move on
			 *  	
			 *  	if next.opp.face != start.prev.opp.face
			 *  		cut to new v3
			 *  	else
			 *  		cut to v1
			 *  
			 *  	if opp.face != null
			 *  		cut from v3
			 *  	else
			 *  		cut from new v2
			 */

			// find edge which points to v2
			//		Edge startEdge = null;
			//		for( Edge edge : newPolygon.getEdges() )
			//			if( edge.getVertex() == v2 ) {
			//				startEdge = edge;
			//				break;
			//			}
			//		
			//		Edge previousToStart = startEdge.getPrevious();
			//		Edge current = startEdge;
			//		Vertex v3 = v2;
			//		
			//		do {
			//			
			//			if( current.getNext() == startEdge ) {
			//				break;
			//			}
			//			
			//			if( current.getNext().getOpposite().getFace() == null ) {
			//				current = current.getNext();
			//				continue;
			//			}
			//			
			//			Face faceToCut = current.getNext().getOpposite().getFace();
			//			System.err.println("cutting face: " + faceToCut.getEdge());
			//			Vec stoneToStone = ((OwnedPolygon)faceToCut).getStone().minus(newStone); 
			//			Ray stoneToStoneRay = new Ray( newStone, stoneToStone.dir() );
			//			Vec cutDirection = Vecs.perpendicular(stoneToStone);
			//			intersections = faceToCut.getIntersections( new Ray(v2.getLocation(), cutDirection) );
			//			
			//			if( current.getOpposite().getFace() != null ) {
			//				// cut to old v3
			//				System.out.println("v2 = old v3");
			//				v2 = v3;
			//				System.out.println(current);
			//			}
			//			else {
			//				// cut to new v2
			//				System.out.println("v2 = new v2");
			//				Map.Entry<Edge, Vec> cutFrom = getRightMost( intersections, stoneToStoneRay );
			//				v2 = cutFrom.getKey().split( new Vertex(cutFrom.getValue()) );
			//			}
			//			
			//			if( current.getNext().getOpposite().getFace() != previousToStart.getOpposite().getFace() ) {
			//				// find new v2
			//				Map.Entry<Edge, Vec> cutTo = getLeftMost( intersections, stoneToStoneRay );
			//				v3 = cutTo.getKey().split( new Vertex(cutTo.getValue()) );
			//			}
			//			else {
			//				v3 = v1;
			//			}
			//			
			//			OwnedPolygon conqueredFromCut = new OwnedPolygon(null, owner, null);
			//			System.out.println( "cutting from and to: " + v3 + " " + v2 );
			//			Face newFace = faceToCut.cut( v3, v2, conqueredFromCut );
			//			assert newFace == conqueredFromCut : "unexpected face returned from cut";
			//			
			//			Face removedFace = removeEdge( current.getNext() );
			//			System.err.println("removed during insertion: " + removedFace);
			//		} while( v3 != v1 );
			//		

			//before returning go through all the edges of the polygon and provide them the required alignment 
			for(Edge edge : newPolygon.getEdges()){
				//for each edge check if the rotation is required 

				//roatation is required for an edge is any of the vertices are 
				// not equidistant from the adjacent stones

				if(edge.getFace() != null){ // dont cross boundry
					OwnedPolygon myPoly = (OwnedPolygon) edge.getFace();

					if(edge.getOpposite().getFace() != null){
						OwnedPolygon adjacentPolygon = (OwnedPolygon)edge.getOpposite().getFace();

						if(roataionRequired(myPoly,adjacentPolygon,edge)){
							roatateEdgeToBalancePolygon(myPoly,adjacentPolygon,edge);
						}

					}
				}

			}

		}catch(Exception e){System.err.println("Unable to insert stone");};
		return newPolygon;

	}

	private void roatateEdgeToBalancePolygon(OwnedPolygon myPoly,
			OwnedPolygon adjacentPolygon, Edge edge) {

		//find the fixed point

		Vec oneEnd = edge.getVertex().getLocation();
		double d1 = getEuclideanDistanceFromVec(myPoly.getStone(),oneEnd);
		double d2 = getEuclideanDistanceFromVec(adjacentPolygon.getStone(),oneEnd);

		Vec otherend = edge.getOpposite().getVertex().getLocation();
		double d3 = getEuclideanDistanceFromVec(myPoly.getStone(),otherend);
		double d4 = getEuclideanDistanceFromVec(adjacentPolygon.getStone(),otherend);

		Vec midpoint = myPoly.getStone().midpointTo(adjacentPolygon.getStone());
		Vec perpendicular = Vecs.perpendicular(midpoint.minus(myPoly.getStone()));

		Ray intersectingRay;
		if(Math.abs(d1-d2) > 1){
			intersectingRay = new Ray(otherend, perpendicular);
		}
		else{
			intersectingRay = new Ray(oneEnd, perpendicular);	
		}

		Map<Edge, Vec> intersections = adjacentPolygon.getIntersections(intersectingRay);
		if( intersections.size() != 2 ) {
			throw new RuntimeException("expected 2 intersect points");
		}

		Iterator<Edge> edgeIterator = intersections.keySet().iterator();
		Edge e1 = edgeIterator.next();
		Edge e2 = edgeIterator.next();

		Vertex v1 = e1.split( new Vertex( intersections.get(e1) ) );
		Vertex v2 = e2.split( new Vertex( intersections.get(e2) ) );

		//out of these two vertexes one vertex is gonna be same as the
		if(Math.abs(d1-d2) > 1){
			//otherEnd is fixed

			//check if other end is equal to v1 if yes make v2 coincide with oneend
			if(checkequals(otherend,v1)){
				assing(oneEnd,v2);
			}else {
				assing(oneEnd,v1);
			}


		}else{
			//other end is fixed
			if(checkequals(oneEnd,v1)){
				assing(otherend,v2);
			}else {
				assing(otherend,v1);
			}

		}

	}

	private void assing(Vec otherend, Vertex v2) {
		// TODO Auto-generated method stub
		otherend.set(0, v2.getLocation().get(0)) ;
		otherend.set(0, v2.getLocation().get(0)) ;
	}

	private boolean checkequals(Vec oneEnd, Vertex v1) {
		// TODO Auto-generated method stub
		boolean returnValue = true;

		if(oneEnd.get(0) != v1.getLocation().get(0) || oneEnd.get(1) != v1.getLocation().get(1)){

			returnValue = false;
		}


		return returnValue;
	}

	private static double getEuclideanDistanceFromVec(Vec point1,Vec point2){

		double x1 = point1.get(0);
		double y1 = point1.get(1);
		double x2 = point2.get(0);
		double y2 = point2.get(1);
		return  Math.sqrt(( Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));

	}
	private boolean roataionRequired(OwnedPolygon myPoly,
			OwnedPolygon adjacentPolygon, Edge edge) {

		boolean returnValue = false;

		Vec oneEnd = edge.getVertex().getLocation();
		double d1 = getEuclideanDistanceFromVec(myPoly.getStone(),oneEnd);
		double d2 = getEuclideanDistanceFromVec(adjacentPolygon.getStone(),oneEnd);

		Vec otherend = edge.getOpposite().getVertex().getLocation();
		double d3 = getEuclideanDistanceFromVec(myPoly.getStone(),otherend);
		double d4 = getEuclideanDistanceFromVec(adjacentPolygon.getStone(),otherend);


		if(!(Math.abs(d1-d2) < 1) && (Math.abs(d3-d4) <1)){
			returnValue = true;
		}

		return returnValue;
	}

	private Map.Entry<Edge, Vec> getLeftMost(Map<Edge, Vec> points, Ray relativeTo) {
		double max = Double.NEGATIVE_INFINITY;
		Map.Entry<Edge, Vec> leftMost = null;
		for( Map.Entry<Edge, Vec> entry : points.entrySet() ) {
			double val = isLeftOrRight( relativeTo, entry.getValue() );
			if( val > max ) {
				max = val;
				leftMost = entry;
			}
		}
		return leftMost;
	}

	private Map.Entry<Edge, Vec> getRightMost(Map<Edge, Vec> points, Ray relativeTo) {
		double min = Double.POSITIVE_INFINITY;
		Map.Entry<Edge, Vec> leftMost = null;
		for( Map.Entry<Edge, Vec> entry : points.entrySet() ) {
			double val = isLeftOrRight( relativeTo, entry.getValue() );
			if( val < min ) {
				min = val;
				leftMost = entry;
			}
		}
		return leftMost;
	}

	// finds the determinant of the vector provided by the ray and the vector m
	// returns 0 if m is on the line given by l.
	// returns > 0 if left, < 0 if right
	private static double isLeftOrRight( Ray l, Vec m ) {
		assert l.getPoint().getDegree() == 2 && m.getDegree() == 2 : "Invalid vectors";

		Vec a = l.getPoint();
		Vec b = a.plus(l.getDir());

		double ax = a.get(0);
		double ay = a.get(1);
		double bx = b.get(0);
		double by = b.get(1);
		double  x = m.get(0);
		double  y = m.get(1);

		double det = (bx - ax) * (y - ay) - (by - ay) * (x - ax);
		return det;
	}





	public static void main(String[] args) {

		VoronoiGameField game = new VoronoiGameField();

		System.out.println(game);

		game.getFaces().iterator().next().setStone(new Stone(1,1));

		game.placeStone(new Stone(3, 5), null);
		game.validate();
		System.out.println(game);

		game.placeStone(new Stone(5, 3), null);
		game.validate();
		System.out.println("final "+game);
	}

	public Collection<OwnedPolygon> getAllPolygons() {
		return getFaces();
	}
}
