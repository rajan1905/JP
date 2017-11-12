package com.trade.ticker;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.csv.CSVRecord;

public class GenerateTicker implements Runnable
{
	private List<String> input;
	private BlockingQueue<Ticker> queue;
	private Iterable<?> parser;
	
	public GenerateTicker(List<String> input, BlockingQueue<Ticker> queue, Iterable<?> parser) 
	{
		this.input=input;
		this.queue=queue;
		this.parser=parser;
	}

	public void run() 
	{
		Ticker ticker=null;
		
		while(input.size()>0)
		{
			String tuple=input.get(0);
			input.remove(0);
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
			
			try
			{
				queue.put(ticker);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
