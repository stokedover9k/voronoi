package mesh;

public class Faces {

	public interface Factory<F extends Face> {
		public F createFace(Edge edge);
		public F cloneFace(Face face);
	}

	
	
	public static class FaceFactory implements Faces.Factory<Face> {

		@Override
		public Face createFace(Edge edge) {
			return new Face(edge);
		}

		@Override
		public Face cloneFace(Face face) {
			return new Face( face.getEdge() );
		}
	}
}