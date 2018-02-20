package main.core;

public interface Listner {

	public void process(String to,String from);
	public void reportError(String from,Exception e,boolean add);

}
