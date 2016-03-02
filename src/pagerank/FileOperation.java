package pagerank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;


public class FileOperation {
	public ArrayList<Integer> from = new ArrayList<Integer>();
	public ArrayList<Integer> to = new ArrayList<Integer>();
	
	public int ReadFile(String filename)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String str = null;
			String[] tmp;
			while((str = reader.readLine()) != null)
			{
				tmp = str.trim().split("\t");
				this.from.add(new Integer(tmp[0].trim()));
				this.to.add(new Integer(tmp[1].trim()));
			}
			reader.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return this.from.size();
	}
	
	public ArrayList<Integer> getId()
	{
		ArrayList<Integer> tmp = new ArrayList<Integer>(this.from);
		tmp.addAll(this.to);
		return new ArrayList<Integer>(new HashSet<Integer>(tmp));
	}
	
	public static void WriteOut(int[] result, String filename)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			for(int i = 0; i < result.length; i++)
			{
				writer.write(result[i] + "" + "\r\n");
			}
			writer.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void Log(String str, String filename)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
			writer.write(str + "\r\n");
			writer.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
