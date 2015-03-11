///**
// * 
// */
//package com.sail.cot.mail.service.impl;
//
///**
// * <p>Title: 旗行办公自动化系统（OA）</p>
// * <p>Description:</p>
// * <p>Copyright: Copyright (c) 2008</p>
// * <p>Company: </p>
// * <p>Create Time: May 11, 2009 8:59:50 AM </p>
// * <p>Class Name: ReciveMailThread.java </p>
// * @author achui
// *
// */
//public class ReciveMailThread extends Thread {
//	
//	public void run()
//	{
//		ReciveMailServiceImpl recv = new ReciveMailServiceImpl();
//		try {
//			//recv.openFolder("smtp.163.com", "pop3.163.com", "achui_1980", "344135");
//			recv.saveReciveMail("smtp.163.com", "pop3.163.com", "achui_1980", "344135", null,null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public  static void main(String[] args)
//	{
//		Thread thread = new Thread(new ReciveMailThread());
//		try
//		{
//		
//		System.out.println(thread.getState());
//		thread.start();
//		System.out.println(thread.isAlive());
//		}
//		catch(Exception ex){}
//		finally{System.out.println(thread.isAlive());}
//		
//		
//		
//	}
//}
