package com.trade.ticker;

import java.util.concurrent.BlockingQueue;

public class ProcessTicker implements Runnable
{
	BlockingQueue<Ticker> queue;
	
	public ProcessTicker(BlockingQueue<Ticker> queue)
	{
		this.queue=queue;
	}
	
	public void run() 
	{
		Ticker ticker;
		
		try
		{
			while((ticker=queue.take()) != null)
			{
				if(!TickerUtility.checkForWorkingWeek(ticker))
					TickerUtility.findNextWorkingDayForSettlement(ticker);
				
				Statistics.computeStatistics(ticker);
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("ProcessTicker Ending..!");
	}

}
