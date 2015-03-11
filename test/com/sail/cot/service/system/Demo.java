package com.sail.cot.service.system;
import ft.otp.verify.OTPVerify;
import java.util.Scanner;
import java.util.Map;

/********************************************************************
  
  	RFC4226 JAVA SAMPLE RESULT:
    Secret = 0x3132333435363738393031323334353637383930

    Count    HOTP
    0        755224
    1        287082
    2        359152
    3        969429
    4        338314
    5        254676
    6        287922
    7        162583
    8        399871
    9        520489

    /////////////////////////////////////////////////

TOTP JAVA SAMPLE RESULT:
    Secret = 0x3132333435363738393031323334353637383930
    T0 = UTC EPOCH
    X = 30

    +------------+-----------------------+------------------+--------+
    | Unix Time  |       UTC Time        |    Value of T    |  OTP   |
    +------------+-----------------------+------------------+--------+
    | 1000000000 |  2001-09-09 01:46:40  | 0000000001FCA055 | 864010 |
    +------------+-----------------------+------------------+--------+
    | 1111111111 |  2005-03-18 01:58:31  | 00000000023523ED | 050471 |
    +------------+-----------------------+------------------+--------+
    | 1234567890 |  2009-02-13 23:31:30  | 000000000273EF07 | 005924 |
    +------------+-----------------------+------------------+--------+
    | 2000000000 |  2033-05-18 03:33:20  | 0000000003F940AA | 279037 |
    +------------+-----------------------+------------------+--------+
    
    
    
#define OTP_SUCCESS					(0x00000000L) //操作成功

#define OTP_ERR_INVALID_PARAMETER	(0x00000001L) //参数无效

#define OTP_ERR_CHECK_PWD			(0x00000002L) //认证失败

#define OTP_ERR_SYN_PWD				(0x00000003L) //同步失败

#define OTP_ERR_REPLAY				(0x000000004) //同一个动态口令被多次重试


*******************************************************************/

public class Demo {

	private Demo() {}

	
	//ET z201验证接口调用
	private static void testCheckPwdz201()
	{
		String seed = "ll8E1FD8B73FBD8E8EBC8B2C16DF9772FCFB17E56025B167CD";
		String sOTP = "";
		Map hashMap;
		Scanner sc = new Scanner(System.in);
		int iDrift = 0;
		long lSucc = 0;
		
		Long nReturn;
		
		System.out.println("demo ET_CheckPwdz201,input:\"quit\" to end");
		
		System.out.print("please input otp:");
        while(!((sOTP = sc.next()).equals("quit")))
        {
        	long l3 = System.currentTimeMillis()/1000+(-25)*60;
        	System.out.println(l3);
        	//System.out.println(sOTP);
			hashMap = OTPVerify.ET_CheckPwdz201(
				seed,									//令牌密钥
				System.currentTimeMillis()/1000,		//调用本接口计算机的当前时间
				0,										//给0
				60,										//给60，因为每60秒变更新的动态口令
				iDrift, 								//漂移值，用于调整硬件与服务器的时间偏差，见手册说明
				20,										//认证窗口，见手册说明 
				lSucc,									//成功值，用于调整硬件与服务器的时间偏差，见手册说明
				sOTP);									//要认证的动态口令OTP
			
			
			nReturn = (Long)hashMap.get("returnCode");
			System.out.println("returnCode = " + nReturn);
			
			if(nReturn == OTPVerify.OTP_SUCCESS)
			{
				System.out.println("check success");
				System.out.println("currentSucc = " + hashMap.get("currentUTCEpoch"));
				System.out.println("currentDrift = " + hashMap.get("currentDrift"));
				
				iDrift = ((Long)hashMap.get("currentDrift")).intValue();
				lSucc = ((Long)hashMap.get("currentUTCEpoch")).longValue();
				
			}
			else
			{
				System.out.println("check fail");
			}
			
			System.out.print("please input otp:");
        }		  
			
	}

	//ET z201同步接口调用
	private static void testSyncPwdz201()
	{
		String seed = "ll8E1FD8B73FBD8E8EBC8B2C16DF9772FCFB17E56025B167CD";
		String sOTP1 = "";
		String sOTP2 = "";
		Map hashMap;
		Scanner sc = new Scanner(System.in);
		int iDrift = 0;
		long lSucc = 0;
		Long nReturn;
		
		System.out.println("demo ET_Syncz201,input:\"quit\" to end");
		
	
	            
	    System.out.print("please input otp1:");
        while(!((sOTP1 = sc.next()).equals("quit")))
        {
        	System.out.print("please input otp2:");
        	sOTP2 = sc.next();
        	
        	
        	hashMap = OTPVerify.ET_Syncz201(seed, System.currentTimeMillis()/1000, 0, 60, iDrift, 120, lSucc, 
	            sOTP1,sOTP2);
	            
	        nReturn = (Long)hashMap.get("returnCode");
			System.out.println("returnCode = " + nReturn); 
	            
        	if(nReturn == OTPVerify.OTP_SUCCESS)
			{
				System.out.println("syn success");
				System.out.println("currentSucc = " + hashMap.get("currentUTCEpoch"));
				System.out.println("currentDrift = " + hashMap.get("currentDrift"));
				
				iDrift = ((Long)hashMap.get("currentDrift")).intValue();
				lSucc = ((Long)hashMap.get("currentUTCEpoch")).longValue();
				
			}
			else
			{
				System.out.println("syn fail");
			}
			
			System.out.print("please input otp1:");
        }
		
	}
	
    public static void main(String[] args) 
    {
    	//ET z201
//    	testCheckPwdz201();
    	
    	testSyncPwdz201();
    }
}

