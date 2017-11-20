package com.trade.statistics;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.trade.comparators.ValueComparator;
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
	public static BlockingQueue<Ticker> statisticQueue;
	public static Map<Calendar,Float> incomingAmountSettledPerDay;
	public static Map<Calendar,Float> outgoingAmountSettledPerDay;
	public static Map<String,Float> entityIncomingRank;
	public static Map<String,Float> entityOutgoingRank;
	private static final char SELL='S';
	
	static
	{
		statisticQueue=new ArrayBlockingQueue<Ticker>(1024,true);
		incomingAmountSettledPerDay=new HashMap<Calendar, Float>();
		outgoingAmountSettledPerDay=new HashMap<Calendar, Float>();
		entityIncomingRank=new HashMap<String, Float>();
		entityOutgoingRank=new HashMap<String, Float>();
	}
	
	private Statistics() {}
	
	/**
	 * This method is used to initialize the Statistics module.
	 */
	public static void init()
	{
		Runnable runnable=new Runnable()
		{
			public void run() 
			{
				while(true)
				{
					try
					{
						Ticker ticker=statisticQueue.take();
						float amount= ticker.getUnits() * ticker.getPricePerUnit() * ticker.getAgreedFx();
						
						calculateAmountSettledPerDay(ticker , amount);
					}
					catch(InterruptedException ie)
					{
						ie.printStackTrace();
					}
				}
				
			}
		};
		
		Thread generateStatistics=new Thread(runnable , "StatisticThread");
		generateStatistics.start();
	}
	
	/**
	 * This method is used to calculate the amount settled per Calendar day.
	 * 
	 * @param ticker
	 * @param amount
	 */
	public static void calculateAmountSettledPerDay(Ticker ticker, float amount)
	{
		Map<Calendar,Float> map=null;
		Calendar settlementDate=ticker.getSettlementDate();
		
		map = ticker.getAction()==SELL ? incomingAmountSettledPerDay : outgoingAmountSettledPerDay;
		
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
		Map<String,Float> map=null;
		String entity=ticker.getEntity();
		
		map = ticker.getAction() == SELL ? entityIncomingRank : entityOutgoingRank;
		
		float currentValue = map.get(entity) == null ? 0 : map.get(entity);
		currentValue += amount;
		
		map.put(entity, currentValue);
		
	}
	
	@SuppressWarnings("rawtypes")
	public static void printStatistics()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("\tShowing amount settled incoming per day\n");
		sb.append("\t---------------------------------------\n\n");
		
		Iterator<?> iterator=incomingAmountSettledPerDay.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Map.Entry pair=(Map.Entry) iterator.next();
			Calendar calendar=(Calendar) pair.getKey();
			sb.append("Date : "+calendar.getTime()+" Amount : "+pair.getValue()+"\n\n");
			
		}
		
		iterator=outgoingAmountSettledPerDay.entrySet().iterator();
		
		sb.append("\tShowing amount settled outging per day\n");
		sb.append("\t---------------------------------------\n\n");
		
		while(iterator.hasNext())
		{
			Map.Entry pair=(Map.Entry) iterator.next();
			Calendar calendar=(Calendar) pair.getKey();
			sb.append("Date : "+calendar.getTime()+" Amount : "+pair.getValue()+"\n\n");
		}
		
		ValueComparator valueComparator=new ValueComparator(entityIncomingRank);
		TreeMap<String,Float> sortedMap=new TreeMap<String, Float>(valueComparator);
		
		sortedMap.putAll(entityIncomingRank);
		
		sb.append(sortedMap);
		
		valueComparator=new ValueComparator(entityOutgoingRank);
		sortedMap=new TreeMap<String, Float>(valueComparator);
		
		sortedMap.putAll(entityOutgoingRank);
		sb.append("\n\n");
		sb.append(sortedMap);
		
		System.out.println(sb.toString());
	}
}
