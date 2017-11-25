package com.trade.source;

import java.io.File;
import java.io.FileNotFoundException;

import com.trade.ticker.TickerModule;

public interface Parser 
{
	public void parse(File file, TickerModule tickerModule) throws FileNotFoundException;
}
