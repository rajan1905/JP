package com.trade.main;

import java.io.File;
import java.io.IOException;

import com.trade.source.SourceModule;
import com.trade.statistics.StatisticModule;
import com.trade.statistics.Statistics;
import com.trade.ticker.TickerModule;

public class EntryPoint 
{
	static long lastResult=0;
	
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		/*
		 * Start the ticker module
		 */
		
		TickerModule tickerModule = new TickerModule();
		tickerModule.init();
		
		String fileLocation = "com/trade/resources/input.csv";
		ClassLoader classLoader = EntryPoint.class.getClassLoader();
		File file = new File(classLoader.getResource(fileLocation).getFile());
		
		/*
		 * Start the SourceModule. You can have more than one SourceModule
		 * with different sources.
		 */
		SourceModule sourceModule = new SourceModule(file , null, tickerModule.getInputQueue());
		sourceModule.init();
		
		StatisticModule.init();
		
		Thread.sleep(3000);
		System.out.println(Statistics.printStatistics());
	}
}
