import java.util.Scanner;

class Point{
	double x;
	double y;

	public Point() {}

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

public class Test {

	public static Scanner sc=new Scanner(System.in);
	public static double distance(Point p1,Point p2) {
		return Math.sqrt(Math.pow(Math.abs(p2.getX()-p1.getX()), 2)+Math.pow(Math.abs(p2.getY()-p1.getY()), 2));	
	}

	public static float area(double a, double b, double c) 
	{ 
		double s = (a+b+c)/2; 
		return (float)Math.sqrt(s*(s-a)*(s-b)*(s-c)); 
	}

	public static boolean isInside(Point a,Point b,Point c,Point p) 
	{    
		/* Calculate area of triangle ABC */
		double A = area (distance(a,b),distance(b,c),distance(a,c)); 

		/* Calculate area of triangle PBC */  
		double A1 = area (distance(a,b),distance(b,p),distance(a,p));  

		/* Calculate area of triangle PAC */  
		double A2 = area (distance(a,p),distance(p,c),distance(a,c)); 

		/* Calculate area of triangle PAB */   
		double A3 = area (distance(b,p),distance(p,c),distance(b,c)); 

		/* Check if sum of A1, A2 and A3 is same as A */
		return (A == A1 + A2 + A3); 
	} 

	public static boolean isInsidePoly(Point[] poly,Point p) //using Triangle fan test(http://erich.realtimerendering.com/ptinpoly/)
	{

		int count=0;
		for(int i=1;i<poly.length-1;i++)
		{
			if(isInside(poly[0],poly[i],poly[i+1],p))
				++count;
		}

		if(poly.length==4) {//if polygon is square

			if(distance(poly[0],poly[1])==distance(poly[1],poly[2])&&
					distance(poly[1],poly[2])==distance(poly[2],poly[3])&&
						count==2)
				return true;
			
		}

		if(count%2==0)
			return false;
		else
			return true;
	}
	public static Point acceptPoint()
	{
		Point p=new Point();
		System.out.println("Enter x axis for point");
		p.x=sc.nextFloat();
		System.out.println("Enter y axis for point");
		p.y=sc.nextFloat();
		return p;
	}
	public static Point[] acceptPolygon()
	{
		System.out.println("Enter numbers of points in your polygon");
		int ver=sc.nextInt();

		Point[] poly= new Point[ver];
		for(int i=0;i<ver;i++)
		{
			System.out.println("Enter coordinates for point "+i+"=>");
			poly[i]=Test.acceptPoint();
		}
		return poly;

	}
	public static int menuList()
	{
		System.out.println("0.Exit");
		System.out.println("1.Insert point to check inside polygon or not");
		System.out.println("2.Change polygon");
		System.out.println("3.Print Polygon");
		return sc.nextInt();

	}
	public static void main(String args[]) {

		int choice;
		Point p=new Point();
		Point[] polygon=Test.acceptPolygon();
		while( ( choice = Test.menuList( ) ) != 0 )
		{

			switch( choice )
			{
			case 1:
				p=Test.acceptPoint();
				break;
			case 2:
				polygon=Test.acceptPolygon();
				break;
			case 3:
				Test.printPolygon(polygon);
			}
			if(Test.isInsidePoly(polygon, p))
				System.out.println("\nGiven point("+p.getX()+","+p.getY()+") is inside the polygon\n");
			else
				System.out.println("\nGiven point("+p.getX()+","+p.getY()+") is outside the polygon\n");
		}
	}

	private static void printPolygon(Point[] polygon) {
		for(Point p: polygon)
			System.out.print("("+p.getX()+","+p.getY()+") => ");
	}

}