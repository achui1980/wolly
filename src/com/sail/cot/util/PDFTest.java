package com.sail.cot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.lang.RandomStringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.util.pdf.util.TableHeader;

public class PDFTest extends PdfPageEventHelper{

    public Document initDocument(){
        Rectangle rectangle = new Rectangle(PageSize.getRectangle("A4"));
        Document document = new Document(rectangle,32,20,10,10);
        return document;
    }
    
    public void initComInfo(Document document){
        document.addTitle("IN_1234567");
        document.addSubject("IN_1234567");
        document.addAuthor("achui");
        document.addKeywords("TCPDF, PDF, example, test, guide");
        document.addCreator("TCPDF");
        
    }
    public void createInPDF(String path,boolean chk) throws DocumentException, MalformedURLException, IOException{
        Document document = this.initDocument();
        PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
        this.writePdf(writer,document,chk);
    }
    public void writePdf(PdfWriter writer,Document document,boolean chk) throws MalformedURLException, DocumentException, IOException{
        writer.setBoxSize("art", new Rectangle(36, 54, 100, 10));
        TableHeader event = new TableHeader();
        writer.setPageEvent(event);
        document.open(); 
        this.initComInfo(document);
        document.add(this.createBody());
        document.close();
    }
    public PdfPTable createBody() throws MalformedURLException, IOException, DocumentException{
//      String title[] = {"Article No.","Client Article No.","Description","Barcode","Size","Quantity","Amount","Total"};
        String title[] = {"Description","Barcode","Client Article No.","Size","Quantity","Unit","Amount","Total"};
        // 设置表格的形式
        PdfPTable table = new PdfPTable(new float[]{22,10,10,10,6,4,8,8}); // 最外表
        table.setSplitLate(false);
        PdfPCell defaultCell = table.getDefaultCell();
        table.setWidthPercentage(100);
        table.setSplitRows(false);
        //table.setHeaderRows(1);
        defaultCell.setPaddingBottom(5);
        defaultCell.setBorder(0); // 无边框
        defaultCell.setBackgroundColor(new BaseColor(240,240,240));
        defaultCell.setBorderColorRight(BaseColor.WHITE);
        defaultCell.setBorderWidthRight(1f);
        
        Font boldFont = new Font();
        boldFont.setStyle(Font.BOLD);
        boldFont.setSize(8);
        PdfPCell cell = null;
        
        
        for(int i = 0;i<title.length;i++){
            cell = new PdfPCell();
            cell.addElement(new Paragraph(String.valueOf(title[i]),boldFont));
            cell.setBorder(0);
            cell.setPaddingBottom(5f);
            table.addCell(cell);
        }
        Font font = new Font();
        font.setSize(8);
        // 表体设置
        for(int i = 0;i<100;i++){
            if(i%2==0)
                defaultCell.setBackgroundColor(new BaseColor(240,240,240));
            else
                defaultCell.setBackgroundColor(BaseColor.WHITE);
            defaultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//          table.addCell(new Paragraph(detail.getEleId(),font));
            table.addCell(new Paragraph(String.valueOf(i),font));
            table.addCell(new Paragraph(String.valueOf(RandomStringUtils.randomAlphabetic(10)),font));
            table.addCell(new Paragraph(String.valueOf(RandomStringUtils.randomAlphabetic(10)),font));
//          defaultCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(new Paragraph(String.valueOf(RandomStringUtils.randomAlphabetic(10)),font));
            table.addCell(new Paragraph(String.valueOf("248"),font));
            table.addCell(new Paragraph(String.valueOf(RandomStringUtils.randomAlphabetic(7)),font));
            table.addCell(new Paragraph(String.valueOf(RandomStringUtils.randomNumeric(3)),font));
            table.addCell(new Paragraph(String.valueOf(RandomStringUtils.randomNumeric(10)),font));
        }
        
        cell = new PdfPCell(new Paragraph(""));
        cell.setColspan(title.length);
        cell.setBorder(0);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Total Amount: 100000",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
            
        cell.setColspan(title.length);
        cell.setBorder(0);
        cell.setBorderColor(new BaseColor(120,120,120));
        cell.setBorderWidthTop(.1f);
        cell.setBorderWidthBottom(.1f);
//      cell.setUseDescender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingBottom(6f);
        cell.setPaddingRight(1f);
        table.addCell(cell);
        
        return table;
    }
    
    public PdfPTable mailHeaderTable(){
        boolean isAgent =false;
        PdfPTable table = new PdfPTable(isAgent?3:2);
        table.setWidthPercentage(65);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell defaultCell = table.getDefaultCell();
        defaultCell.setBorder(0); // 无边框
        defaultCell.setBackgroundColor(new BaseColor(240,240,240));
        defaultCell.setBorderColorRight(BaseColor.WHITE);
        defaultCell.setBorderWidthRight(4f);
        defaultCell.setVerticalAlignment(Element.ALIGN_CENTER);
        defaultCell.setFixedHeight(15f);
        
        table.addCell(new Paragraph("Buyer",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
        table.addCell(new Paragraph("Seller",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
        if(isAgent)
            table.addCell(new Paragraph("Agent",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
        
        defaultCell.setBackgroundColor(BaseColor.WHITE);
        
        defaultCell.setFixedHeight(0.0f);
        defaultCell.setPaddingTop(6f);
        defaultCell.setPaddingRight(10f);
        defaultCell.setLeading(0, 1.3f);
        String buyer = "achui";
        String seller = "achui";
        buyer = buyer==null ? " ":buyer;
        seller = seller == null ? " " : seller;
        table.addCell(new Paragraph(buyer.replace(",", " "),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
        table.addCell(new Paragraph(seller.replace(",", " "),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
        if(isAgent){
            String agent = "achui";
            agent = agent == null ? " " : agent;
            table.addCell(new Paragraph(agent.replace(",", " "),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
        }
        
        return table;
    }
    
    public static void main(String[] args) throws MalformedURLException, DocumentException, IOException{
        PDFTest test = new PDFTest();
        test.createInPDF("D:/test.pdf", false);
    }
}
