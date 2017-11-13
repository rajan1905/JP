package com.trade.ticker;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.trade.enums.WorkWeek;

public class TickerUtility 
{
	private static final int PROCESSORS=1;
	private static BlockingQueue<Ticker> queue=new ArrayBlockingQueue<Ticker>(128,true);
	public static BlockingQueue<String> input=new ArrayBlockingQueue<String>(1024,true);
	
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
	
	public static void findNextWorkingDayForSettlement(Ticker ticker)
	{
		Calendar newSettlementDate=ticker.getSettlementDate();
		
		while(!checkForWorkingWeek(ticker))
		{
			newSettlementDate.add(Calendar.DAY_OF_MONTH, WorkWeek.WORKING_DAY_FACTOR);
			ticker.setSettlementDate(newSettlementDate);
		}
		
	}
	
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
