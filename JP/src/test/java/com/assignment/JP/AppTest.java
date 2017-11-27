package com.assignment.JP;

import java.io.IOException;

import com.trade.statistics.StatisticModule;
import com.trade.statistics.Statistics;
import com.trade.ticker.TickerModule;

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
    
    public void testTrade() throws IOException, InterruptedException 
    {
    	final TickerModule tickerModule = new TickerModule();
		tickerModule.init();
		
		// 2 JAN 2016 - SATURDAY - SGP (SUN-THU) - Applicable Settlement Date - 3 JAN 2016
		// 7 JAN 2016 - THURSDAY - AED (SUN-THU) - Applicable Settlement Date - 7 JAN 2016
		final String[] tickers= 
			{
					"foo,B,0.50,SGP,01 Jan 2016,02 Jan 2016,200,100.25", 
					"bar,S,0.22,AED,05 Jan 2016,07 Jan 2016,450,150.5"
			};
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
		
		Thread.sleep(3000);
		System.out.println(Statistics.printStatistics());
    }
    
    
}
