class Point implements Cloneable
{
	double x;
	double y;

	public Point() {}
    
	@Override
	protected Point clone() throws CloneNotSupportedException {
		return new Point(x,y);
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}	
}
