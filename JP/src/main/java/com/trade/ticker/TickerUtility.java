package com.trade.ticker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import com.trade.enums.WorkWeek;

public class TickerUtility 
{
	private static final int PROCESSORS=1;
	private static Iterable<?> parser=null;
	private static List<Ticker> tickers=new ArrayList<Ticker>();
	private static BlockingQueue<Ticker> queue=new ArrayBlockingQueue<Ticker>(128,true);
	private static List<String> input=new ArrayList<String>();
	private static File file;
	
	/**
	 * This method returns a parser to read tickers from
	 * a file. 
	 * 
	 * TODO - Can implement reading from multiple formats.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Iterable getParser(File file) throws IOException
	{
		if(parser==null)
		{	
			Reader reader=new FileReader(file);
			parser = new CSVParser(reader,CSVFormat.DEFAULT.withHeader());
		}
		
		return parser;
	}
	
	public static boolean addTicker(Ticker ticker)
	{
		return tickers.add(ticker);
	}
	
	public static void init() throws IOException,InterruptedException
	{
		file=new File("");
		BufferedReader reader=new BufferedReader(new FileReader(file.getPath()));
		String line;
		Runnable runnable;
		Thread generateTickerThread = null;
		Thread processTickerThread = null;
		
		while((line = reader.readLine()) != null)
		{
			input.add(line);
		}
		reader.close();
		
		// Create threads to generate ticker
		for(int i=0;i<PROCESSORS;i++)
		{
			runnable=new GenerateTicker(input,queue,parser);
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
		
		do
		{
			newSettlementDate.add(Calendar.DAY_OF_MONTH, WorkWeek.WORKING_DAY_FACTOR);
			ticker.setSettlementDate(newSettlementDate);
		}
		while(!checkForWorkingWeek(ticker));
	}
	
	public static Calendar convertStringToCalendar(String input)
	{
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy");
		
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
