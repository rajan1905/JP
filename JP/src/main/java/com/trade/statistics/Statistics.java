package com.trade.statistics;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.trade.comparators.ValueComparator;
import com.trade.constants.Constants;
import com.trade.ticker.Ticker;

/**
 * The Statistics class is used to perform all statistic-computation
 * related tasks. 
 * 
 * @author rajan.singh
 *
 */
public class Statistics 
{
	public static Map<Calendar,Float> incomingAmountSettledPerDay;
	public static Map<Calendar,Float> outgoingAmountSettledPerDay;
	public static Map<String,Float> tickerRankByIncoming;
	public static Map<String,Float> tickerRankByOutgoing;
	
	static
	{
		incomingAmountSettledPerDay = new HashMap<Calendar, Float>();
		outgoingAmountSettledPerDay = new HashMap<Calendar, Float>();
		tickerRankByIncoming = new HashMap<String, Float>();
		tickerRankByOutgoing = new HashMap<String, Float>();
	}
	
	private Statistics() {}
	
	/**
	 * This method is used to calculate the amount settled per Calendar day.
	 * 
	 * @param ticker
	 * @param amount
	 */
	public static void calculateAmountSettledPerDay(Ticker ticker, float amount)
	{
		Map<Calendar,Float> map = null;
		Calendar settlementDate = ticker.getSettlementDate();
		
		map = ticker.getAction() == Constants.SELL ? incomingAmountSettledPerDay : outgoingAmountSettledPerDay;
		
		float currentValue = map.get(settlementDate) == null ? 0 : map.get(settlementDate);
		currentValue += amount;
		
		map.put(settlementDate, currentValue);
		
		computeRanking(ticker , currentValue);
	}
	
	/**
	 * This method is used to compute the ranking by incoming/outgoing amount 
	 * for each trade entity.
	 * 
	 * @param ticker
	 * @param amount
	 */
	public static void computeRanking(Ticker ticker, float amount)
	{
		Map<String,Float> map = null;
		String entity = ticker.getEntity();
		
		map = ticker.getAction() == Constants.SELL ? tickerRankByIncoming : tickerRankByOutgoing;
		
		float currentValue = map.get(entity) == null ? 0 : map.get(entity);
		currentValue += amount;
		
		map.put(entity, currentValue);
	}
	
	@SuppressWarnings("rawtypes")
	public static String printStatistics()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\tShowing amount settled incoming per day\n");
		sb.append("\t---------------------------------------\n\n");
		
		Iterator<?> iterator = incomingAmountSettledPerDay.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Map.Entry pair = (Map.Entry) iterator.next();
			Calendar calendar = (Calendar) pair.getKey();
			sb.append("Date : "+calendar.getTime()+" Amount : "+pair.getValue()+"\n\n");
			
		}
		
		iterator = outgoingAmountSettledPerDay.entrySet().iterator();
		
		sb.append("\tShowing amount settled outging per day\n");
		sb.append("\t---------------------------------------\n\n");
		
		while(iterator.hasNext())
		{
			Map.Entry pair = (Map.Entry) iterator.next();
			Calendar calendar = (Calendar) pair.getKey();
			sb.append("Date : "+calendar.getTime()+" Amount : "+pair.getValue()+"\n\n");
		}
		
		ValueComparator valueComparator=new ValueComparator(tickerRankByIncoming);
		TreeMap<String,Float> sortedMap=new TreeMap<String, Float>(valueComparator);
		
		int counter = 1;
		
		sortedMap.putAll(tickerRankByIncoming);
		
		iterator = sortedMap.entrySet().iterator();
		
		sb.append("\tShowing ranking by incoming per day\n");
		sb.append("\t---------------------------------------\n\n");
		
		while(iterator.hasNext())
		{
			Map.Entry pair = (Map.Entry) iterator.next();
			sb.append(counter + ": "+pair.getKey()+" , Amount : "+pair.getValue()+"\n\n");
			counter++;
		}
		
		counter = 1;
		
		valueComparator=new ValueComparator(tickerRankByOutgoing);
		sortedMap=new TreeMap<String, Float>(valueComparator);
		
		sortedMap.putAll(tickerRankByOutgoing);
		
		iterator = sortedMap.entrySet().iterator();
		
		sb.append("\tShowing ranking by outging per day\n");
		sb.append("\t---------------------------------------\n\n");
		
		while(iterator.hasNext())
		{
			Map.Entry pair = (Map.Entry) iterator.next();
			sb.append(counter + ": "+pair.getKey()+" , Amount : "+pair.getValue()+"\n\n");
			counter++;
		}
		
		return sb.toString();
	}
}
