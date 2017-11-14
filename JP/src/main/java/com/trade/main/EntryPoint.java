package com.trade.main;

import java.io.File;
import java.io.IOException;

import com.trade.inputsource.Source;
import com.trade.statistics.Statistics;
import com.trade.ticker.TickerUtility;

public class EntryPoint 
{

	public static void main(String[] args) throws InterruptedException, IOException 
	{
		String fileLocation="com/trade/resources/input.txt";
		ClassLoader classLoader=EntryPoint.class.getClassLoader();
		File file=new File(classLoader.getResource(fileLocation).getFile());
		
		Source source=new Source(file,TickerUtility.input);
		source.init();
		
		TickerUtility.init();
		
		Thread.sleep(2000);
		
		Statistics.printStatistics();
	}
}
