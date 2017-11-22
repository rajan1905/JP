package com.trade.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;

import com.trade.enums.SourceType;


public class SourceModule 
{
	private File file;
	private Parser parser;
	BlockingQueue<String> tradeRecordQueue;
	String restSource;
	
	public SourceModule(File file, String restSource, BlockingQueue<String> record)
	{
		this.file = file;
		this.restSource = restSource;
		this.tradeRecordQueue = record;
	}
	
	public void init() throws FileNotFoundException,InterruptedException
	{
		parser = getParser(file);
		parser.parse(file, tradeRecordQueue);
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
			parser = new RESTClient(null , tradeRecordQueue);
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