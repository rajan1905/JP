package com.trade.ticker;

import java.util.concurrent.BlockingQueue;

import com.trade.statistics.Statistics;

/**
 * The ProcessTicker class represents a task of processing a ticker 
 * by reading from an input queue of {@link Ticker}. The ticker is 
 * processed for correct settlement date as per the market.
 * 
 * The ticker after processing is passed to {@link Statistics} module 
 * for statistics generation.
 * 
 * @author rajan.singh
 *
 */
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
				
				Statistics.statisticQueue.put(ticker);
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("ProcessTicker Ending..!");
	}

}
