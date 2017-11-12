package com.trade.ticker;

import java.util.Calendar;;

public class Ticker 
{
	private String entity;
	private char action;
	private float agreedFx;
	private String currency;
	private Calendar instructionDate;
	private Calendar settlementDate;
	private int units;
	private float pricePerUnit;
	
	// Suppresses default constructor, ensuring non-instantiability
	private Ticker(TickerBuilder tickerBuilder)
	{
		entity=tickerBuilder.entity;
		action=tickerBuilder.action;
		agreedFx=tickerBuilder.agreedFx;
		currency=tickerBuilder.currency;
		instructionDate=tickerBuilder.instructionDate;
		settlementDate=tickerBuilder.settlementDate;
		units=tickerBuilder.units;
		pricePerUnit=tickerBuilder.pricePerUnit;		
	}
	
	public static TickerBuilder builder()
	{
		return new TickerBuilder();
	}
	
	static class TickerBuilder
	{
		private String entity;
		private char action;
		private float agreedFx;
		private String currency;
		private Calendar instructionDate;
		private Calendar settlementDate;
		private int units;
		private float pricePerUnit;
		
		public TickerBuilder entity(String entity)
		{
			this.entity=entity;
			return this;
		}
		
		public TickerBuilder action(char action)
		{
			this.action=action;
			return this;
		}
		
		public TickerBuilder agreedFx(float agreedFx)
		{
			this.agreedFx=agreedFx;
			return this;
		}
		
		public TickerBuilder currency(String currency)
		{
			this.currency=currency;
			return this;
		}
		
		public TickerBuilder instructionDate(Calendar instructionDate)
		{
			this.instructionDate=instructionDate;
			return this;
		}
		
		public TickerBuilder settlementDate(Calendar settlementDate)
		{
			this.settlementDate=settlementDate;
			return this;
		}
		
		public TickerBuilder units(int units)
		{
			this.units=units;
			return this;
		}
		
		public TickerBuilder pricePerUnit(float pricePerUnit)
		{
			this.pricePerUnit=pricePerUnit;
			return this;
		}
		
		public Ticker build()
		{
			return new Ticker(this);
		}
	}
	
	public String getEntity() 
	{
		return entity;
	}

	public char getAction() 
	{
		return action;
	}

	public float getAgreedFx() 
	{
		return agreedFx;
	}

	public String getCurrency() 
	{
		return currency;
	}

	public Calendar getInstructionDate() 
	{
		return instructionDate;
	}

	public Calendar getSettlementDate() 
	{
		return settlementDate;
	}
	
	public void setSettlementDate(Calendar settlementDate)
	{
		this.settlementDate=settlementDate;
	}

	public int getUnits() 
	{
		return units;
	}

	public float getPricePerUnit() 
	{
		return pricePerUnit;
	}
}
