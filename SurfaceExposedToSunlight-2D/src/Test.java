import java.util.Scanner;

public class Test {
	public static Scanner sc=new Scanner(System.in);

	public static Block[] acceptBlocks()
	{
		int n;
		System.out.println("Enter building count");
		n=sc.nextInt();
		Block[] arr=new Block[n];

		for(int i=0;i<n;i++)
		{
			Point[] points=new Point[4];
			System.out.println("Enter point one ");
			points[0]=Test.acceptPoint();
			System.out.println("Enter point two");
			points[1]=Test.acceptPoint();
			System.out.println("Enter point three");
			points[2]=Test.acceptPoint();
			System.out.println("Enter point four");
			points[3]=Test.acceptPoint();
			arr[i]=new Block(points);
		}
		return arr;
	}

	public static Point acceptPoint()
	{
		Point p=new Point();
		System.out.println("Enter x axis");
		p.x=sc.nextDouble();
		System.out.println("Enter y axis");
		p.y=sc.nextDouble();
		return p;
	}



	public static void main(String[] args) {

		Block[] blocks=Test.acceptBlocks();
		System.out.println("Enter co-ordinates for Sun");
		Point sun=Test.acceptPoint();
		Graph g=new Graph(blocks,sun);
		double surf =g.calSurface();
		System.out.println("\nTotal surface exposed to sunlight = "+surf+"\n");
	}
}