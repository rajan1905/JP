package com.trade.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;

public class RESTClient implements Parser 
{
	File file;
	BlockingQueue<String> tradeRecordQueue;
	
	public RESTClient(File file , BlockingQueue<String> tradeRecordQueue) 
	{
		this.file = file;
		this.tradeRecordQueue = tradeRecordQueue;
	}
	
	public void parse(File file, BlockingQueue<String> queue) throws FileNotFoundException, InterruptedException 
	{
		// TODO
	}

}
