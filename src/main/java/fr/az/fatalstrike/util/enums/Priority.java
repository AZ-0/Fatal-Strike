package fr.az.fatalstrike.util.enums;

import java.util.Arrays;

public enum Priority
{
	NONE(Byte.MIN_VALUE),
	
	HIGH((byte) 1),
	MEDIUM((byte) 0),
	LOW((byte) -1),
	;
	
	private byte prio;
	
	private Priority(byte prio)
	{
		this.prio = prio;
	}
	
	public boolean isSuperior(Priority to)
	{
		return prio > to.prio;
	}
	
	public static boolean isSuperior(Priority first, Priority second)
	{
		return first.prio > second.prio;
	}
	
	public boolean isInferior(Priority to)
	{
		return prio < to.prio;
	}
	
	public static boolean isInferior(Priority first, Priority second)
	{
		return first.prio < second.prio;
	}
	
	public boolean isEqual(Priority to)
	{
		return prio == to.prio;
	}
	
	public static boolean isEqual(Priority first, Priority second)
	{
		return first == second;
	}
	
	public static Priority min(Priority... priorities)
	{
		if (Arrays.asList(priorities).isEmpty()) return null;
		
		Priority min = priorities[0];
		
		for (Priority prio : priorities)
		{
			if (prio.prio < min.prio) min = prio;
		}
		
		return min;
	}
	
	public static Priority max(Priority... priorities)
	{
		if (Arrays.asList(priorities).isEmpty()) return null;
		
		Priority max = priorities[0];
		
		for (Priority prio : priorities)
		{
			if (prio.prio > max.prio) max = prio;
		}
		
		return max;
	}
}
