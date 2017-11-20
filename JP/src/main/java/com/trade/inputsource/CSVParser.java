package com.trade.inputsource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class CSVParser implements Parser 
{

	public void parse(File file, BlockingQueue<String> queue) throws FileNotFoundException, InterruptedException 
	{
		Scanner scanner=new Scanner(file);
		
		while(scanner.hasNextLine())
		{
			queue.put(scanner.nextLine());
		}
		
		scanner.close();
	}

}
