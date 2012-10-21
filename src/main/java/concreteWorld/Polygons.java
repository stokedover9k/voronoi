package concreteWorld;

import java.util.Iterator;
import java.util.List;

import mesh.Vertex;
import util.Vecs.Vec;
import concreteWorld.VoronoiGameField.OwnedPolygon;

public class Polygons {
	
	public static interface PolygonEvaluator {
		double evaluate( OwnedPolygon polygon );  
	}
	
	public static interface TriangleEvaluator {
		double evalueate( Vec origin, Vec a, Vec b );
	}
	
	public static interface PolygonPerTriangleEvaluator extends PolygonEvaluator {
	}
	
	public static class TriangularizerEvaluator implements PolygonPerTriangleEvaluator {

		final TriangleEvaluator triangleEvaluator;
		
		public TriangularizerEvaluator( TriangleEvaluator triangleEvaluator ) {
			this.triangleEvaluator = triangleEvaluator;
		}
		
		@Override
		public double evaluate(OwnedPolygon polygon) {
			double value = 0;
			
			List<Vertex> vertices = polygon.getVertices();
			vertices.add( vertices.get(0) );  // add the first onto the end
			
			Iterator<Vertex> itr = vertices.iterator();
			Vertex last = itr.next();
			while( itr.hasNext() ) {
				Vertex current = itr.next();
				value += triangleEvaluator.evalueate( polygon.getStone(), last.getLocation(), current.getLocation() );
				last = current;
			}
			
			return value;
		}
	}
	
	public static class WeightedGaussianTriangleEvaluator implements TriangleEvaluator {

		@Override
		public double evalueate(Vec origin, Vec vecA, Vec vecB) {
			double a = origin.minus(vecA).norm();
			double b = origin.minus(vecB).norm();
			double c = vecA.minus(vecB).norm();
			
			return 0.25 * Math.sqrt((a+b-c)*(a-b+c)*(-a+b+c)*(a+b+c));
		}
		
	}
}
