package pagerank;

import java.util.ArrayList;

public class OutDegree {
	public ArrayList<Integer> outobject = new ArrayList<Integer>();
	public double degree;
	
	public OutDegree(int value)
	{
		this.outobject.add(value);
		this.degree = 0;
	}
	
	public void adddegree(int value)
	{
		this.outobject.add(value);
	}
	
	public void ComputDegree()
	{
		this.degree = ((double)1/this.outobject.size());
	}
}
