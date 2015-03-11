package com.sail.cot.util;



public class SystemInfoUtil
{
	
	static { 
		System.loadLibrary("systeminfo");
	}
	
	public native void getUploadPath(String path);
	//获取C盘卷标号
	public native long getVolum();
	//获取MAC地址
	public native String getMacAddr(int index);
	
	public native String getVolumAsString();
	
	public String getVolumSeri(){
		
		String volum = getVolumAsString();
		System.out.println("seri:"+volum);
		if(volum != null && !volum.equals(""))
			volum = volum.trim().toUpperCase();
		else
			return "001CBF5F8A69";
		if(volum.length() < 12)
		{
			int dif = 12 - volum.length();
			for(int i=0; i< dif; i++)
				volum = "0" + volum;
		}
		else 
		{
			volum = volum.substring(volum.length() - 12 );
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<volum.length(); i++)
		{
			char tmp = volum.charAt(i);
			if(tmp > 'F')
				tmp = 'F';
			else if( tmp < 'A' && tmp > '9')
				tmp = 'A';
			else if( tmp < '0')
				tmp = '0';
			sb.append(tmp);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.library.path")); 
		SystemInfoUtil m = new SystemInfoUtil();
        //System.out.println(m.getHello());
        //System.out.println(Long.toHexString(m.getVolum()));
		System.out.println(m.getVolumAsString().trim());
		//m.getVolumAsString().
		String tt = m.getVolumSeri();
		System.out.println("tet"+tt);
    }
}
