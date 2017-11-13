package com.trade.inputsource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;


public class Source 
{
	private File file;
	private Parser parser;
	BlockingQueue<String> record;
	
	public Source(File file, BlockingQueue<String> record)
	{
		this.file=file;
		this.record=record;
	}
	
	public void init() throws FileNotFoundException,InterruptedException
	{
		parser.parse(file, record);
	}
}
