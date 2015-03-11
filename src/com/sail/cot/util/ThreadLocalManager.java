/**
 * @(#) ThreadLocalManager.java 2007-7-21
 * 
 * Copyright 2006 ptnetwork
 */
package com.sail.cot.util;

public class ThreadLocalManager {

	private ThreadLocalManager() {
		
	}
	
	private static final ThreadLocal currentThread = new ThreadLocal();

	public static ThreadLocal getCurrentThread() {
		return currentThread;
	}
}
