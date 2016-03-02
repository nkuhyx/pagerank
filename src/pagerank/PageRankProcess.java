package pagerank;

import java.util.ArrayList;
import Jama.Matrix;

public class PageRankProcess {
	private Matrix M;
	private Matrix e;
	private long ReadTime;
	
	public PageRankProcess(FileOperation fo, String filename)
	{
		long start = System.currentTimeMillis();
		int length = fo.ReadFile(filename);
		PageRank.Ini(length, fo.getId());
		ArrayList<Integer> from = PageRank.buildMap(fo.from);
		ArrayList<Integer> to = PageRank.buildMap(fo.to);
		PageRank.getSum(from);
		Matrix matrix = PageRank.fullZeroColumn(PageRank.buildMatrix(from, to),PageRank.checkZero(from));
		this.M = PageRank.getTransMatrix(matrix);
		this.e = PageRank.getE();
		System.out.println("Matrix over");
		UnderPageRank.Initial(PageRank.getSampleLength(), from, to);
		long end = System.currentTimeMillis();
		this.ReadTime = end - start;
	}
	public void CommonUnderPageRank(int iter, double[] alpha, int Num, String pathname, String log)
	{
		long start; 
		long usingtime = 0;
		long usingmemory = 0;
		double[] b; 
		int[] result;
		String tmp;
		String filename;
		for(int i = 0; i < alpha.length; i++)
		{
			start = System.currentTimeMillis();
			b = UnderPageRank.PageRank(iter, alpha[i]);
			System.out.println("alpha "+alpha[i] + " is over!");
			result = UnderPageRank.getResult(b, Num);
			filename = "\\alpha="+ alpha[i] +".txt";
			FileOperation.WriteOut(result, pathname + filename);
			usingtime = System.currentTimeMillis() - start;
			usingmemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			tmp = alpha[i] + "\t" + this.ReadTime + "\t" +  usingtime + "\t" + usingmemory;
			FileOperation.Log(tmp , log);
		}
		System.out.println("calculating over!");
		//
	}
	
	public void CommonPageRank(int iter, double[] alpha, int Num, String pathname, String log)
	{
		long start; 
		long usingtime = 0;
		long usingmemory = 0;
		Matrix b;
		int[] result;
		String tmp;
		String filename;
		for(int i = 0; i < alpha.length; i++)
		{
			start = System.currentTimeMillis();
			b = PageRank.PageRanking(iter, alpha[i], this.M, this.e);
			System.out.println("alpha "+alpha[i] + " is over!");
			result = PageRank.getResult(b, Num);
			filename = "\\alpha="+ alpha[i] +".txt";
			FileOperation.WriteOut(result, pathname + filename);
			usingtime = System.currentTimeMillis() - start;
			usingmemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			tmp = alpha[i] + "\t" + this.ReadTime + "\t" +  usingtime + "\t" + usingmemory;
			FileOperation.Log(tmp , log);
		}
		System.out.println("calculating over!");
	}
	
	public void BlockPageRank(int iter, double[] alpha, int Num, int blocksize, String pathname, String log)
	{
		long start; 
		long usingtime = 0;
		long usingmemory = 0;
		String tmp;
		Matrix b;
		int[] result;
		String filename;;
		for(int i = 0; i < alpha.length; i++)
		{
			start = System.currentTimeMillis();
			b =PageRank.BlockPageRank(iter, alpha[i], this.M, this.e, blocksize);
			System.out.println("alpha"+alpha[i] + "is over!");
			result = PageRank.getResult(b, Num);
			filename = "\\Block_"+blocksize+"&alpha_"+ alpha[i] +".txt";
			FileOperation.WriteOut(result, pathname + filename);
			usingtime = System.currentTimeMillis() - start;
			usingmemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			double blocksplit = Math.pow(PageRank.block, 2);
			tmp = blocksize +"\t" +  alpha[i] + "\t" + this.ReadTime + "\t" +  usingtime/blocksplit + "\t" + usingmemory/blocksplit;
			FileOperation.Log(tmp , log);
		}
		System.out.println("calculating over!");
	}	
}
