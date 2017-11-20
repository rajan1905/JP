package com.trade.inputsource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;

import com.trade.enums.FileType;


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
		parser = getParser(file);
		parser.parse(file, record);
	}
	
	private FileType getFileType(File file)
	{
		FileType fileType=null;
		
		if(file != null)
		{
			String fileName=file.getName();
			int dotIndex=fileName.lastIndexOf('.');
			String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex+1);
			extension=extension.toLowerCase();
			
			if(extension.equals("txt"))
			{
				fileType = FileType.TXT;
			}
			else if(extension.equals("csv"))
			{
				fileType = FileType.CSV;
			}
			else
			{
				fileType = FileType.UNKNOWN;
			}
		}
		
		return fileType;
	}
	private Parser getParser(File file)
	{
		Parser parser;
		
		switch(getFileType(file))
		{
		case TXT:
			parser=null;
			break;
		case CSV:
			parser=new CSVParser();
			break;
		default: parser=null;
		}
		
		return parser;
	}
}
