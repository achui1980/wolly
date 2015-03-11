/**
 * 
 */
package com.sail.cot.util;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRDefaultScriptlet;

import org.apache.commons.lang.RandomStringUtils;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: Apr 10, 2009 12:03:01 PM </p>
 * <p>Class Name: RMB.java </p>
 * @author achui
 *
 */
public class RMB extends JRDefaultScriptlet {
	private static String[] place = {""," Thousand "," Million "," Billion "," Trillion "};
	/**
	 * 描述： 将数字改成人民币大写
	 * @param strVal  输入的值 如：125.76
	 * @return
	 * 返回值：String 返回人民币大写
	 */
	public   static  String changeToBig( String strVal){
		if(strVal == null || "null".equals(strVal) || "".equals(strVal))
			strVal = "0";
		if(strVal.equals("0")) 
			return "零圆整";
		double  value = Double.parseDouble(strVal);
		if(value == 0)
			return "零圆整";
        char [] hunit = { '拾' , '佰' , '仟' };                             // 段内位置表示 
        char [] vunit = { '万' , '亿' };                                        // 段名表示 
        char [] digit = { '零' , '壹' , '贰' , '叁' , '肆' , '伍' , '陆' , '柒' , '捌' , '玖' };   // 数字表示 
        long  midVal  =  ( long )(value * 100 );                                // 转化成整形 
       String valStr = String.valueOf(midVal);                           // 转化成字符串 
       String head = valStr.substring( 0 ,valStr.length() - 2 );              // 取整数部分 
       String rail = valStr.substring(valStr.length() - 2 );                // 取小数部分 
       String prefix = "" ;                                               // 整数部分转化的结果 
       String suffix = "" ;                                               // 小数部分转化的结果
        // 处理小数点后面的数 
        if (rail.equals( "00" )){                                          // 如果小数部分为0 
         suffix = "整" ;
       }  else {
         suffix = digit[rail.charAt( 0 ) - '0' ] + "角" + digit[rail.charAt( 1 ) - '0' ] + "分" ;  // 否则把角分转化出来 
       }
        // 处理小数点前面的数 
        char [] chDig = head.toCharArray();                  // 把整数部分转化成字符数组 
        char  zero = '0' ;                                    // 标志'0'表示出现过0 
        byte  zeroSerNum  =   0 ;                              // 连续出现0的次数 
        for ( int  i = 0 ;i < chDig.length;i ++ ){                  // 循环处理每个数字 
          int  idx = (chDig.length - i - 1 ) % 4 ;                   // 取段内位置 
          int  vidx = (chDig.length - i - 1 ) / 4 ;                  // 取段位置 
          if (chDig[i] == '0' ){                              // 如果当前字符是0 
           zeroSerNum ++ ;                                 // 连续0次数递增 
            if (zero  ==   '0' ){                               // 标志 
             zero = digit[ 0 ];
           }  else   if (idx == 0   &&  vidx  > 0   && zeroSerNum  <   4 ){
             prefix  +=  vunit[vidx - 1 ];
             zero = '0' ;
           }
            continue ;
         }
         zeroSerNum  =   0 ;                                 // 连续0次数清零 
          if (zero  !=   '0' ) {                               // 如果标志不为0,则加上,例如万,亿什么的 
           prefix += zero;
           zero = '0' ;
         }
         prefix += digit[chDig[i] - '0' ];                   // 转化该数字表示 
          if (idx  >   0 ) prefix  +=  hunit[idx - 1 ];                  
          if (idx == 0   &&  vidx > 0 ){
           prefix += vunit[vidx - 1 ];                      // 段结束位置应该加上段名如万,亿 
         }
       }

        if (prefix.length()  >   0 ) prefix  +=   '圆' ;         // 如果整数部分存在,则有圆的字样 
        return  prefix + suffix;                          // 返回正确表示 
     } 
	
	/**
	 * 描述：   在给定时间的基础上，加或减掉几年，几个月，几天
	 * @param tp   		给定的需要设置的基础时间
	 * @param year		需要加或减的年数，正数为加year年，负数为减year年 
	 * @param month		需要加或减的月数，正数为加month个月，负数为减month个月
	 * @param date		需要加或减的天数，正数为加date天，负数为减date天
	 * @return
	 * 返回值：Timestamp  返回经过操作后的时间
	 */
	public   static  Timestamp addDate(Timestamp tp,int year,int month,int date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(tp.getTime());
		int y = calendar.get(Calendar.YEAR);
		int m = calendar.get(Calendar.MONTH);
		int d = calendar.get(Calendar.DATE);
		calendar.set(Calendar.YEAR, y + year);
		calendar.set(Calendar.MONTH, m + month);
		calendar.set(Calendar.DATE, d + date);
		Timestamp newTp = new Timestamp(
				calendar.getTimeInMillis());
		return newTp;
	}
	
	/**
	 * 描述：
	 * @param pattern 需要格式化的时间串 如　yyyy-MM-dd;yyyy-MM-dd HH:mm:ss
	 * @param tp 需要格式化的时间
	 * @return
	 * 返回值：String
	 */
	public static String getFormateDate(String pattern,Timestamp tp)
	{
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		Date date = new Date(tp.getTime());
		String dataStr = sf.format(date);
		return dataStr;
	}
	
	//
	/**
	 * 描述： 数字转化为英文金额的大写形式
	 * @param val 需要转换的金额
	 * @return
	 * 返回值：String 英文金额的大写形式
	 */
	public static String convertToEnglish(String val)
	{
		if(val == null || "null".equals(val) || "".equals(val))
			return "";
		String Dollars=""; 
		String Cents="";
		String Temp = "";
	    int DecimalPlace = 0;//小数点位置
		int Count = 0;
	   
	    //String representation of amount.
	    val = val.trim();
	    //Position of decimal place 0 if none.
	    DecimalPlace = val.indexOf(".");
	    //Convert cents and set MyNumber to dollar amount.
	    if (DecimalPlace > 0) {
	    	String tens = val.substring(DecimalPlace + 1) + "00";
	        Cents = getTens(tens.substring(0,2));
	        val = val.substring(0,DecimalPlace).trim();
	    }
	    Count = 0;
	    do{
	    	if(val.length() > 3)
	    		Temp = getHundreds(val.substring(val.length()-3),Count);
	    	else
	    		Temp = getHundreds(val,Count) ;
	    	if(!"".equals(Temp))
	    	{
	    		Dollars = Temp + place[Count] + Dollars;
	    	}
	    	if(val.length() > 3)
	    	{
	    		val = val.substring(0,val.length()-3);
	    	}
	    	else
	    		val = "";
	    	Count = Count+1;
	    	
	    }while (!"".equals(val));
	    
	    if("".equals(Dollars))
	    	Dollars = "No Dollars";
	    else if("One".equals(Dollars))
	    	Dollars = "One";
	    else
	    	Dollars = Dollars ;
	    Dollars = Dollars.trim();
	    if(Dollars.startsWith("and"))
	    {
	    	Dollars = Dollars.substring(3);
	    }
	    if(Dollars.endsWith("and"))
	    {
	    	Dollars = Dollars.substring(0,Dollars.length()-3);
	    }
	   
	    if("".equals(Cents))
	    	Cents = " ";
	    else if("One".equals(Cents))
	    	Cents = " and One Cent";
	    else
	    	Cents = " and " + Cents + " Cents";
	    
	    return  (Dollars + Cents).trim();

	}
	//Converts a number from 100-999 into text 
	public static String getHundreds(String val,int count)
	{
		String result ="";
		if(Integer.parseInt(val) == 0) return "";
		val = "000"+val;
		val = val.substring(val.length()-3);
	    
	    //转换百位数.
		if(!"0".equals(val.substring(0,1)))
		{
			result = getDigit(val.substring(0,1))+ " Hundred " ;
		}
		//转换十位数和各位数
		if(!"0".equals(val.substring(1,1)))
			if(count != 0) result = result + getTens(val.substring(1));
			else result = result +"and "+ getTens(val.substring(1));
		else
			if(count != 0) result = result + getDigit(val.substring(2));
			else result = result + "and " + getDigit(val.substring(2));
		
		
	    return result;

	}
	public static String getTens(String val)
	{
		String result = "";
		//如果值在10-19之间
		int ival = Integer.parseInt(val.substring(0,1));
		int iival = Integer.parseInt(val);
	    if(ival == 1)
	    {
	        switch (iival)
	        {
	        	case 10: result = "Ten ";break;
	        	case 11: result = "Eleven ";break;
	        	case 12: result = "Twelve ";break;
	        	case 13: result = "Thirteen ";break;
	        	case 14: result = "Fourteen ";break;
	        	case 15: result = "Fifteen ";break;
	        	case 16: result = "Sixteen ";break;
	        	case 17: result = "Seventeen ";break;
	        	case 18: result = "Eighteen ";break;
	        	case 19: result = "Nineteen ";break;
	        }
	    }
	    else // If value between 20-99...
	    {
	       switch (ival)
	       {
	       		case 2: result = "Twenty ";break;
	       		case 3: result = "Thirty ";break;
	       		case 4: result = "Forty ";break;
	       		case 5: result = "Fifty ";break;
	       		case 6: result = "Sixty ";break;
	       		case 7: result = "Seventy ";break;
	       		case 8: result = "Eighty ";break;
	       		case 9: result = "Ninety ";break;
	            
	       }
	       String digit =  getDigit(val.substring(val.length()-1));
	       //if(!"".equals(digit))
	    	   //result = result + "-" + digit;//拼接各位数.
	      result = result + digit;//拼接各位数.
	    }
	    
	    return result;
	}
	public static String getDigit(String val)
	{
		int ival = Integer.parseInt(val);
		String res = "";
		switch (ival)
		{
			case 1: 
				res = "One";break;
			case 2: 
				res = "Two";break;
			case 3: 
				res = "Three";break;
			case 4: 
				res = "Four";break;
			case 5: 
				res = "Five";break;
			case 6: 
				res = "Six";break;
			case 7: 
				res = "Seven";break;
			case 8: 
				res = "Eight";break;
			case 9: 
				res = "Nine";break;
	        default:
	        	res = "";
		}
		return res;
	}
	/**
	 * 描述：  转换说输入字段的精度
	 * @param num 		需要进行转换的数字
	 * @param precision 需要保留的小数位数（如果该值为空或是负数，默认保留2位小数）
	 * @return
	 * 返回值：String 转换后的数字（会四舍五入）
	 */
	public static String getPrecision(Object num,int precision)
	{
		if(num == null || precision < 0 )
			return "0";
		double tmp = Double.parseDouble(num.toString());
		String formater = "#,##0.";
		for(int i=1; i<=precision ; i++)
		{
			formater += "0";
		}
		if(precision == 0)
			formater = "#,##0";
		if("#,##0.".equals(formater))
			formater = "#,##0.00";//默认2为小数
		DecimalFormat   format = new DecimalFormat(formater);
		String res = format.format(tmp);
		return res;
	}
	
	//8位随机整数+空格+生产价(***+空格+**)+固定数字2
	public static String getFormatPriceFac(String priceFac){
		StringBuffer sf = new StringBuffer();
		String rdm = RandomStringUtils.randomNumeric(8);
		float dobFac =  Float.parseFloat(priceFac);
		DecimalFormat format = new DecimalFormat("000.00");
		String temp = format.format(dobFac);
		temp = temp.replace(".", " ");
		sf.append(rdm);
		sf.append(" ");
		sf.append(temp);
		sf.append(" 2");
		return sf.toString();
	}
	
	//把价格的小数点变成"8",小数位保留2位,不足补0
	public static String formatPrice(Object price){
		if(price == null)
			return "0";
		DecimalFormat format = new DecimalFormat("0.00");
		String temp = format.format(Double.parseDouble(price.toString()));
		if(temp.indexOf(".")>0){
			temp=temp.replace(".", "8");
		}
		return temp;
	}
	
	//日期转化 2009-09-01 转成特定格式
	public static String changeDate(Object tp,String format){
		if(tp == null) return "";
		Date date = (Date)tp;
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(format,java.util.Locale.ENGLISH);
		String str = sdf.format(date);
		return str;
	}
	
	//返回两日期的天数
	public static int daysOfTwo(Timestamp fDate, Timestamp oDate) {
		if (null == fDate || null == oDate) {
           return -1;
       }
       long intervalMilli = oDate.getTime() - fDate.getTime();
       return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }

	/**
	 * 描述：  转换所输入字段的精度,将其格式化后，输出长度为length的字符串,不足位数的补零
	 * @param num 		需要进行转换的数字
	 * @param precision 需要保留的小数位数（如果该值为空或是负数，默认保留2位小数）
	 * @param length	输出字符串的长度
	 * @return
	 * 返回值：String 转换后的数字（会四舍五入），length的字符串,不足位数的补零
	 */
	public  static String formatString(Object num,int precision,int length)
	{
		double i = Math.pow(10, precision);//获取倍数
		String res = getPrecision(num,precision);
		BigDecimal bigDecimal = new BigDecimal(res);
		bigDecimal = bigDecimal.multiply(new BigDecimal(i));
		//Double tmpDouble = Double.parseDouble(res)*i;
		//Integer result = new Integer(tmpDouble.intValue());	
		Integer result = new Integer(bigDecimal.intValue());
		String par = "%1$"+(length>=10?length:("0"+length))+"d";
		res = String.format(par, result);
		return res;
	}
	/**
	 * 描述： 按要求格式化字符串
	 * 如：9.45 要表示成 9 1/2 ，1/2如果能竖着显示最好了。
	 * 小数点是 0.01~0.25的全部显示成 1/4 ，
	 * 0.25~0.5 全部显示成 1/2，
	 * 0.5~0.75全部显示成 3/4，
	 * 0.75~1直接四舍五入成整数
	 * @param eleObj 需要格式化的数据
	 * @return
	 * 返回值：String
	 */
	public static String formatEleString(Object eleObj) {
		if (eleObj == null) return "";
		Double db = Double.parseDouble(eleObj.toString());
		double mindb = Math.floor(db);
		double floatPoint = db.doubleValue() - mindb;
		DecimalFormat format = new DecimalFormat("###");
		String point = "";
		if(floatPoint <= 0.25)
			point = "1/4";
		else if(floatPoint >0.25 && floatPoint <=0.5)
			point = "1/2";
		else if (floatPoint >0.5 && floatPoint <=0.75)
			point = "3/4";
		else if(floatPoint >0.75)
			mindb = mindb +1;
		String resString = format.format(mindb) + " " + point;
		if(mindb == 0)
			return point;
		return resString;
	}
	
	//截取报价明细中的包装类型里面的英文名
	public static String getEnNameFromBoxType(String boxType){
		if(boxType!=null && !"".equals(boxType)){
			int temp = boxType.lastIndexOf("(");
			String str=boxType.substring(temp+1,boxType.length()-1);
			return str;
		}else{
			return "";
		}
	}

	//将String字符串里面的值转成String
	//参数String的格式为 XXX##XXX##XXX
	public static String convertStringToString(String res)
	{
		if(res == null) return null;
		String strRes = "";   
		String[] arr = res.split("##");
		Map<String, String> map = new HashMap<String, String>();
		for(String str : arr)
		{
			map.put(str, null);
		}
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext())
		{
			String key = iterator.next();
			//String value = (String)map.get(key);
			strRes += key+" ";
		}
		return strRes;
	}

	
	//格式化生产价 例如4.9--->Bg004m90LP
	public static String getFtPriceFac(Object priceFac){
		String oneRdm = RandomStringUtils.randomAlphabetic(1);
		float dobFac =  0f;
		if(priceFac!=null){
			dobFac=((BigDecimal)priceFac).floatValue();
		}
		DecimalFormat format = new DecimalFormat("000.00");
		String temp = format.format(dobFac);
		temp = temp.replace(".", oneRdm);
		String twoRdm = RandomStringUtils.randomAlphabetic(2);
		String twoRdm2 = RandomStringUtils.randomAlphabetic(2);
		return twoRdm+temp+twoRdm2;
	}
	
	//小林的特殊客户的特殊需求
	public static String convertToEnglish2(String val)
	{
		if(val == null || "null".equals(val) || "".equals(val))
			return "";
		String Dollars=""; 
		String Cents="";
		String Temp = "";
	    int DecimalPlace = 0;//小数点位置
		int Count = 0;
	   
	    //String representation of amount.
	    val = val.trim();
	    //Position of decimal place 0 if none.
	    DecimalPlace = val.indexOf(".");
	    //Convert cents and set MyNumber to dollar amount.
	    if (DecimalPlace > 0) {
	    	String tens = val.substring(DecimalPlace + 1) + "00";
	        Cents = getTens(tens.substring(0,2));
	        val = val.substring(0,DecimalPlace).trim();
	    }
	    Count = 0;
	    do{
	    	if(val.length() > 3)
	    		Temp = getHundreds(val.substring(val.length()-3),Count);
	    	else
	    		Temp = getHundreds(val,Count) ;
	    	if(!"".equals(Temp))
	    	{
	    		Dollars = Temp + place[Count] + Dollars;
	    	}
	    	if(val.length() > 3)
	    	{
	    		val = val.substring(0,val.length()-3);
	    	}
	    	else
	    		val = "";
	    	Count = Count+1;
	    	
	    }while (!"".equals(val));
	    
	    if("".equals(Dollars))
	    	Dollars = "No Dollars";
	    else if("One".equals(Dollars))
	    	Dollars = "One";
	    else
	    	Dollars = Dollars ;
	    Dollars = Dollars.trim();
	    if(Dollars.startsWith("and"))
	    {
	    	Dollars = Dollars.substring(3);
	    }
	    if(Dollars.endsWith("and"))
	    {
	    	Dollars = Dollars.substring(0,Dollars.length()-3);
	    }
	   
	    if("".equals(Cents))
	    	Cents = " ";
	    else if("One".equals(Cents))
	    	Cents = " and Cent One";
	    else
	    	Cents = " and Cents " + Cents;
	    
	    return  (Dollars +" Dollars"+ Cents).trim();

	}
	
	//小林的另个特殊客户的特殊需求
	public static String convertToEnglish3(String val)
	{
		if(val == null || "null".equals(val) || "".equals(val))
			return "";
		String Dollars=""; 
		String Cents="";
		String Temp = "";
	    int DecimalPlace = 0;//小数点位置
		int Count = 0;
	   
	    //String representation of amount.
	    val = val.trim();
	    //Position of decimal place 0 if none.
	    DecimalPlace = val.indexOf(".");
	    //Convert cents and set MyNumber to dollar amount.
	    if (DecimalPlace > 0) {
	    	String tens = val.substring(DecimalPlace + 1) + "00";
	        Cents = getTens(tens.substring(0,2));
	        val = val.substring(0,DecimalPlace).trim();
	    }
	    Count = 0;
	    do{
	    	if(val.length() > 3)
	    		Temp = getHundreds(val.substring(val.length()-3),Count);
	    	else
	    		Temp = getHundreds(val,Count) ;
	    	if(!"".equals(Temp))
	    	{
	    		Dollars = Temp + place[Count] + Dollars;
	    	}
	    	if(val.length() > 3)
	    	{
	    		val = val.substring(0,val.length()-3);
	    	}
	    	else
	    		val = "";
	    	Count = Count+1;
	    	
	    }while (!"".equals(val));
	    
	    if("One".equals(Dollars))
	    	Dollars = "One";
	    else
	    	Dollars = Dollars ;
	    Dollars = Dollars.trim();
	    if(Dollars.startsWith("and"))
	    {
	    	Dollars = Dollars.substring(3);
	    }
	    if(Dollars.endsWith("and"))
	    {
	    	Dollars = Dollars.substring(0,Dollars.length()-3);
	    }
	   
	    if("".equals(Cents))
	    	Cents = " ";
	    else if("One".equals(Cents))
	    	Cents = " & Cent One";
	    else
	    	Cents = " & Cents " + Cents;
	    
	    if(!Dollars.equals("")){
	    	 return  ("Dollars "+Dollars + Cents).trim();
	    }else{
	    	return  Cents.trim().substring(2);
	    }
	    

	}
	

	public static void main(String[] args)
	{
//		HttpURLConnection connection =  null;
//		try{
//		URL url = new URL("http://192.168.1.12:8080/CotSystem");
//		connection = (HttpURLConnection)url.openConnection();
//		System.out.println(connection.getResponseCode());
//			if(connection.getResponseCode() == 200)
//				System.out.println("链路连通！");
//			else {
//				System.out.println(connection.getResponseCode());
//				System.out.println("链路不连通！");
//			}
//		}
//		catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("链路不连通！");
//		}
//
//		BigDecimal aaa = new BigDecimal(1.6434543);
//		String test = RMB.getPrecision(aaa,4);
//		System.out.println(test);
//		try {
//			Code39Barcode code39Barcode =  new Code39Barcode("+-/.$ % ",true);
//			System.out.println("success");
//			//code39Barcode.
//		} catch (BarcodeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Date tp = new Date(System.currentTimeMillis());
//		
//		BigDecimal aaa = new BigDecimal(2384.789);
////		String aa = RMB.changeDate(tp, "MM/dd/yyyy");
//		String temp = new DecimalFormat("0.00").format(aaa);
//		System.out.println(temp);
		String temp=convertToEnglish3("106408.4");
		System.err.println(temp);
	}
	
}
