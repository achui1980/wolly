/**
 * 
 */
package com.sail.cot.trigger;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sail.cot.service.customer.CotCustomerService;
import com.sail.cot.util.ContextUtil;
import com.sailing.oa.domain.CotStatData;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Nov 19, 2009 2:51:43 PM </p>
 * <p>Class Name: TriggerJob.java </p>
 * @author achui
 *
 */
public class TriggerJob implements Job{

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	private CotCustomerService cotCustomerService;
	public CotCustomerService getCotCustomerService() {
		if (cotCustomerService == null) {
			cotCustomerService = (CotCustomerService) ContextUtil.getBean("CotCustomerService");
		}
		return cotCustomerService;
	}
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		JobDetail jobDetail = jobCtx.getJobDetail(); 
		CotStatData data = this.getCotCustomerService().statData();
		String jobName = jobDetail.getName();  
		int i=0;
		do{
			try {
				this.accessToRemote(data);
				i = 9;//执行成功	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				i++;
				System.out.println("执行失败：进行第"+i+"次尝试");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//e.printStackTrace();
			}
		}while(i<=8);
		System.out.println("触发执行完毕，当前时间"+new Date(System.currentTimeMillis()));
	}
	//远程访问
    public void accessToRemote(CotStatData data) throws Exception{
    	  String addr = ContextUtil.getProperty("remoteaddr.properties","remoteaddr");
		  URL url = new URL(addr); 
		  System.out.println("addr:"+addr);
		  InetAddress aa =  java.net.InetAddress.getByName("xmqh.iplink.com.cn");
		  System.out.println(aa.getHostAddress());
		  URLConnection connection= url.openConnection();
		  HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;
		  httpUrlConnection.setDoOutput(true);
		  httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
		  httpUrlConnection.setRequestMethod("POST");
		  
		  httpUrlConnection.connect();
		  OutputStream outStrm = httpUrlConnection.getOutputStream();
		  ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStrm);
		  objectOutputStream.writeObject(data);
		  objectOutputStream.flush();
		  objectOutputStream.close();
		  System.out.println(httpUrlConnection.getResponseCode());
		  System.out.println("success");  
	  }

}
