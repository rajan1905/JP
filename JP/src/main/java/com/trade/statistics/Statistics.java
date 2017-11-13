package com.trade.statistics;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.trade.ticker.Ticker;

public class Statistics 
{
	public static Map<Calendar,Float> incomingAmountSettledPerDay;
	public static Map<Calendar,Float> outgoingAmountSettledPerDay;
	private static long timeStamp=0;
	public static Map<String,Float> entityIncomingRank;
	public static Map<String,Float> entityOutgoingRank;
	
	static
	{
		incomingAmountSettledPerDay=new HashMap<Calendar, Float>();
		outgoingAmountSettledPerDay=new HashMap<Calendar, Float>();
		entityIncomingRank=new HashMap<String, Float>();
		entityOutgoingRank=new HashMap<String, Float>();
	}
	
	private Statistics() {}
	
	public static void addToAmountSettledPerDay(Ticker ticker)
	{
		Map<Calendar,Float> map=null;
		Calendar settlementDate=ticker.getSettlementDate();
		float amount= ticker.getUnits() * ticker.getPricePerUnit() * ticker.getAgreedFx();
		
		if(ticker.getAction()=='S')
			map=incomingAmountSettledPerDay;
		else
			map=outgoingAmountSettledPerDay;
		
		if(map.containsKey(settlementDate))
		{
			float value=map.get(settlementDate);
			value += amount;
			map.remove(settlementDate);
			map.put(settlementDate, value);
		}
		else
		{
			map.put(settlementDate, amount);
		}
	}
	
	public static void computeRanking(Ticker ticker)
	{
		Map<String,Float> map=null;
		String entity=ticker.getEntity();
		float amount= ticker.getUnits() * ticker.getPricePerUnit() * ticker.getAgreedFx();
		
		if(ticker.getAction()=='S')
			map=entityIncomingRank;
		else
			map=entityOutgoingRank;
		
		if(map.containsKey(entity))
		{
			float value=map.get(entity);
			value += amount;
			map.remove(entity);
			map.put(entity, value);
		}
		else
		{
			map.put(entity, amount);
		}
	}
	
	public synchronized static void computeStatistics(Ticker ticker)
	{
		addToAmountSettledPerDay(ticker);
		computeRanking(ticker);
		timeStamp=System.nanoTime();
	}
	
	public static long getTimeStamp()
	{
		return timeStamp;
	}
}
