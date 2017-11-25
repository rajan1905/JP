package com.trade.statistics;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.trade.constants.Constants;
import com.trade.ticker.Ticker;

public class StatisticModule 
{
	private static BlockingQueue<Ticker> statisticQueue;
	
	static 
	{
		statisticQueue = new ArrayBlockingQueue<Ticker>(Constants.DEFAULT_QUEUE_SIZE);
	}
	
	public static void init()
	{
		Runnable runnable = new Runnable() 
		{	
			public void run() 
			{
				while(true)
				{
					try
					{
						Ticker ticker = statisticQueue.take();
						System.out.println("Got ticker : "+ticker);
						float amount = ticker.getUnits() * 
								ticker.getPricePerUnit() * 
								ticker.getAgreedFx();
						Statistics.calculateAmountSettledPerDay(ticker , amount);
					}
					catch(InterruptedException ie)
					{
						ie.printStackTrace();
					}
				}
			}
		};
		
		Thread generateStatisticsThread = new Thread(runnable , "GenerateStatisticsThread");
		generateStatisticsThread.start();
	}
	
	public static BlockingQueue<Ticker> getStatisticQueue()
	{
		return statisticQueue;
	}
}
