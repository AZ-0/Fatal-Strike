package fr.az.fatalstrike.util;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class Tuple2<A,B>
{
	public A a;
	public B b;
	
	public Tuple2() { this(null, null); }
	public Tuple2(A a, B b) { this.a = a; this.b = b; }
	
	public A a() { return a; }
	public B b() { return b; }
	
	@SafeVarargs
	public final <T> List<T> toList(T... inferator) { return Arrays.asList((T) a, (T) b); }
	public <T> T[] toArray(T... inferator) { return this.toList().toArray(inferator); }
	
	public String toString() { return "Tuple" + this.toList(); }
}
