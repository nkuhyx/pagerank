package pagerank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import Jama.Matrix;

public class PageRank {
	private static int[] count;
	private static int Id_length;
	private static int sample_length;
	public static HashMap<Integer, Integer> Id = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Integer> TranId = new HashMap<Integer, Integer>();
	public static int block;
	
	public static void Ini(int sample_length, ArrayList<Integer> Id)
	{
		PageRank.sample_length = sample_length;
		PageRank.Id_length = Id.size();
		for(int i = 0; i < PageRank.Id_length; i++)
		{
			PageRank.TranId.put(Id.get(i), i);
			PageRank.Id.put(i, Id.get(i));
		}
	}	
	
	public static int[] getDataInfo()
	{
		return new int[]{PageRank.Id_length, PageRank.sample_length};
	}
	
	public static int getIdLenth()
	{
		return PageRank.Id_length;
	}
	
	public static int getSampleLength()
	{
		return PageRank.sample_length;
	}
	
	public static ArrayList<Integer> buildMap(ArrayList<Integer> arr)
	{
		for(int i = 0; i < PageRank.sample_length; i++)
		{
			arr.set(i, PageRank.TranId.get(arr.get(i)));
		}
		return arr;
	}
	
	public static void getSum(ArrayList<Integer> arr)
	{
		PageRank.count = new int[PageRank.Id_length];
		for(int i = 0; i < PageRank.sample_length; i++)
		{
			PageRank.count[arr.get(i)] += 1;
		}
	}
	
	public static ArrayList<Integer> checkZero(ArrayList<Integer> arr)
	{
		ArrayList<Integer> tmp = new ArrayList<Integer>(new HashSet<Integer>(arr));
		ArrayList<Integer> queue = new ArrayList<Integer>();
		for(int i = 0; i < PageRank.Id_length; i++)
		{
			queue.add(i);
		}
		queue.removeAll(tmp);
		return queue;
	}
	
	public static Matrix buildMatrix(ArrayList<Integer> from, ArrayList<Integer> to)
	{
		Matrix matrix = new Matrix(PageRank.Id_length, PageRank.Id_length);
		for(int i = 0; i < PageRank.sample_length; i++)
		{
			matrix.set(to.get(i), from.get(i), 1);
		}
		return matrix;
	}
	
	public static Matrix fullZeroColumn(Matrix matrix, ArrayList<Integer> checkzero)
	{
		for(int i = 0; i < checkzero.size(); i++)
		{
			PageRank.count[checkzero.get(i)] = PageRank.Id_length;
			for(int j = 0; j < PageRank.Id_length; j++)
			{
				matrix.set(j, checkzero.get(i), 1);
			}
		}
		return matrix;
	}
	
	public static Matrix getTransMatrix(Matrix matrix)
	{
		System.out.println("checkpoint!");
		for(int i = 0; i < PageRank.Id_length; i++)
		{
			for(int j = 0; j < PageRank.Id_length; j++)
			{
				if(matrix.get(j, i) == 1)
				{
					matrix.set(j, i, 1/(double)PageRank.count[i]);
				}
			}
		}
		return matrix;
	}
	public static Matrix getE()
	{
		Matrix e = new Matrix(PageRank.Id_length, 1);
		for(int i = 0; i < PageRank.Id_length; i++)
		{
			e.set(i, 0, 1/(double)(PageRank.Id_length));
		}
		return e;
	}
	
	public static Matrix PageRanking(int iter, double alpha, Matrix M, Matrix e)
	{
		Matrix b = e;
		Matrix tmp = e.times(1 - alpha);
		for(int i = 0; i < iter; i++)
		{
			b = M.times(b).times(alpha).plus(tmp);
			System.out.println("commonpagerank:"+ i+1);
		}
		return b;
	}
	
	public static double[] getNum(Matrix matrix)
	{
		double[] Num = new double[PageRank.Id_length];
		for(int i = 0; i < PageRank.Id_length; i++)
		{
			Num[i] = matrix.get(i, 0);
		}
		return Num;
	}
	
	public static int[] getResult(Matrix matrix, int num)
	{
		double[] result = new double[PageRank.Id_length];
		int[] index = new int[PageRank.Id_length];
		int[] rank = new int[num];
		for(int i = 0; i < PageRank.Id_length; i++)
		{
			result[i] = matrix.get(i, 0);
			index[i] = i;
		}		
		double tmp;
		int tmp2;
		for(int i = 0; i < PageRank.Id_length; i++)
		{
			for(int j = i; j < PageRank.Id_length; j++)
			{
				if(result[i] < result[j])
				{
					tmp = result[i];
					tmp2 = index[i];
					
					result[i] = result[j];
					index[i] = index[j];
					
					result[j] = tmp;
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
	
	public static Matrix MatrixMontage(ArrayList<Matrix> matrixs, int blocksize, int columnwidth)
	{
		Matrix matrix = new Matrix(PageRank.Id_length, 1);
		int i = 0; 
		for(;i < matrixs.size()-1; i++)
		{
			matrix.setMatrix(i*blocksize, (i+1)*blocksize-1, 0, columnwidth-1, matrixs.get(i));
		}
		matrix.setMatrix(i*blocksize, PageRank.Id_length - 1, 0, columnwidth-1, matrixs.get(matrixs.size()-1));
		return matrix;
	}
	
	public static ArrayList<Matrix> VectorSplit(Matrix matrix, int blocksize)
	{
		ArrayList<Matrix> vectors = new ArrayList<Matrix>();
		int i = 0;
		for(; i < PageRank.Id_length/blocksize; i++)
		{
			vectors.add(matrix.getMatrix(i*blocksize, (i+1)*blocksize-1, 0, 0));	
		}
		if(i*blocksize <= PageRank.Id_length - 1)
		{
			vectors.add(matrix.getMatrix(i*blocksize, PageRank.Id_length-1, 0, 0));
		}
		return vectors;
	}
	
	public static ArrayList<Matrix> MatrixSplit2Strips(Matrix matrix, int blocksize)
	{
		ArrayList<Matrix> matrixs = new ArrayList<Matrix>();
		int i = 0; 
		for(;i < PageRank.Id_length/blocksize; i++)
		{
			matrixs.add(matrix.getMatrix(i*blocksize, (i+1)*blocksize-1,0, PageRank.Id_length-1));
		}
		if(i * blocksize <= PageRank.Id_length-1)
		{
			matrixs.add(matrix.getMatrix(i*blocksize, PageRank.Id_length - 1, 0, PageRank.Id_length - 1));
		}
		return matrixs;
	}
	
	
	
	public static Matrix BlockPageRanking(double alpha,  ArrayList<Matrix> M,  Matrix b, Matrix e, int blocksize)
	{
		ArrayList<Matrix> smallb = new ArrayList<Matrix>();
		for(int i = 0; i < M.size(); i++)
		{
			smallb.add(M.get(i).times(b).times(alpha));
		}
		return PageRank.MatrixMontage(smallb, blocksize, 1).plus(e);
	}
	
	public static Matrix BlockPageRank(int iter, double alpha, Matrix M, Matrix e, int blocksize)
	{
		ArrayList<Matrix> Ms = PageRank.MatrixSplit2Strips(M, blocksize);
		Matrix b = e;
		e = e.times(1 - alpha);
		for(int i = 0; i < iter; i++)
		{
			b = PageRank.BlockPageRanking(alpha, Ms, b, e, blocksize);
			System.out.println("blockpagernak:"+ i);
		}
		return b;
	}	
	
	public static ArrayList<Matrix> MatrixSplit(Matrix matrix, int blocksize)
	{
		PageRank.block = (int)Math.ceil(PageRank.Id_length/(float)blocksize);
		int blocks = PageRank.block * PageRank.block;
		ArrayList<Matrix> matrixs = new ArrayList<Matrix>(blocks);
		int i = 0;
		for(; i < PageRank.block - 1; i++)
		{
			int j = 0;
			for(; j < PageRank.block - 1; j++)
			{
				matrixs.add(matrix.getMatrix(i*blocksize, (i+1)*blocksize-1, j*blocksize, (j+1)*blocksize-1));
			}
			matrixs.add(matrix.getMatrix(i*blocksize, (i+1)*blocksize-1, j*blocksize, PageRank.Id_length -1));
		}
		int j = 0;
		for(; j < PageRank.block - 1; j++)
		{
			matrixs.add(matrix.getMatrix(i*blocksize, PageRank.Id_length -1, j*blocksize, (j+1)*blocksize-1));
		}
		matrixs.add(matrix.getMatrix(i*blocksize, PageRank.Id_length -1, j*blocksize, PageRank.Id_length -1));
		return matrixs;
	}
	
	public static ArrayList<Matrix> BlockPageRanking(double alpha, ArrayList<Matrix> M, ArrayList<Matrix> e, ArrayList<Matrix> b)
	{
		ArrayList<Matrix> tmp = new ArrayList<Matrix>();
		Matrix total;
		for(int i = 0; i < PageRank.block; i++)
		{
			total = M.get(i*PageRank.block).times(b.get(0)) ;
			for(int j = 1; j < PageRank.block; j++)
			{
				 total = total.plus(M.get(i*PageRank.block+j).times(b.get(j)));
			}
			tmp.add(total.times(alpha).plus(e.get(i)));
		}
		return tmp;	
	}
	
	public static Matrix Block_PageRank(int iter, double alpha, Matrix M, Matrix e, int blocksize)
	{
		ArrayList<Matrix> Ms = PageRank.MatrixSplit(M, blocksize);
		ArrayList<Matrix> es = PageRank.VectorSplit(e, blocksize);
		ArrayList<Matrix> bs = new ArrayList<Matrix>(es);
		for(Matrix E: es)
		{
			E = E.times(1-alpha);
		}
		for(int i = 0; i < iter; i++)
		{
			bs = PageRank.BlockPageRanking(alpha, Ms, es, bs);
			System.out.println("blockpagerank:"+i);
		}
		return PageRank.MatrixMontage(bs, blocksize, 1);
	}
	/*
	public static Matrix BlockPageRanking(int iter, double alpha, Matrix M, Matrix e, int index)
	{
		System.out.println("the " + index + " block: +\r\n");
		return PageRank.PageRanking(iter, alpha, M, e);
	}	
	
	public static Matrix BlockPageRank(int iter, double alpha, Matrix M, Matrix e, int blocksize)
	{
		ArrayList<Matrix> Ms = PageRank.MatrixSplit(M, blocksize);
		ArrayList<Matrix> es = PageRank.VectorSplit(e, blocksize);
		Matrix[] bs = new Matrix[es.size()];
		System.out.println(es.size());
		for(int i = 0; i < es.size(); i++)
		{
			bs[i] =  PageRank.BlockPageRanking(iter, alpha, Ms.get(i), es.get(i), i+1);
		}
		return PageRank.MatrixMontage(bs, blocksize, 1);
	}
		
	public static void out(Matrix matrix)
	{
		for(int i = 1455; i < 1456; i++)
		{
			for(int j = 0; j < PageRank.Id_length; j++)
			{
				System.out.print(matrix.get(j, i)+"\r\n");
			}
			System.out.println("--------------------------\r\n");
		}
	}
*/	
	
}
