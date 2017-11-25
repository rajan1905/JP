package com.trade.source;

import java.io.File;
import java.io.FileNotFoundException;

import com.trade.ticker.TickerModule;

public class RESTClient implements Parser 
{
	File file;
	TickerModule tickerModule;
	
	public RESTClient(File file , TickerModule tickerModule) 
	{
		this.file = file;
		this.tickerModule = tickerModule;
	}
	
	public void parse(File file, TickerModule tickerModule) throws FileNotFoundException 
	{
		// TODO
	}

}
