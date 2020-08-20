/*
 * Use trig functions to create realistic effect. Calculus may be useful. 
 */
public class BodyPartSystem {
	//most likely, these can be changed to joints though generality proves safer for now
	//let us define start and connect as the stationary joints
	private MovingBodyPart start;
	private MovingBodyPart connect;
	//let us define end as the mobile joint
	private MovingBodyPart end;
	
	private int tokenCount;
	public BodyPartSystem() {
		
	}
	public BodyPartSystem(MovingBodyPart start, MovingBodyPart connect, MovingBodyPart end) {
		this.start = start;
		this.connect = connect;
		this.end = end;
		this.tokenCount = 0;
	}/*
	public void translate (Vec2 trans) {
		start.setLoc(start.getLoc().plus(trans));
		connect.setLoc(connect.getLoc().plus(trans));
		end.setLoc(end.getLoc().plus(trans));
	}
	public BodyPartSystem(Node<Joint> startNode, Node<Joint> connectNode, Node<Joint> endNode) {
		start = startNode.getData();
		connect = connectNode.getData();
		end = endNode.getData();
	}*/
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
	//changes the location of MovingBodyPart end by increasing the angle between starter and ender by dtheta
	//returns the changed Vec2 to be applied on elements deeper in the tree
	public Mat33 applyAngleChange(float dtheta){
		/*
		 * 1. Create two vectors from three joint locations
		 * 2. Translate a copy of ender to the origin (subtracting connect)
		 * 3. Rotate with trig
		 * 4. Translate back to original location (adding connect)
		 * 5. Return tokenCount that limits the action duration
		 */
		//Rotation matrix: a = cos, b = -sin, c = sin, d = cos
		//Determine how the end is translated so that we can apply this translation to other nodes beneath it in the tree
		//I might screw up order of matrix multiplication
		//end.setTransform(Mat22.findRotationMat(dtheta).multiply(end.getTransform()));
		//Thread.sleep(1);
		//System.out.println("before " + end.getTransform());
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
		//end.setTransform(end.getTransform().multiply(Mat33.findRotationMat(dtheta)));
		//end.setLoc(connect.getLoc().plus(rotatedEnder));
		//System.out.println("after " + end.getTransform());
		//tokenCount++;
	}
	//We know u dot v = |u||v|cos(theta)
	//This method allows us to find cos(theta), hence 'SpecialAngle'
	//returns a value between -1 and 1
	public float findSpecialCosAngle (Vec2 starter, Vec2 ender) {
		//consideration: either starter or ender is stationary in simple motion,
		//but when multiple systems are moving and connected via the same end joint,
		//angles must be added or subtracted to each other
		return starter.toUnit().dot(ender.toUnit());
	}
	//assumes starting point is at the origin
	//returns unit vector, ready to be scaled
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
