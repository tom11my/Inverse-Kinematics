

public class Vec3 {
	private float x, y, z;
	public Vec3(float x, float y, float z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	public Vec3 multiply(Mat33 transform) {
		float [][] nums = transform.getNums();
		return new Vec3(nums[0][0]*x + nums[0][1]*y + nums[0][2]*z, nums[1][0]*x + nums[1][1]*y + nums[1][2]*z, nums[2][0]*x + nums[2][1]*y + nums[2][2]*z);

	}
	public Vec2 trim () {
		return new Vec2(this.x, this.y);
	}
	public Vec3 getNeg() {
		return new Vec3(-this.x, -this.y, -this.z);
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
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public String toString() {
		return "x: " + this.x + " y: " + this.y + " z: " + this.z;
	}
}
