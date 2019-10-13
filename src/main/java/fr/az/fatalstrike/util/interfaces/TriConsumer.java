package fr.az.fatalstrike.util.interfaces;

/**
 * Represents an operation that accepts two input arguments and returns no result.
 * This is the tree-arity specialization of Consumer.
 * Unlike most other functional interfaces, TriConsumer is expected to operate via side-effects. 
 * This is a functional interface whose functional method is accept(Object, Object, Object).
 * 
 * @since Fatal Strike 1.0
 * @see java.util.function.Consumer Consumer{@literal <T>}
 * @see java.util.function.BiConsumer BiConsumer{@literal <T,U>}
 */
@FunctionalInterface
public interface TriConsumer<T,U,V>
{
	public void accept(T t, U u, V v);
}
