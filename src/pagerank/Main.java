package pagerank;

public class Main {

	public static void main(String args[])
	{
		
		//parameter select;
		String filename = "D:\\hyx\\BD\\WikiData.txt";
		String pathname = "D:\\hyx\\workspace\\PageRankResult";
		PageRankProcess prp = new PageRankProcess(new FileOperation(), filename);
		//System.out.println(PageRank.getDataInfo()[0] + " "  + PageRank.getDataInfo()[1]);
		int iter = 1000;
		int Num = 100;
		int blocksize = 4000;
		double[] alpha= new double[]{0.7, 0.75, 0.8, 0.85, 0.9, 0.95};
		//common section;
		//String commonlog = pathname + "\\commonlog.txt";
		//String commonheader = "alpha" + "\t" + "readtime(ms)" + "\t" + "usingtime(ms)" + "\t"+"usingmemory(byte)";
		//FileOperation.Log(commonheader, commonlog);
		//prp.CommonPageRank(iter, alpha, Num, pathname, commonlog);
		//common section;
		String commonlog = pathname + "\\undercommonlog.txt";
		String commonheader = "alpha" + "\t" + "readtime(ms)" + "\t" + "usingtime(ms)" + "\t"+"usingmemory(byte)";
		FileOperation.Log(commonheader, commonlog);
		prp.CommonUnderPageRank(iter, alpha, Num, pathname, commonlog);
		//block section;
		//String blocklog = pathname + "\\blocklog.txt";
		//String blockheader = "blocksize" + "\t" + "alpha" + "\t" + "readtime(ms)" + "\t" + "usingtime(ms)" + "\t"+"usingmemory(byte)"; 
		//FileOperation.Log(blockheader, blocklog);
		//prp.BlockPageRank(iter, alpha, Num, blocksize, pathname, blocklog);	
	}
}
