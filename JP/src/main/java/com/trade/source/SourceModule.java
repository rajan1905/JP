package com.trade.source;

import java.io.File;
import java.io.FileNotFoundException;

import com.trade.enums.SourceType;
import com.trade.ticker.TickerModule;

public class SourceModule 
{
	private File file;
	private Parser parser;
	TickerModule tickerModule;
	String restSource;
	
	public SourceModule(File file, String restSource, TickerModule tickerModule)
	{
		this.file = file;
		this.restSource = restSource;
		this.tickerModule = tickerModule;
	}
	
	public void init() throws FileNotFoundException,InterruptedException
	{
		parser = getParser(file);
		parser.parse(file, tickerModule);
	}
	
	private SourceType getFileType(File file)
	{
		SourceType fileType=null;
		
		if(file != null)
		{
			String fileName = file.getName();
			int dotIndex = fileName.lastIndexOf('.');
			String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex+1);
			extension = extension.toLowerCase();
			
			if(extension.equals("txt"))
			{
				fileType = SourceType.TXT;
			}
			else if(extension.equals("csv"))
			{
				fileType = SourceType.CSV;
			}
			else
			{
				fileType = SourceType.UNKNOWN;
			}
		}
		
		return fileType;
	}
	private Parser getParser(File file)
	{
		Parser parser;
		
		// Is source a REST/RSS 
		if(file == null && restSource != null)
		{
			parser = new RESTClient(null , tickerModule);
		}
		else
		{
			switch(getFileType(file))
			{
			case TXT:
				parser = null;
				break;
			case CSV:
				parser = new CSVParser();
				break;
			default: parser = null;
			}
		}
		
		return parser;
	}
}