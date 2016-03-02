package pagerank;

import java.util.ArrayList;
import java.util.HashMap;

public class UnderPageRank {
	public static HashMap<Integer, OutDegree> outdegrees = new HashMap<Integer, OutDegree>();
	public static int c;
	
	public static void Initial(int size, ArrayList<Integer> from, ArrayList<Integer> to){
		int value = 0;
		int key = 0;
		for(int i = 0; i < size; i++){
			//key = PageRank.TranId.get(from.get(i));
			//value = PageRank.TranId.get(to.get(i));
			key = from.get(i);
			value = to.get(i);
			if(UnderPageRank.outdegrees.containsKey(key)){
				UnderPageRank.outdegrees.get(key).outobject.add(value);
			}
			else{
				UnderPageRank.outdegrees.put(key, new OutDegree(value));
			}
		}
		UnderPageRank.c = PageRank.getIdLenth() - UnderPageRank.outdegrees.size();
		for(Integer k : UnderPageRank.outdegrees.keySet())
		{
			UnderPageRank.outdegrees.get(k).ComputDegree();
		}
	}
	
	public static double[] UnderPageRanking(double[] b, double inivalue, double cvalue, double alpha)
	{
		double[] tmp = new double[b.length];
		for(Integer k : UnderPageRank.outdegrees.keySet())
		{
			double degree = UnderPageRank.outdegrees.get(k).degree; 
			for(int index : UnderPageRank.outdegrees.get(k).outobject)
			{
				tmp[index] += alpha*b[k]*degree;
			}
		}
		for(int i = 0; i < b.length; i++)
		{
			tmp[i] += cvalue + inivalue;
		}
		return tmp;
	}
	
	public static double[] PageRank(int iter, double alpha)
	{
		int length = PageRank.getIdLenth();
		double inivalue = 1.0/length;
		double[] b = new double[length];
		for(int i = 0; i < length; i++)
		{
			b[i] = inivalue;
		}
		for(int i = 0; i < iter; i++)
		{
			b = UnderPageRank.UnderPageRanking(b, inivalue*(1-alpha), 0, alpha);
			System.out.println(i);
		}
		return b;
	}
	
	public static int[] getResult(double[] b, int num)
	{
		int length = PageRank.getIdLenth();
		int[] index = new int[length];
		int[] rank = new int[num];
		for(int i = 0; i < length; i++)
		{
			index[i] = i;
		}		
		double tmp;
		int tmp2;
		for(int i = 0; i < length; i++)
		{
			for(int j = i; j < length; j++)
			{
				if(b[i] < b[j])
				{
					tmp = b[i];
					tmp2 = index[i];
					
					b[i] = b[j];
					index[i] = index[j];
					
					b[j] = tmp;
					index[j] =tmp2;
				}
			}
		}
		for(int i = 0; i < num; i++)
		{
			rank[i] = PageRank.Id.get(index[i]);
		}
		return rank;
	}
	
}
