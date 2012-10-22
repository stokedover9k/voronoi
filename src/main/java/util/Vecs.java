package util;

import util.Locations.Loc;
import util.Locations.Loc2d;

/*
 * The following operations are available for +, -, and * operations in vector math
 * In-place operations:  add,   subtract,  scale,  normalize
 * Not in-place:         plus,  minus,     times,  dir
 */

public class Vecs {
	
	public static class Vec extends Locations.Loc {

		public Vec(Loc loc) {
			super( loc );
		}
		
		private Vec( int degree ) {
			super( degree );
		}
		
		
		
		public Vec add(Loc loc) {
			for( int i = 0; i < values.length; i++ )
				values[i] += loc.values[i];
			return this;
		}
		public Vec plus(Loc loc) {
			Vec vec = new Vec(values.length);
			for( int i = 0; i < values.length; i++ )
				vec.values[i] = values[i] + loc.values[i];
			return vec;
		}
		
		public Vec subtract(Loc loc) {
			for( int i = 0; i < values.length; i++ )
				values[i] -= loc.values[i];
			return this;
		}
		public Vec minus(Loc loc) {
			Vec vec = new Vec(values.length);
			for( int i = 0; i < values.length; i++ )
				vec.values[i] = values[i] - loc.values[i];
			return vec;
		}
		
		public Vec scale(double x) {
			for( int i = 0; i < values.length; i++ )
				values[i] *= x;
			return this;
		}
		public Vec times(double x) {
			Vec vec = new Vec(values.length);
			for( int i = 0; i < values.length; i++ )
				vec.values[i] = values[i] * x;
			return vec;
		}
		
		// the dot product
		public double dot(Loc loc) {
			double sum = 0;
			for( int i = 0; i < values.length; i++ ) {
				sum += values[i] * loc.values[i];
			}
			return sum;
		}
		
		// the length of the vector
		public double norm() {
			return Math.sqrt(dot(this));
		}
		
		// makes the vector unit length
		public Vec normalize() {
			scale( 1.0F / norm() );
			return this;
		}
		
		// returns a unit vector in the same direction
		public Vec dir() {
			return times( 1.0F / norm() );
		}
		
		public Vec midpointTo(Loc location) {
			Vec midpoint = new Vec(values.length);
			for( int i = 0; i < values.length; i++ )
				midpoint.values[i] = (this.values[i] + location.values[i]) / 2;
			return midpoint;
		}
		
	}

	
	public static Vec perpendicular(Vec vec) {
		Vec temp = new Vec(vec);
		temp.values[0] += 1;
		if( vec.dir().dot(temp.normalize()) < 1 ) ;
		else temp.values[1] += 1;
		
		Vec a = vec.dir().scale(vec.dir().dot(temp.normalize()));
		return temp.subtract(a).normalize();
	}
	
	
	
	// L is the line to intersect
	// L's location is expected to be relative to the origin of the intersecting vector (origin)
	// Returns NULL if parallel
	public static Vec intersection( Vec vec, Ray L ) {
//		System.err.println(vec);
//		System.err.println( L.getPoint() );
//		System.err.println( L.getDir() );
		
		Vec V = vec.dir();
		Vec D = L.getPoint();
		double cos1 = V.dot(D.dir());
		
		Vec T = V.times(D.norm() * cos1);
		Vec Y = T.minus(L.getPoint());
		double cos2 = L.getDir().dot(Y.dir());
		if( cos2 == 0 )
			return null;
		
		double LLenToIntersection = Y.norm() / cos2;
		
		return L.getDir().times(LLenToIntersection).add(L.getPoint());
	}
	
	public static class Ray {
		Vec from;
		Vec dir;
		
		public Ray( Vec from, Vec dir ) {
			this.from = from;
			this.dir = dir.normalize();
		}
		
		public Vec getPoint() {
			return from;
		}
		
		public Vec getDir() {
			return dir;
		}
	}
	
	public static void main(String[] args) {
		
		Vec a1 = new Vec( new Loc2d(1,1) );
		Vec a2 = new Vec( new Loc2d(-1, 1) );
		Vec b1 = new Vec( new Loc2d(2, 2) );
		Vec b2 = new Vec( new Loc2d(-3, 1) );
		
		Vec t = intersection(a2.dir(), new Ray(b1.minus(a1), b2));
		System.out.println(t);
	}
}
