import java.util.Scanner;

public class Test {
	public static Scanner sc=new Scanner(System.in);


	public static Point acceptPoint()
	{
		Point p=new Point();
		System.out.println("Enter x axis");
		p.x=sc.nextDouble();
		System.out.println("Enter y axis");
		p.y=sc.nextDouble();
		return p;
	}
	public static Graph acceptGraph() throws CloneNotSupportedException
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

		System.out.println("Enter co-ordinates for Sun");
		Point sun=Test.acceptPoint();
		return new Graph(arr,sun);
	}

	public static int menuList()
	{
		System.out.println("0.Exit");
		System.out.println("1.Update sun co-ordinates");
		System.out.println("2.Change Graph");
		return sc.nextInt();
	}

	public static void main(String[] args) throws CloneNotSupportedException {

		Graph g=Test.acceptGraph();
		double surf =g.calSurface();
		System.out.println("\nTotal surface exposed to sunlight = "+surf+"\n");
		int choice;
		while((choice=Test.menuList())!=0)
		{
			switch(choice)
			{
			case 1:
				System.out.println("Enter new co-ordinates for Sun");
				Point sun=Test.acceptPoint();
				g.changeSun(sun);
				break;
			case 2:
				g=Test.acceptGraph();
				break;
			default:
				System.out.println("Enter valid choice");
				break;
			}
			surf=g.calSurface();
			System.out.println("\nTotal surface exposed to sunlight = "+surf+"\n");
		}


	}
}