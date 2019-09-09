package com.itsync.displayobject;

public class Counter {
	
	private static Counter counter;
	public int count;
	
	private Counter(){
		count = 0;
	}
	
	public static Counter GetInstance(){
		
		if (counter == null) {
			synchronized (Counter.class) {
				if (counter == null) {
					counter = new Counter();
				}
			}
		}
		
		return counter;
	}

}
