/*
 * A trio of joints with rotating functionality. 
 */
public class BodyPartSystem {
	private MovingBodyPart start;
	private MovingBodyPart connect;
	private MovingBodyPart end;
	
	private int tokenCount;
	public BodyPartSystem() {
		
	}
	public BodyPartSystem(MovingBodyPart start, MovingBodyPart connect, MovingBodyPart end) {
		this.start = start;
		this.connect = connect;
		this.end = end;
		this.tokenCount = 0;
	}
	public BodyPartSystem(Node<MovingBodyPart> startNode, Node<MovingBodyPart> connectNode, Node<MovingBodyPart> endNode) {
		start = startNode.getData();
		connect = connectNode.getData();
		end = endNode.getData();
	}
	public Vec2 rotate (Vec2 orig, float theta) {
		float x = orig.getX();
		float y = orig.getY();
		return new Vec2((float)(x*Math.cos(theta) - y*Math.sin(theta)), (float) (x*Math.sin(theta) + y*Math.cos(theta)));
	}
	/*changes the location of MovingBodyPart end by increasing the angle between starter and ender by dtheta */
	public Mat33 applyAngleChange(float dtheta){
		Mat33 cur = end.getTransform();
		//end.setTransform(Mat33.findRotationMat(dtheta).multiply(end.getTransform()));
		float a = cur.getNums()[0][2];
		float b = cur.getNums()[1][2];
		
		float cos = (float)Math.cos(dtheta);
		float sin = (float)Math.sin(dtheta);

		float newA = a*cos - b*sin;
		float newB = a*sin + b*cos;

		float [] arr = {1.0f, 0.0f, newA, 0.0f, 1.0f, newB, 0.0f, 0.0f, 1.0f};
		end.setTransform(new Mat33(arr));
		return end.getTransform();
	}
	/* Finds cos(theta) with dot product */
	public float findSpecialCosAngle (Vec2 starter, Vec2 ender) {
		return starter.toUnit().dot(ender.toUnit());
	}
	public Vec2 angleToDir (float theta) {
		return new Vec2((float)Math.cos(theta), (float)Math.sin(theta));
	}
	public float dirToAngle (Vec2 dir) {
		return (float)(Math.atan(dir.getY()/dir.getX())*180/Math.PI);
	}
	class Line {
		private Vec2 initial;
		private Vec2 dir;
		public Line(Vec2 initial, Vec2 dir) {
			this.setInitial(initial);
			this.setDir(dir);
		}
		
		public Vec2 findDir (Vec2 initial, Vec2 end) {
			return end.minus(initial);
		}
		public Vec2 getInitial() {
			return initial;
		}
		public void setInitial(Vec2 initial) {
			this.initial = initial;
		}
		public Vec2 getDir() {
			return dir;
		}
		public void setDir(Vec2 slope) {
			this.dir = slope;
		}

	}
	public MovingBodyPart getStart() {
		return start;
	}

	public void setStart(MovingBodyPart start) {
		this.start = start;
	}

	public MovingBodyPart getConnect() {
		return connect;
	}

	public void setConnect(MovingBodyPart connect) {
		this.connect = connect;
	}

	public MovingBodyPart getEnd() {
		return end;
	}

	public void setEnd(MovingBodyPart end) {
		this.end = end;
	}
	public int getTokenCount() {
		return tokenCount;
	}
	public void setTokenCount(int changeTokens) {
		this.tokenCount = changeTokens;
	}
	
}
