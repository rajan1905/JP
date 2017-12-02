package com.assignment.JP;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.trade.statistics.StatisticModule;
import com.trade.statistics.Statistics;
import com.trade.ticker.TickerModule;
import com.trade.ticker.TickerUtility;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for Trade settlement app.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testTradeResults() throws IOException, InterruptedException 
    {
    	Map<Calendar,Float> incomingAmountSettledPerDay = new HashMap<Calendar, Float>();
    	Map<Calendar,Float> outgoingAmountSettledPerDay = new HashMap<Calendar, Float>();
    	Map<String,Float> tickerByIncomingRank = new HashMap<String, Float>();
    	Map<String,Float> tickerByOutgoingRank = new HashMap<String, Float>();
    	
    	final TickerModule tickerModule = new TickerModule();
		tickerModule.init();
		
		// 2 JAN 2016 - SATURDAY - SGP (SUN-THU) - Applicable Settlement Date - 3 JAN 2016  : FOO  Amount - 10025
		// 7 JAN 2016 - THURSDAY - AED (SUN-THU) - Applicable Settlement Date - 7 JAN 2016	: BAR  Amount - 14899.5
		// 18 FEB 2016 - THURSDAY - NORMAL (MON-FRI) - Applicable Settlement Date - 18 FEB 2016	: AAA  Amount - 100
		final String[] tickers= 
			{
					"foo,B,0.50,SGP,01 Jan 2016,02 Jan 2016,200,100.25", 
					"bar,S,0.22,AED,05 Jan 2016,07 Jan 2016,450,150.5",
					"aaa,S,0.1,AAA,05 Jan 2016,18 Feb 2016,1000,1"
			};
		
		// OUTPUT SHOULD BE
		
		//	incomingAmountSettlePerday [{7 jan 2016 , 14899.5} , {18 feb 2016 , 100}]

		incomingAmountSettledPerDay.put(TickerUtility.convertStringToCalendar("7 Jan 2016"), (float) 14899.5);
		incomingAmountSettledPerDay.put(TickerUtility.convertStringToCalendar("18 Feb 2016"), (float) 100.0);
		
		//	outgoingAmountSettlePerday [{3 jan 2016 , 10025.0}]
		outgoingAmountSettledPerDay.put(TickerUtility.convertStringToCalendar("3 Jan 2016"), (float) 10025.0);
		
		//	tickerByIncomingRank [{BAR , 14899.5} , {aaa , 100.0}]
		tickerByIncomingRank.put("bar", (float) 14899.5);
		tickerByIncomingRank.put("aaa", (float) 100.0);
		
		//	tickerByIncomingRank [{BAR , 14899.5}]
		tickerByOutgoingRank.put("foo", (float) 10025.0);
			
			
		/*
		 * Start the SourceModule. You can have more than one SourceModule
		 * with different sources.
		 */
		Runnable runnable=new Runnable() 
		{
			
			public void run() 
			{
				int i=0;
				
				while(i<tickers.length)
				{
					String str=tickers[i];
					try {
						tickerModule.getInputQueue().put(str);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
				
			}
		};
		
		Thread input=new Thread(runnable);
		input.start();
		
		StatisticModule.init();
		
		Thread.sleep(1000);
		
		assertEquals(incomingAmountSettledPerDay, Statistics.incomingAmountSettledPerDay);
		assertEquals(outgoingAmountSettledPerDay, Statistics.outgoingAmountSettledPerDay);
		assertEquals(tickerByIncomingRank, Statistics.tickerRankByIncoming);
		assertEquals(tickerByOutgoingRank, Statistics.tickerRankByOutgoing);	
    }
}
