package com.trade.ticker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.trade.constants.Constants;
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
	/**
	 * The method checks whether the settlementDate of {@link Ticker}
	 * is falling in working week as prescribed by ticker's market.
	 * 
	 * @param ticker
	 * @return boolean
	 */
	public static boolean checkForWorkingWeek(Ticker ticker)
	{
		boolean result = false;
		short weekDay = (short) (ticker.getSettlementDate().get(Calendar.DAY_OF_WEEK)-1);
		WorkWeek workWeek = WorkWeek.getMap().get(ticker.getCurrency());
		
		if(workWeek == null)
		{
			workWeek = WorkWeek.DEFAULT;
		}
		
		if(weekDay >= workWeek.getStart() && weekDay <= workWeek.getEnd())
			result = true;
		
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
		Calendar newSettlementDate = ticker.getSettlementDate();
		
		while(!checkForWorkingWeek(ticker))
		{
			newSettlementDate.add(Calendar.DAY_OF_MONTH, Constants.WORKING_DAY_FACTOR);
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
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
		
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
	
	public static void performTickerValidation(Ticker ticker)
	{
		
	}
}
