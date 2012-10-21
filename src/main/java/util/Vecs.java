package util;

import util.Locations.Loc;

/*
 * The following operations are available for +, -, and * operations in vector math
 * In-place operations: add,   subtract,  scale,  normalize
 * Not in-place:        plus,  minus,     times,  dir
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
		
		public Vec scale(float s) {
			for( int i = 0; i < values.length; i++ )
				values[i] *= s;
			return this;
		}
		public Vec times(float s) {
			Vec vec = new Vec(values.length);
			for( int i = 0; i < values.length; i++ )
				vec.values[i] = values[i] * s;
			return vec;
		}
		
		// the dot product
		public float dot(Loc loc) {
			float sum = 0;
			for( int i = 0; i < values.length; i++ ) {
				sum += values[i] * loc.values[i];
			}
			return sum;
		}
		
		// the length of the vector
		public float norm() {
			return (float) Math.sqrt(dot(this));
		}
		
		public Vec normalize() {
			scale( 1.0F / norm() );
			return this;
		}
		
		public Vec dir() {
			return times( 1.0F / norm() );
		}
		
	}

}
