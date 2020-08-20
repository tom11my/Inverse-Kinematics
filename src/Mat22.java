
public class Mat22 {
	private float a, b, c, d;
	// a b
	// c d
	public Mat22() {
		this.a = this.d = 1;
		this.c = this.b = 0;
	}
	public Mat22(float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	//composition of two matrices
	public Mat22 multiply (Mat22 mat) {
		float e = mat.getA();
		float f = mat.getB();
		float g = mat.getC();
		float h = mat.getD();
		
		return new Mat22(e*a + g*b, f*a + h*b, e*c + g*d, f*c + h*d);
	}
	//sines originally had opposite signs (+/-)
	public static Mat22 findRotationMat(float theta) {
		return new Mat22((float)Math.cos(theta), (float)Math.sin(theta), (float) -Math.sin(theta), (float)Math.cos(theta));
	}
	public float getA() {
		return a;
	}
	public void setA(float a) {
		this.a = a;
	}
	public float getB() {
		return b;
	}
	public void setB(float b) {
		this.b = b;
	}
	public float getC() {
		return c;
	}
	public void setC(float c) {
		this.c = c;
	}
	public float getD() {
		return d;
	}
	public void setD(float d) {
		this.d = d;
	}
	
}
