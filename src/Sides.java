
//creates a new object, called Sides, that will represent each rectangle
//surrounding the squares

public class Sides {
	
	protected int x1; //top left corner
	protected int y1;
	protected int x2; //bottom right corner
	protected int y2;
	
	public Sides(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

	}
	
	public int getx1(){
		return x1;
	}
	
	public int gety1(){
		return y1;
	}
	
	public int getx2(){
		return x2;
	}
	
	public int gety2(){
		return y2;
	}
	
	
}
