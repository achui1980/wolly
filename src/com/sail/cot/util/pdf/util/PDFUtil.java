package com.sail.cot.util.pdf.util;

import java.util.Date;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.sail.cot.util.RMB;

public class PDFUtil {
	public static Paragraph getItalic(String text){
		return new Paragraph(text,new Font(FontFamily.UNDEFINED, 8, Font.ITALIC, null));
	}
	
	public static Paragraph getBold(String text){
		return new Paragraph(text,new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null));
	}
	
	public static Paragraph getUnd(String text){
		return new Paragraph(text,new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null));
	}
	public static String formatDate(Date date){
		return RMB.changeDate(date,"dd MMMMM yyyy");
	}
}
