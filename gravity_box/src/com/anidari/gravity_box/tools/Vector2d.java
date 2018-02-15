package com.anidari.gravity_box.tools;

/**
 * everything needed for vector maths in 2d
 * @author ajrog_000
 *
 */
public class Vector2d {
	
	public float x;
	public float y;
	
	public Vector2d(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(Vector2d vector2d) {
		this.x = vector2d.x;
		this.y = vector2d.y;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * takes two coordinates and makes a vector from them
	 * @param point1 must have 2 elements
	 * @param point2 must have 2 elements
	 * @return
	 */
	static public Vector2d fromPoints(float[] point1, float[] point2) {
		return new Vector2d(point2[0] - point1[0], point2[1] - point1[1]);
	}
	
	/**
	 * takes two coordinates and makes a vector from them
	 * @return
	 */
	static public Vector2d fromPoints(float point1X, float point1Y, float point2X, float point2Y) {
		return new Vector2d(point2X - point1X, point2Y - point1Y);
	}
	
	/**
	 * useful for making a velocity vector from the last two frames of coordinates
	 * @param point1X
	 * @param point1Y
	 * @param point2X
	 * @param point2Y
	 * @param delta
	 * @return
	 */
	static public Vector2d fromPoints(float point1X, float point1Y, float point2X, float point2Y, float delta) {
		return new Vector2d((point2X - point1X) / delta, (point2Y - point1Y) / delta);
	}
	
	public void updateFromPoints(float point1X, float point1Y, float point2X, float point2Y, float delta) {
		x = (point2X - point1X) / delta;
		y = (point2Y - point1Y) / delta;
	}
	
	public void averageWithPoints(float pointX, float pointY) {
		x = (x + pointX) / 2f;
		y = (y + pointY) / 2f;
	}
	
	public void averageFromPoints(float point1X, float point1Y, float point2X, float point2Y, float delta) {
		x = (x + (point2X - point1X) / delta) / 2f;
		y = (y + (point2Y - point1Y) / delta) / 2f;
	}
	
	public float getMagnitude() {
		return (float) Math.sqrt((this.x * this.x) + (this.y * this.y));
	}
	
	public float getMagnitudeSquared() {
		return (this.x * this.x) + (this.y * this.y);
	}
	
	public void normalise() {
		float magnitude = getMagnitude();
		this.x /= magnitude;
		this.y /= magnitude;
	}
	
	public void add(Vector2d addedVector) {
		this.x += addedVector.getX();
		this.y += addedVector.getY();		
	}
	
	public void subtract(Vector2d subtractVector) {
		this.x -= subtractVector.getX();
		this.y -= subtractVector.getY();
	}
	
	/**
	 * divides by a scalar
	 */
	public void divideBy(float divideBy) {
		this.x /= divideBy;
		this.y /= divideBy;
	}
	
	/**
	 * times by a scalar
	 */
	public void timesBy(float timesBy) {
		this.x *= timesBy;
		this.y *= timesBy;
	}
	
	/**
	 * useful for updating speed
	 * @param addVector
	 * @param scalar
	 */
	public void addVectorByScalar(Vector2d addVector, float scalar) {
		this.x += (addVector.getX() * scalar);
		this.y += (addVector.getY() * scalar);
	}
	
	public void addVector(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	public void addX(float x) {
		this.x += x;
	}
	
	public void addY(float y) {
		this.y += y;
	}
	
	public void minusX(float x) {
		this.x -= x;
	}
	
	public void minusY(float y) {
		this.y -= y;
	}
	
	public void reset() {
		this.x = 0.0f;
		this.y = 0.0f;
	}
	
	public void invertX() {
		this.x = this.x * -1;
	}
	
	public void invertY() {
		this.y = this.y * -1;
	}
	
	public void makeXPositive() {
		this.x = Math.abs(x);
	}
	
	public void makeYPositive() {
		this.y = Math.abs(y);
	}
	
	public void makeXNegative() {
		this.x = Math.abs(x) * -1f;
	}
	
	public void makeYNegative() {
		this.y = Math.abs(y) * -1f;
	}
	
	public void scaleX(float scale) {
		this.x *= scale;
	}
	
	public void scaleY(float scale) {
		this.y *= scale;
	}
	
	/**
	 * uses the vector convention of + being counter-clockwise
	 * @param degrees
	 */
	public void setFromAngle(float angle, float magnitude) {
		// convert to vector using given magnitude
		this.x = (float) (magnitude * Math.cos(angle));
		this.y = (float) (magnitude * Math.sin(angle));
	}
}
