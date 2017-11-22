package com.trade.ticker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.trade.constants.Constants;

public class TickerModule 
{
	private BlockingQueue<Ticker> tickerQueue;
	private BlockingQueue<String> inputQueue;
	private ExecutorService threadPool;
	
	public TickerModule() 
	{
		tickerQueue = new ArrayBlockingQueue<Ticker>(Constants.DEFAULT_QUEUE_SIZE);
		inputQueue = new ArrayBlockingQueue<String>(Constants.DEFAULT_QUEUE_SIZE);
		threadPool = Executors.newCachedThreadPool();
	}
	
	public void init() throws IOException, InterruptedException
	{
		List<Runnable> jobList = new ArrayList<Runnable>();
		
		jobList.add(new GenerateTicker(inputQueue, tickerQueue));
		jobList.add(new ProcessTicker(tickerQueue));
		
		for(Runnable job : jobList)
			threadPool.execute(job);
	}
	
	public BlockingQueue<String> getInputQueue()
	{
		return inputQueue;
	}
	
	public ExecutorService getThreadPool()
	{
		return threadPool;
	}
}
