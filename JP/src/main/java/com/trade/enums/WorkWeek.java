package com.trade.enums;

import java.util.HashMap;
import java.util.Map;

public enum WorkWeek 
{
	AED(Week.SUNDAY,Week.THURSDAY),
	SAR(Week.SUNDAY,Week.THURSDAY),
	DEFAULT(Week.MONDAY,Week.FRIDAY);
	
	private short start,end;
	
	private enum Week
	{
		SUNDAY,MONDAY,TUESDAY,WEDNESDAY,
		THURSDAY,FRIDAY,SATURDAY
	}
	
	private static Map<String,WorkWeek> map=null;
	public final static short WORKING_DAY_FACTOR=1;
	
	private WorkWeek(Week startWeek, Week endWeek)
	{
		start=(short) startWeek.ordinal();
		end=(short) endWeek.ordinal();
	}
	
	private static void generateMapping()
	{
		if(map==null)
		{
			map=new HashMap<String, WorkWeek>();
			
			for(WorkWeek workWeek:values())
			{
				map.put(workWeek.name(), workWeek);
			}
		}
	}
	
	public static Map<String,WorkWeek> getMap()
	{
		if(map==null)
			generateMapping();
		
		return map;
	}
	
	public short getStart()
	{
		return start;
	}
	
	public short getEnd()
	{
		return end;
	}
}
