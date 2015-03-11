package com.sail.cot.email.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
/**
 * 下载文件，根据相应文件类型返回相应输出信息
 * @author zhao
 *
 */
public class ContentTypeUtil {
	private static Map<String,String> typeMap = new HashMap<String, String>();		// 浏览器不能直接打开
	private static Map<String,String> typeOpenMap = new HashMap<String, String>(); // 浏览器可直接打开
	static {
//		* Documents (*.doc, *.docx, *.rtf, *.odt, *.sxw)
//	    * Spreadsheets (*.xls, *.xlsx, *.csv, *.ods, *.sxc)
//	    * Presentations (*.ppt, *.pptx, *.pps, *.odp, *.sxi)
//	    * Images (*.jpg, *.gif,*.png)
		//HTML格式TXT格式JPG/JPEG格式GIF格式PNG格式ART格式AU格式AIFF格式XBM格式--还有SWF格式 也就是FLASH格式（但是要安装FLASH插件）
		typeOpenMap.put("css","text/css"); 
		typeOpenMap.put("html","text/html"); 
		typeOpenMap.put("htm","text/html"); 
		typeOpenMap.put("mht","text/plain"); // 暂时先用文本打开
		typeOpenMap.put("js","text/plain"); 
		typeOpenMap.put("java","text/plain"); 
		typeOpenMap.put("css","text/plain"); 
		typeOpenMap.put("asc","text/plain"); 
		typeOpenMap.put("txt","text/plain"); 
		typeOpenMap.put("rtx","text/richtext"); 
		typeOpenMap.put("rtf","text/rtf"); 
		typeOpenMap.put("sgml","text/sgml"); 
		typeOpenMap.put("sgm","text/sgml"); 
		typeOpenMap.put("tsv","text/tab-separated-values"); 
		typeOpenMap.put("wml","text/vnd.wap.wml"); 
		typeOpenMap.put("wmls","text/vnd.wap.wmlscript"); 
		typeOpenMap.put("etx","text/x-setext"); 
		typeOpenMap.put("xsl","text/xml"); 
		typeOpenMap.put("xml","text/xml"); 
		typeOpenMap.put("au","audio/basic"); 
		typeOpenMap.put("snd","audio/basic"); 
		typeOpenMap.put("mid","audio/midi"); 
		typeOpenMap.put("midi","audio/midi"); 
		typeOpenMap.put("kar","audio/midi");
		typeOpenMap.put("mpga","audio/mpeg"); 
		typeOpenMap.put("mp2","audio/mpeg"); 
		typeOpenMap.put("mp3","audio/mpeg"); 
		typeOpenMap.put("aif","audio/x-aiff"); 
		typeOpenMap.put("aiff","audio/x-aiff"); 
		typeOpenMap.put("aifc","audio/x-aiff"); 
		typeOpenMap.put("m3u","audio/x-mpegurl"); 
		typeOpenMap.put("ram","audio/x-pn-realaudio"); 
		typeOpenMap.put("rm","audio/x-pn-realaudio"); 
		typeOpenMap.put("rpm","audio/x-pn-realaudio-plugin"); 
		typeOpenMap.put("ra","audio/x-realaudio"); 
		typeOpenMap.put("wav","audio/x-wav"); 
		typeOpenMap.put("bmp","image/bmp"); 
		typeOpenMap.put("gif","image/gif"); 
		typeOpenMap.put("ief","image/ief"); 
		typeOpenMap.put("jpeg","image/jpeg"); 
		typeOpenMap.put("jpg","image/jpeg"); 
		typeOpenMap.put("jpe","image/jpeg"); 
		typeOpenMap.put("png","image/png"); 
		typeOpenMap.put("tiff","image/tiff"); 
		typeOpenMap.put("tif","image/tiff"); 
		typeOpenMap.put("djvu","image/vnd.djvu"); 
		typeOpenMap.put("djv","image/vnd.djvu"); 
		typeOpenMap.put("wbmp","image/vnd.wap.wbmp"); 
		typeOpenMap.put("ras","image/x-cmu-raster"); 
		typeOpenMap.put("pnm","image/x-portable-anymap"); 
		typeOpenMap.put("pbm","image/x-portable-bitmap"); 
		typeOpenMap.put("pgm","image/x-portable-graymap"); 
		typeOpenMap.put("ppm","image/x-portable-pixmap"); 
		typeOpenMap.put("rgb","image/x-rgb"); 
		typeOpenMap.put("xpm","image/x-xpixmap"); 
		typeOpenMap.put("pdf","application/pdf");
		typeOpenMap.put("xbm","image/x-xbitmap"); 
		typeOpenMap.put("xwd","image/x-xwindowdump");
		
		
		
		typeMap.put("xls","application/vnd.ms-excel"); 
		typeMap.put("doc","application/msword"); 
		
		
		
		typeMap.put("ez","application/andrew-inset"); 
		typeMap.put("hqx","application/mac-binhex40"); 
		typeMap.put("cpt","application/mac-compactpro"); 
		typeMap.put("docx","application/msword"); 
		typeMap.put("bin","application/octet-stream"); 
		typeMap.put("dms","application/octet-stream"); 
		typeMap.put("lha","application/octet-stream"); 
		typeMap.put("lzh","application/octet-stream"); 
		typeMap.put("exe","application/octet-stream");
		typeMap.put("class","application/octet-stream"); 
		typeMap.put("so","application/octet-stream"); 
		typeMap.put("dll","application/octet-stream"); 
		typeMap.put("oda","application/oda"); 
		typeMap.put("ai","application/postscript"); 
		typeMap.put("eps","application/postscript");
		typeMap.put("ps","application/postscript"); 
		typeMap.put("smi","application/smil"); 
		typeMap.put("smil","application/smil"); 
		typeMap.put("mif","application/vnd.mif"); 
		typeMap.put("ppt","application/vnd.ms-powerpoint"); 
		typeMap.put("wbxml","application/vnd.wap.wbxml"); 
		typeMap.put("wmlc","application/vnd.wap.wmlc"); 
		typeMap.put("wmlsc","application/vnd.wap.wmlscriptc"); 
		typeMap.put("bcpio","application/x-bcpio"); 
		typeMap.put("vcd","application/x-cdlink"); 
		typeMap.put("pgn","application/x-chess-pgn"); 
		typeMap.put("cpio","application/x-cpio"); 
		typeMap.put("csh","application/x-csh"); 
		typeMap.put("dcr","application/x-director"); 
		typeMap.put("dir","application/x-director"); 
		typeMap.put("dxr","application/x-director"); 
		typeMap.put("dvi","application/x-dvi"); 
		typeMap.put("spl","application/x-futuresplash"); 
		typeMap.put("gtar","application/x-gtar"); 
		typeMap.put("hdf","application/x-hdf"); 
		typeMap.put("js","application/x-javascript"); 
		typeMap.put("skp","application/x-koan"); 
		typeMap.put("skd","application/x-koan"); 
		typeMap.put("skt","application/x-koan"); 
		typeMap.put("skm","application/x-koan"); 
		typeMap.put("latex","application/x-latex"); 
		typeMap.put("nc","application/x-netcdf"); 
		typeMap.put("cdf","application/x-netcdf"); 
		typeMap.put("sh","application/x-sh"); 
		typeMap.put("shar","application/x-shar"); 
		typeMap.put("sit","application/x-stuffit"); 
		typeMap.put("sv4cpio","application/x-sv4cpio"); 
		typeMap.put("sv4crc","application/x-sv4crc"); 
		typeMap.put("tar","application/x-tar"); 
		typeMap.put("tcl","application/x-tcl"); 
		typeMap.put("tex","application/x-tex"); 
		typeMap.put("texinfo","application/x-texinfo"); 
		typeMap.put("texi","application/x-texinfo"); 
		typeMap.put("t","application/x-troff"); 
		typeMap.put("tr","application/x-troff"); 
		typeMap.put("roff","application/x-troff"); 
		typeMap.put("man","application/x-troff-man"); 
		typeMap.put("me","application/x-troff-me"); 
		typeMap.put("ms","application/x-troff-ms"); 
		typeMap.put("ustar","application/x-ustar"); 
		typeMap.put("src","application/x-wais-source"); 
		typeMap.put("xhtml","application/xhtml+xml"); 
		typeMap.put("xht","application/xhtml+xml"); 
		typeMap.put("zip","application/zip"); 
		typeMap.put("pdb","chemical/x-pdb"); 
		typeMap.put("xyz","chemical/x-xyz");
		typeMap.put("igs","model/iges"); 
		typeMap.put("iges","model/iges"); 
		typeMap.put("msh","model/mesh"); 
		typeMap.put("mesh","model/mesh");
		typeMap.put("silo","model/mesh"); 
		typeMap.put("wrl","model/vrml"); 
		typeMap.put("vrml","model/vrml"); 
		typeMap.put("mpeg","video/mpeg"); 
		typeMap.put("mpg","video/mpeg"); 
		typeMap.put("mpe","video/mpeg"); 
		typeMap.put("qt","video/quicktime"); 
		typeMap.put("mov","video/quicktime"); 
		typeMap.put("mxu","video/vnd.mpegurl"); 
		typeMap.put("avi","video/x-msvideo"); 
		typeMap.put("movie","video/x-sgi-movie"); 
		typeMap.put("ice","x-conference/x-cooltalk");
	}
	/**
	 * 设置打开时的文件类型
	 * @param postfixName 后缀名
	 * @param response 要响应客户端的HttpServletResponse对象
	 * @return 如果后缀名存在type则返回true，否则返回假
	 */
	public static boolean setContentType(String postfixName,HttpServletResponse response){
		postfixName = postfixName.toLowerCase();
		if(postfixName.equals("swf")){ // 放在typeOpenMap会变成下载。。。
			response.setContentType("application/x-shockwave-flash");
			return true;
		}else if(typeOpenMap.containsKey(postfixName)){
			response.setContentType(typeMap.get(postfixName));
			return true;
		}else if(typeMap.containsKey(postfixName)){
			response.setContentType(typeMap.get(postfixName)+"; CHARSET=utf8");
			return false;
		}else{
			response.setContentType("application/octet-stream; CHARSET=utf8");
			return false;
		}
	}
}


