package com.trade.ticker;

import java.util.concurrent.BlockingQueue;

/**
 * The GenerateTicker class is used to generate {@link Ticker}
 * by reading from an input queue(containing ticker entries).
 * 
 * The ticker is then added to {@link ProcessTicker} queue
 * for further processing.
 * 
 * @author rajan.singh
 *
 */
public class GenerateTicker implements Runnable
{
	private BlockingQueue<String> input;
	private BlockingQueue<Ticker> queue;
	
	public GenerateTicker(BlockingQueue<String> input, BlockingQueue<Ticker> queue) 
	{
		this.input=input;
		this.queue=queue;
	}

	public void run() 
	{
		Ticker ticker=null;
		
		while(input.size()>0)
		{
			try
			{
				String tuple=input.take();
				String[] t=tuple.split(",");
				
				ticker=Ticker.builder()
						.entity(t[0])
						.action(t[1].charAt(0))
						.agreedFx(Float.parseFloat(t[2]))
						.currency(t[3])
						.instructionDate(TickerUtility.convertStringToCalendar(t[4]))
						.settlementDate(TickerUtility.convertStringToCalendar(t[5]))
						.units(Integer.parseInt(t[6]))
						.pricePerUnit(Float.parseFloat(t[7]))
						.build();
				
				queue.put(ticker);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
