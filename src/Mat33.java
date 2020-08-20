

public class Mat33 {
	private float [][] nums;
	public Mat33() {
		//creates identity matrix by default
		nums = new float[3][3];
		nums[0][0] = 1;
		nums[1][1] = 1;
		nums[2][2] = 1;
	}
	
	public Mat33(float [][] mat) {
		if(mat.length == 3 && mat[0].length == 3)
			this.nums = mat;
		else
			System.out.println("INVALID DIMENSIONS");
	}
	
	public Mat33(float [] arr1D) {
		nums = new float[3][3];
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				nums[row][col] = arr1D[row*3 + col];
			}
		}
	}
	
	//matrix on left and vec on right 
	public Vec3 multipliedBy (Vec3 point) {
		
		float x = point.getX();
		float y = point.getY();
		float z = point.getZ();
		
		return new Vec3(nums[0][0]*x + nums[0][1]*y + nums[0][2]*z, nums[1][0]*x + nums[1][1]*y + nums[1][2]*z, nums[2][0]*x + nums[2][1]*y + nums[2][2]*z);
	}/*
	public Mat33 multiply(Vec3 v) {
		
	}*/
	//imagine this Mat33 on the left and mat on the right
	public Mat33 multiply (Mat33 matrix) {
		float [][] mat = matrix.getNums();
		float [][] newMat = new float [3][3];
		//row a, b, c and column 1, 2, 3
		float a1 = newMat[0][0] = nums[0][0]*mat[0][0] + nums[0][1]*mat[1][0] + nums[0][2]*mat[2][0];
		float a2 = newMat[0][1] = nums[0][0]*mat[0][1] + nums[0][1]*mat[1][1] + nums[0][2]*mat[2][1];
		float a3 = newMat[0][2] = nums[0][0]*mat[0][2] + nums[0][1]*mat[1][2] + nums[0][2]*mat[2][2];
		float b1 = newMat[1][0] = nums[1][0]*mat[0][0] + nums[1][1]*mat[1][0] + nums[1][2]*mat[2][0];
		float b2 = newMat[1][1] = nums[1][0]*mat[0][1] + nums[1][1]*mat[1][1] + nums[1][2]*mat[2][1];
		float b3 = newMat[1][2] = nums[1][0]*mat[0][2] + nums[1][1]*mat[1][2] + nums[1][2]*mat[2][2];
		float c1 = newMat[2][0] = nums[2][0]*mat[0][0] + nums[2][1]*mat[1][0] + nums[2][2]*mat[2][0];
		float c2 = newMat[2][1] = nums[2][0]*mat[0][1] + nums[2][1]*mat[1][1] + nums[2][2]*mat[2][1];
		float c3 = newMat[2][2] = nums[2][0]*mat[0][2] + nums[2][1]*mat[1][2] + nums[2][2]*mat[2][2];
		return new Mat33(newMat);
	}
	public float getValAtPosition(int row, int col) {
		return this.nums[row][col];
	}
	public void setValAtPosition (int row, int col, float value) {
		this.nums[row][col] = value;
	}
	public static Mat33 findRotationMat(float dtheta) {
		Mat22 mat2 = Mat22.findRotationMat(dtheta);
		float [] arr = {mat2.getA(), mat2.getB(), 0.0f, mat2.getC(), mat2.getD(), 0.0f, 0.0f, 0.0f, 1.0f};
		return new Mat33(arr);
	}
	//creates translation matrix given a 2D pofloat
	//careful with negative/positive
	public static Mat33 findTransMat(Vec3 point) {
		Mat33 m = new Mat33();
		m.setValAtPosition(0, 2, -point.getX());
		m.setValAtPosition(1,  2,  -point.getY());
		return m;
	}
	/** Returns the translation in vector form. */
	public Vec2 findTransVec() {
		return new Vec2(getValAtPosition(0, 2), getValAtPosition(1, 2));
	}
	//might need to add two negatives like I did above
	public static Mat33 findTranslationMat(Vec2 point) {
		Mat33 m = new Mat33();
		m.setValAtPosition(0, 2, point.getX());
		m.setValAtPosition(1, 2, point.getY());
		return m;
	}
	public static Mat33 findScaleMat(float scalar) {
		Mat33 m = new Mat33();
		m.setValAtPosition(0, 0, scalar);
		m.setValAtPosition(1, 1, scalar);
		return m;
	}
	public float[][] getNums() {
		return nums;
	}

	public void setNums(float[][] nums) {
		this.nums = nums;
	}

	public String toString() {
		return "\n" + nums[0][0] + " " + nums[0][1] + " " + nums[0][2] + "\n" + 
	nums[1][0] + " " + nums[1][1] + " " + nums[1][2] +"\n" + nums[2][0] + " " + nums[2][1] + " " + nums[2][2];
		
	}
}

