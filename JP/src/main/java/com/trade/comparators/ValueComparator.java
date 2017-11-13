package com.trade.comparators;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<String> 
{
	Map<String, Float> base;
	
	public ValueComparator(Map<String, Float> base) 
	{
		this.base=base;
	}
	
	public int compare(String o1, String o2) 
	{
		if(base.get(o1) >= base.get(o2))
			return -1;
		else
			return 1;
	}

}
