import java.util.TreeSet;

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
				return null;		} 
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
			if(sun.x<blocks[i].minX)//left
			{
				if(sun.y<=blocks[i].maxY)//left-bottom 
					pos[i]=1;
				else					//left-up
					pos[i]=2;
			}
			else if(sun.x>blocks[i].maxX)//right
			{
				if(sun.y<=blocks[i].maxY)//right-bottom
					pos[i]=5;
				else					//right-up
					pos[i]=4;
			}
			else 						// upside
				pos[i]=3;
		}
		return pos;
	}


	public double calSurface() throws CloneNotSupportedException
	{
		double surf=0.0;
		double presurf=0.0;
		int[] pos=calPosition(); //step 1
		for(int i=0;i<blocks.length;i++)//step 2
		{
			Point lTop=new Point(blocks[i].minX,blocks[i].maxY);
			Point lBase=new Point(blocks[i].minX,blocks[i].minY);
			Point rTop=new Point(blocks[i].maxX,blocks[i].maxY);
			Point rBase=new Point(blocks[i].maxX,blocks[i].minY);
			TreeSet<Double> set=new TreeSet<Double>();

			presurf=surf;	
			if(pos[i]==1)
			{
				System.out.println("in case 1");
				if(i==0)
					set.add(blocks[i].maxY);
				else
				{
					if(sun.x>blocks[i-1].maxX)
						set.add(blocks[i].maxY);
					else
					{
						double bound=0;
						for(int j=0;j<i;j++)
						{	
							if(sun.x>blocks[j].maxX)
								continue;
							if(bound<blocks[j].maxY)
								bound=blocks[j].maxY;
							System.out.println("bound="+bound);
							Point cp=getGP(pos[j], blocks[j]);
							if(pos[j]==3)
							{
								if(blocks[j].maxX==sun.x)
									cp=lBase.clone();
								else
									cp=new Point(blocks[j].maxX,blocks[j].maxY);
							}
							Point ip=lineSegmentIntersection(sun,cp,lTop,lBase);
							if(bound>=blocks[j].maxY)
							{
								if(ip!=null)
								{
									if(ip.y<0)
										set.add(getDistance(lBase, lTop));
									else
										set.add(getDistance(ip, lTop));
								}									
							}
							if(ip==null && bound==blocks[j].maxY)
								set.add(0.0);
						}
					}
				}
			}
			else if(pos[i]==2)
			{
				System.out.println("in case 2");
				if(i==0)
					set.add(blocks[i].maxY+blocks[i].maxX-blocks[i].minX);
				else
				{
					if(sun.x>blocks[i-1].maxX)
						set.add(blocks[i].maxY+blocks[i].maxX-blocks[i].minX);
					else
					{
						double bound=0;
						for(int j=0;j<i;j++)
						{
							if(sun.x>blocks[j].maxX)
								continue;
							if(bound<blocks[j].maxY)
								bound=blocks[j].maxY;
							Point cp=getGP(pos[j], blocks[j]);
							if(pos[j]==3) {
								if(blocks[j].maxX==sun.x)
									cp=lBase.clone();
								else
									cp=new Point(blocks[j].maxX,blocks[j].maxY);
							}
							Point ip=lineSegmentIntersection(sun,cp,lTop,lBase);
							if(ip!=null) { //special condition block for 2
								System.out.println("ip("+ip.x+","+ip.y+")");
								if(ip.x==lTop.x && ip.y==lTop.y)
									set.add(getDistance(lTop, rTop));
								else if(ip.y<0)
									set.add(blocks[i].maxY+blocks[i].maxX-blocks[i].minX);
								else if(ip.y<blocks[i].maxY)
									set.add(getDistance(ip,lTop)+blocks[i].maxX-blocks[i].minX);
							}
							else
							{
								ip=lineSegmentIntersection(sun,cp,lTop,rTop);
								if(ip!=null)
									set.add(getDistance(ip, rTop));
							}
							if(ip==null && bound==blocks[j].maxY)
								set.add(0.0);
						}
					}
				}
			}
			else if(pos[i]==3) {
				System.out.println("in case 3");
				set.add(getDistance(lTop, rTop));
			}
			else if(pos[i]==4)
			{
				System.out.println("in case 4");
				if(i==blocks.length-1)
					set.add(blocks[i].maxY+blocks[i].maxX-blocks[i].minX);
				else
				{
					if(sun.x<blocks[i+1].minX)
						set.add(blocks[i].maxY+blocks[i].maxX-blocks[i].minX);
					else
					{			
						double bound=0;
						for(int j=blocks.length-1;j>i;j--)
						{
							if(sun.x<blocks[j].minX)
								continue;
							if(bound<blocks[j].maxY)
								bound=blocks[j].maxY;
							Point cp=getGP(pos[j], blocks[j]);
							if(pos[j]==3) {
								if(blocks[j].minX==sun.x)
									cp=rBase.clone();
								else
									cp=new Point(blocks[j].minX,blocks[j].maxY);
							}
							Point ip=lineSegmentIntersection(sun,cp,rTop,rBase);
							if(ip!=null) {
								if(ip.x==rTop.x && ip.y==rTop.y)
									set.add(getDistance(lTop, rTop));
								else if(ip.y<0)
									set.add(blocks[i].maxY+blocks[i].maxX-blocks[i].minX);
								else
									set.add(getDistance(ip, rTop)+blocks[i].maxX-blocks[i].minX);
							}
							else
							{
								ip=lineSegmentIntersection(sun,cp,lTop,rTop);
								if(ip!=null)
									set.add(getDistance(ip, lTop));
							}
							if(ip==null && bound==blocks[j].maxY)
								set.add(0.0);
						}
					}
				}
			}
			else
			{
				System.out.println("in case 5");
				if(i==blocks.length-1)
					set.add(blocks[i].maxY);
				else
				{
					if(sun.x<blocks[i+1].minX)
						set.add(blocks[i].maxY);
					else
					{
						double bound=0;
						for(int j=blocks.length-1;j>i;j--)
						{
							if(sun.x<blocks[j].minX)
								continue;
							if(bound<blocks[j].maxY)
								bound=blocks[j].maxY;
							Point cp=getGP(pos[j], blocks[j]);
							if(pos[j]==3)
							{
								if(blocks[j].minX==sun.x)
									cp=rBase.clone();
								else
									cp=new Point(blocks[j].minX,blocks[j].maxY);
							}
							Point ip=lineSegmentIntersection(sun,cp,rTop,rBase);
						
							if(bound>=blocks[j].maxY) {
								if(ip!=null)
								{
									System.out.println("j="+j+" ip("+ip.x+","+ip.y+")");
									if(ip.y<0)
										set.add(getDistance(rBase, rTop));
									else
										set.add(getDistance(ip, rTop));
								}
							}
							if(ip==null && bound==blocks[j].maxY)
								set.add(0.0);
						}
					}
				}
			}
			if(!set.isEmpty())
				surf=surf+set.first();
			System.out.println("Surface for  block "+i+"="+(surf-presurf));
		}
		return surf;
	}
}
