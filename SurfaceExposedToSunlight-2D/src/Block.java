
public class Block {
	
	double minX,maxY,minY,maxX,dispX,dispY;

	Block(){
		dispX=0;
		dispY=0;
	}
	
	Block(Point[] arr){

		for(int i=0;i<4;i++)
		{
			if(arr[i].x<0)
				dispX=Math.abs(arr[i].x);
			if(arr[i].y<0)
				dispY=Math.abs(arr[i].y);
		}
		this.calculate(arr);
	}
	
	public  void calculate(Point[] arr)
	{
		minX=Math.min(Math.min(arr[0].x, arr[1].x),Math.min(arr[2].x, arr[3].x));
		minY=Math.min(Math.min(arr[0].y, arr[1].y),Math.min(arr[2].y, arr[3].y));
		maxX=Math.max(Math.max(arr[0].x, arr[1].x),Math.max(arr[2].x, arr[3].x));
		maxY=Math.max(Math.max(arr[0].y, arr[1].y),Math.max(arr[2].y, arr[3].y));
	}
	
	public Point[] getPoints() {
		Point[] arr=new Point[4];

		arr[0]=new Point(minX,minY);
		arr[1]=new Point(minX,maxY);
		arr[2]=new Point(maxX,maxY);
		arr[3]=new Point(maxX,minY);
		
		return arr;
	}
	
	void printBlock() {
		System.out.print("a("+minX+","+minY+") "+
				"b("+maxX+","+minY+") "+
				"c("+maxX+","+maxY+") "+
				"d("+minX+","+maxY+")\n"+
				"dispX="+dispX+"dispY="+dispY+"\n");
	}

}
