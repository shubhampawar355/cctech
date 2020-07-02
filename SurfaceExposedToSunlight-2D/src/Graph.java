
public class Graph {

	 private Block[] blocks;
	 private Point sun;
	 private Double dispX,dispY;// displacement X & Y for whole graph


	Graph(){
		this.sun=new Point();
		dispX=0.0;
		dispY=0.0;
	}

	Graph(Block[] arr,Point sun) throws CloneNotSupportedException{
		this();
		this.sun=sun.clone();
		if(sun.x<dispX)
			dispX=Math.abs(sun.x);
		if(sun.y<dispY)
			dispY=Math.abs(sun.y);

		for(int i=0;i<arr.length;i++)
		{
			if(dispX<arr[i].dispX)
				dispX=arr[i].dispX;
			if(dispY<arr[i].dispY)
				dispY=arr[i].dispY;
		}
		
		this.blocks=new Block[arr.length];
		for(int i=0;i<arr.length;i++)
		{
			this.blocks[i]=arr[i].clone();
		}
		this.adjustGraph();
		this.sortBlocks();
	}
	
	void changeSun(Point p) throws CloneNotSupportedException
	{
		p.x=p.x+this.dispX;
		p.y=p.y+this.dispY;
		this.sun=p.clone();
		if(p.x<0)
			this.dispX=Math.abs(p.x);
		if(p.y<0)
			this.dispY=Math.abs(p.y);
		if(p.x<0 || p.y<0)
			adjustGraph();	
	}

	private void adjustGraph()	{
		if(dispX!=0 || dispY!=0)
		{
			sun.x=sun.x+this.dispX;
			sun.y=sun.y+this.dispY;
			for(int i=0;i<blocks.length;i++)
			{
				Point[] arr=blocks[i].getPoints();
				for(int j=0;j<4;j++)
				{
					arr[j].x=arr[j].x+this.dispX;
					arr[j].y=arr[j].y+this.dispY;
				}
				blocks[i].calculate(arr);
			}
		}
	}

	private void sortBlocks()	{
		for(int i=0;i<blocks.length;i++)
		{
			for(int j=0;j<blocks.length;j++)
			{
				if(blocks[j].minX>blocks[i].minX && i!=j)
				{
					Block temp=new Block();
					temp=blocks[i];
					blocks[i]=blocks[j];
					blocks[j]=temp;
				}
			}
		}
	}

	public void printGraph() {
		System.out.println("Blocks :-");
		for(int i=0;i<blocks.length;i++)
		{	System.out.println("block "+i+"=>");
		blocks[i].printBlock();
		}
		System.out.println("Sun("+sun.x+","+sun.y+")\ndispX="+dispX+" dispY="+dispY);
	}

	private Point lineSegmentIntersection(Point sun, Point gp, Point start, Point end) 	{ 
		// Line AB represented as a1x + b1y = c1 
		double a1 = gp.y - sun.y; 
		double b1 = sun.x - gp.x; 
		double c1 = a1*(sun.x) + b1*(sun.y); 
		// Line CD represented as a2x + b2y = c2 
		double a2 = end.y - start.y; 
		double b2 = start.x - end.x; 
		double c2 = a2*(start.x)+ b2*(start.y); 

		double determinant = a1*b2 - a2*b1; 

		if (determinant == 0)   
			return null;// The lines are parallel.
		else
		{ 
			Point ip=new Point();
			ip.x = (b2*c1 - b1*c2)/determinant; 
			ip.y = (a1*c2 - a2*c1)/determinant; 
			if(ip.x<=Math.max(start.x, end.x) && ip.x>=Math.min(start.x, end.x) && ip.y<=Math.max(start.y, end.y) ) 
				return ip;
			else
				return null;
		} 
	}

	private double getDistance(Point p1,Point p2) {
		return Math.sqrt(Math.pow(Math.abs(p2.getX()-p1.getX()), 2)+Math.pow(Math.abs(p2.getY()-p1.getY()), 2));
	}

	private Point getGP(int i,Block b)
	{
		Point p=new Point();
		if(i==1 || i==4)
			p= new Point(b.minX,b.maxY);
		else if(i==2 || i==5)
			p= new Point(b.maxX,b.maxY);
		return p;
	}

	private int[] calPosition()
	{
		int[] pos=new int[blocks.length];

		for(int i=0;i<blocks.length;i++)
		{
			if(sun.x<blocks[i].minX)
			{
				if(sun.y<=blocks[i].maxY)
					pos[i]=1;
				else
					pos[i]=2;
			}
			else if(sun.x>blocks[i].maxX)
			{
				if(sun.y<=blocks[i].maxY)
					pos[i]=5;
				else
					pos[i]=4;
			}
			else 
				pos[i]=3;
		}
		return pos;
	}

	private double getMin(double[] value) {
		double res=Double. MAX_VALUE;
		for(int i=0;i<value.length;i++)
			if(value[i]<res && value[i]>0)
				res=value[i];
		return res;
	}
	
	public double calSurface()
	{
		double surf=0.0;
		double presurf=0.0;
		int[] pos=calPosition(); //step 1
		
		for(int i=0;i<blocks.length;i++)
		{
			Point lTop=new Point(blocks[i].minX,blocks[i].maxY);
			Point lBase=new Point(blocks[i].minX,blocks[i].minY);
			Point rTop=new Point(blocks[i].maxX,blocks[i].maxY);
			Point rBase=new Point(blocks[i].maxX,blocks[i].minY);
			presurf=surf;	
			if(pos[i]==1)
			{
				if(i==0)
					surf=blocks[i].maxY;
				else
				{
					if(sun.x>blocks[i-1].maxX)
						surf=surf+blocks[i].maxY;
					else
					{
						double[] value=new double[i];

						for(int j=0;j<i;j++)
						{
							Point cp=getGP(pos[j], blocks[j]);
							if(pos[j]==3)
								cp=new Point(blocks[j].maxX,blocks[j].maxY);
							Point ip=lineSegmentIntersection(sun,cp,lTop,lBase);
							if(ip!=null) 
								value[j]=getDistance(ip, lTop);

						}
						if(getMin(value)!=Double.MAX_VALUE)
							surf=surf+getMin(value);
					}
					
				}
			}
			else if(pos[i]==2)
			{
				if(i==0)
					surf=blocks[i].maxY+blocks[i].maxX-blocks[i].minX;
				else
				{
					if(sun.x>blocks[i-1].maxX)
						surf=surf+blocks[i].maxY+blocks[i].maxX-blocks[i].minX;
					else
					{
						double[] value=new double[i];
						double bound=Double.MIN_VALUE;
						int boundIndex=0;
						for(int j=0;j<i;j++)
						{
							Point cp=getGP(pos[j], blocks[j]);
							if(pos[j]==3)
								cp=new Point(blocks[j].maxX,blocks[j].maxY);
							if(sun.x==blocks[i-1].maxX)
								cp=new Point(blocks[i].minX,blocks[i].minY);
							if(bound<blocks[j].maxY) {
								bound=blocks[j].maxY;
								boundIndex=j;
							}
							Point ip=lineSegmentIntersection(sun,cp,lTop,lBase);
							if(ip!=null) { 
								if(ip.y<0)
									value[j]=blocks[i].maxY+blocks[i].maxX-blocks[i].minX;
								else
									value[j]=getDistance(ip, lTop)+blocks[i].maxX-blocks[i].minX;
							}
							else
							{
								ip=lineSegmentIntersection(sun,cp,lTop,rTop);
								if(ip!=null) {
									value[j]=getDistance(ip, rTop);
								}
							}							
						}
							surf=surf+value[boundIndex];
					}
				}

			}
			else if(pos[i]==3)
				surf=surf+getDistance(lTop, rTop);
			
			else if(pos[i]==4)
			{
				if(i==blocks.length-1)
					surf=surf+blocks[i].maxY+blocks[i].maxX-blocks[i].minX;
				else
				{
					if(sun.x<blocks[i+1].minX)
						surf=surf+blocks[i].maxY+blocks[i].maxX-blocks[i].minX;
					else
					{
						double[] value=new double[blocks.length];
						double bound=Double.MIN_VALUE;
						int boundIndex=0;
						
						for(int j=blocks.length-1;j>i;j--)
						{
							Point cp=getGP(pos[j], blocks[j]);
							if(pos[j]==3)
								cp=new Point(blocks[j].minX,blocks[j].maxY);
							if(sun.x==blocks[i+1].minX)
								cp=new Point(blocks[i].maxX,blocks[i].minY);

							if(bound<blocks[j].maxY) {
								bound=blocks[j].maxY;
								boundIndex=j;
							}

							Point ip=lineSegmentIntersection(sun,cp,rTop,rBase);
							if(ip!=null) {
								if(ip.y<0)
									value[j]=blocks[i].maxY+blocks[i].maxX-blocks[i].minX;
								else
									value[j]=getDistance(ip, rTop)+blocks[i].maxX-blocks[i].minX;
							}
							else
							{
								ip=lineSegmentIntersection(sun,cp,lTop,rTop);
								if(ip!=null)
									value[j]=getDistance(ip, lTop);
							}
							System.out.println("j="+j+" value[j]= "+value[j]);
						}
							surf=surf+value[boundIndex];
						
					}
				}

			}
			else
			{
				if(i==blocks.length-1)
					surf=surf+blocks[i].maxY;
				else
				{
					if(sun.x<blocks[i+1].minX)
						surf=surf+blocks[i].maxY;
					else
					{
						double[] value=new double[blocks.length];

						for(int j=blocks.length-1;j>i;j--)
						{
							Point cp=getGP(pos[j], blocks[j]);
							Point ip=lineSegmentIntersection(sun,cp,rTop,rBase);
							if(ip!=null) 
								value[j]=getDistance(ip, rTop);
						}
						if(getMin(value)!=Double.MAX_VALUE)
							surf=surf+getMin(value);
	
					}
				}
			}
			System.out.println("Surface for  block "+i+"="+(surf-presurf));
		}
		return surf;
	}

	
}

