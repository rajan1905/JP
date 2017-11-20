package com.trade.ticker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.trade.enums.WorkWeek;

/**
 * The TickerUtility acts as a starting module for {@link Ticker} generation
 * and processing. This class uses a queue for getting input from the Source
 * module and after processing, passes the Ticker to {@link Statistics} module
 * for generating statistics.
 * 
 * @author rajan.singh
 *
 */
public class TickerUtility 
{
	private static final int PROCESSORS=1;
	private static BlockingQueue<Ticker> queue=new ArrayBlockingQueue<Ticker>(128,true);
	public static BlockingQueue<String> input=new ArrayBlockingQueue<String>(1024,true);
	
	/**
	 * This method is used to start the Ticker creation & processing module.
	 *  
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void init() throws IOException,InterruptedException
	{
		Runnable runnable;
		Thread generateTickerThread = null;
		Thread processTickerThread = null;
		
		// Create threads to generate ticker
		for(int i=0;i<PROCESSORS;i++)
		{
			runnable=new GenerateTicker(input,queue);
			generateTickerThread=new Thread(runnable);
			generateTickerThread.start();
			
			runnable=new ProcessTicker(queue);
			processTickerThread=new Thread(runnable);
			processTickerThread.start();
		}
	}
	
	/**
	 * The method checks whether the settlementDate of {@link Ticker}
	 * is falling in working week as prescribed by ticker's market.
	 * 
	 * @param ticker
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	public static boolean checkForWorkingWeek(Ticker ticker)
	{
		boolean result=false;
		short weekDay=(short) ticker.getSettlementDate().getTime().getDay();
		WorkWeek workWeek=WorkWeek.getMap().get(ticker.getCurrency());
		
		if(workWeek == null)
		{
			workWeek=WorkWeek.DEFAULT;
		}
		
		if(weekDay >= workWeek.getStart() && weekDay <= workWeek.getEnd())
			result=true;
		
		return result;
	}
	
	/**
	 * This method finds the next working day for a {@link Ticker}
	 * and sets the settlementDate of the Ticker to that day.
	 * 
	 * @param ticker
	 */
	public static void findNextWorkingDayForSettlement(Ticker ticker)
	{
		Calendar newSettlementDate=ticker.getSettlementDate();
		
		while(!checkForWorkingWeek(ticker))
		{
			newSettlementDate.add(Calendar.DAY_OF_MONTH, WorkWeek.WORKING_DAY_FACTOR);
			ticker.setSettlementDate(newSettlementDate);
		}
		
	}
	
	/**
	 * Convert string representation 'dd MMM yyyy' to corresponding {@link Calendar}
	 * object. If the input representation is of different format, an exception 
	 * of type {@link ParseException} would be thrown.
	 * 
	 * @param input
	 * @return {@link Calendar}
	 */
	public static Calendar convertStringToCalendar(String input)
	{
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
		
		try
		{
			calendar.setTime(sdf.parse(input));
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		
		return calendar;
	}
}
