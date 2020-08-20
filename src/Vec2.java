
public class Vec2 {
	private float x;
	private float y;
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public Vec2() {
		this.x = 0;
		this.y = 0;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;		
	}
	public void setY(float y) {
		this.y = y;
	}

	public Vec2 plus (Vec2 added) {
		return new Vec2(this.x + added.getX(), this.y + added.getY());
	}
	public Vec2 minus (Vec2 subtracted) {
		return plus(subtracted.getNeg());
	}
	public Vec2 getNeg() {
		return new Vec2(-1*this.x, -1*this.y);
	}
	
	public Vec2 scaledBy(float f) {
		return new Vec2(this.x * f, this.y * f);
	}
	//consideration: dot product is associative so order should not matter
	public float dot(Vec2 v) {
		return this.x*v.getX() + this.y*v.getY();
	}
	public Vec2 toUnit() {
		float mag = this.findMag();
		return new Vec2(this.x/mag, this.y/mag);
	}
	public float findMag2() {
		return this.x*this.x + this.y*this.y;
	}
	public float findMag() {
		return (float)Math.sqrt(findMag2());
	}
	//to be implemented later
	public float findAngle() {
		return 0.0f;
		
	}
	/** approximately equal. */
	public boolean equals(Vec2 v) {
		return (int)this.x == (int)v.x && (int) this.y == (int)v.y;
	}
	//multiplying Vec2 by Mat22
	public Vec2 multiply (Mat22 mat) {
		return new Vec2(x*mat.getA() + y*mat.getB(), x*mat.getC() + y*mat.getD());
	}
	public String toString () {
		return "x: " +x + ", y: " + y;
	}
}
