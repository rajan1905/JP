package com.trade.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.trade.ticker.TickerModule;

public class CSVParser implements Parser 
{

	public void parse(File file, TickerModule tickerModule) throws FileNotFoundException 
	{
		Scanner scanner = new Scanner(file);
		
		while(scanner.hasNextLine())
		{
			try 
			{
				tickerModule.getInputQueue().put(scanner.nextLine());
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		scanner.close();
	}

}
