package com.trade.inputsource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;

public interface Parser 
{
	public void parse(File file, BlockingQueue<String> queue) throws FileNotFoundException,InterruptedException;
}
