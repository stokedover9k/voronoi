package concreteWorld;

import java.util.ArrayList;
import java.util.List;

import mesh.Edge;
import mesh.Vertex;

import concreteWorld.VoronoiGameField.OwnedPolygon;
import util.Locations.Loc;
import util.Locations.Loc2d;
import util.Vecs.Vec;

public class GridForOwnedPolygon {

	Vec location;
	int width;

	public GridForOwnedPolygon(Vec location,int width){
		this.location = location;
		this.width = width;
	}

	@Override
	public String toString() {
		return "GridForOwnedPolygon [location=" + location + ", width=" + width
		+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridForOwnedPolygon other = (GridForOwnedPolygon) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	public Vec getLocation(){
		return location;
	}

	public int getWidth(){
		return width;
	}	

	public void setLocation(Vec location){
		this.location = location;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getArea(){
		return width*width;
	}

	private boolean doesBelongToPolygon(OwnedPolygon polygon){
		// TODO: took some liberties with this function so it would compile.  Please check.
		
		List<Vertex> vertices = polygon.getVertices();
		int polyX[] = new int[vertices.size()];
		int polyY[] = new int[vertices.size()];
		
		int i = 0;
		for( Vertex v : vertices ) {
//		for(int i = 0 ; i< vertices.size();i++){
			
			// this line is expensive!!!
//			Loc loc = vertices.get(i).getLocation();
			
			// added this next line
			Loc loc = v.getLocation();
			polyX[i] = (int) loc.get(0);
			polyY[i] = (int) loc.get(1);
			// added this next line
			i++;
		}
		
		return pointInPolygon(vertices.size(), polyX, polyY,  (int)this.location.get(0), (int)this.location.get(1));
	}

	public static List<GridForOwnedPolygon> getGridForPolygon(OwnedPolygon polygon,int width){

		List<GridForOwnedPolygon> returnValue = new ArrayList<GridForOwnedPolygon>();

		//crete grid inside a square which covers the matrix

		//square to cover the polygon
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MAX_VALUE;

		//scan all the edges to find these limits
		for(Edge edge : polygon.getEdges()){
			Vec vertex = edge.getVertex().getLocation();
			int x = (int) vertex.get(0);
			int y = (int) vertex.get(1);
			if(x < minX){
				minX = x;
			}
			else if(x > maxX){
				maxX = x;
			}

			if(y < minY){
				minY = y;
			}
			else if(y > maxY){
				maxY = y;
			}
		}

		//create Grids in the above square
		for(int i = minX ; i< maxX ; i += width){

			for(int j = minY; j < maxY; j += width){

				GridForOwnedPolygon grid = new GridForOwnedPolygon( new Vec(new Loc2d(i,j)), width);
				returnValue.add(grid);
			}
		}


		//remove unwanted Grids
		List<GridForOwnedPolygon> gridOutsideThePolygon = new ArrayList<GridForOwnedPolygon>();
		for(GridForOwnedPolygon grid : returnValue){
			if(!grid.doesBelongToPolygon(polygon)){
				gridOutsideThePolygon.add(grid);
			}
		}

		for(GridForOwnedPolygon grid : gridOutsideThePolygon){
			returnValue.remove(grid);
		}

		return returnValue;

	}
	private boolean pointInPolygon( int polySides,int polyX[],int polyY[] , int x,int y) 
	{

		int      i, j=polySides-1 ;
		boolean  oddNodes = false      ;

		for (i=0; i<polySides; i++) {
			if ((polyY[i]< y && polyY[j]>=y
					||   polyY[j]< y && polyY[i]>=y)
					&&  (polyX[i]<=x || polyX[j]<=x)) {
				if (polyX[i]+(y-polyY[i])/(polyY[j]-polyY[i])*(polyX[j]-polyX[i])<x) {
					oddNodes=!oddNodes; }}
			j=i; }

		return oddNodes; 
	}

}

