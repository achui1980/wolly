package com.sail.cot.util.pdf.create;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sail.cot.domain.CotEmps;
import com.sail.cot.domain.CotOrderOutdetail;
import com.sail.cot.domain.VCeditNode;
import com.sail.cot.domain.VInvoice;
import com.sail.cot.util.RMB;
import com.sail.cot.util.SystemUtil;
import com.sail.cot.util.pdf.util.PDFUtil;
import com.sail.cot.util.pdf.util.TableHeader;

public class InPdf {
	
	private VInvoice vInvoice;
	private List<CotOrderOutdetail> detailList;
	private String headerTitle = "Invoice";
	private CotEmps emps = null;
	
	public String getHeaderTitle() {
		return headerTitle;
	}

	public void setHeaderTitle(String headerTitle) {
		this.headerTitle = headerTitle;
	}

	public Document initDocument(){
		Rectangle rectangle = new Rectangle(PageSize.getRectangle("A4"));
		
		Document document = new Document(rectangle,32,20,10,10);
//		document.setPageSize(rectangle);
//		document.setMargins(20, 20, 10, 10);
		return document;
	}
	
	private PdfPTable createHeader() throws BadElementException, MalformedURLException, IOException{
		Image gif = null;
		try {
			gif = Image.getInstance(vInvoice.getCompanyLogo());
			gif.scaleAbsolute(103, 60);
		} catch (Exception e) {
		}
		
		float[] widths = {42f,89f,62f};
		PdfPTable headTable = new PdfPTable(widths);
		headTable.setWidthPercentage(100);
		headTable.getDefaultCell().setBorder(0); // 无边框
//		headTable.getDefaultCell().setPadding(30);
		
		PdfPCell pdfPCell = new PdfPCell();
		pdfPCell.setColspan(3);
		pdfPCell.setBorder(0);
		pdfPCell.setPaddingBottom(15);
		if(vInvoice instanceof VCeditNode&&vInvoice.getOrderStatus()==8){
			this.setHeaderTitle("Invoice");
		}
		pdfPCell.addElement(new Paragraph(this.getHeaderTitle(),new Font(FontFamily.UNDEFINED, 15, Font.BOLD, null)));
		headTable.addCell(pdfPCell);
		
		pdfPCell = new PdfPCell();
//		pdfPCell.setImage(gif);
		pdfPCell.addElement(gif);
		pdfPCell.setRowspan(6);
		pdfPCell.setBorder(0);
		headTable.addCell(pdfPCell);
		
		headTable.addCell(PDFUtil.getBold("Date:"));
		headTable.addCell(PDFUtil.getBold("Buyer order No.            "+vInvoice.getPoNo()));
		
		pdfPCell = new PdfPCell();
		pdfPCell.setColspan(2);
		pdfPCell.setBorder(0);
		pdfPCell.setPaddingTop(-1f);
		pdfPCell.addElement(new Paragraph(RMB.changeDate(vInvoice.getAddTime(),"dd MMMMM yyyy")+" "+vInvoice.getYwy(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		headTable.addCell(pdfPCell);
		
		pdfPCell = new PdfPCell();
		pdfPCell.setBorder(0);
		pdfPCell.setColspan(2);
		pdfPCell.setRowspan(3);
		pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		pdfPCell.setFixedHeight(27);
		pdfPCell.addElement(new Paragraph("Revised",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		headTable.addCell(pdfPCell);
		
		pdfPCell = new PdfPCell();
		pdfPCell.setColspan(2);
		pdfPCell.setBorder(0);
		pdfPCell.setPaddingTop(-1f);
		pdfPCell.setPaddingLeft(0f);
		PdfPTable cellTable = new PdfPTable(new float[]{13,8,17});
		cellTable.setWidthPercentage(100);
		cellTable.getDefaultCell().setBorder(0);
		cellTable.getDefaultCell().setPaddingRight(10);
//		cellTable.addCell(PDFUtil.getUnd(RMB.changeDate(vInvoice.getRevisedTime(),"dd MMMMM yyyy")+" "+vInvoice.getShy()));
		cellTable.addCell(PDFUtil.getUnd(RMB.changeDate(new java.util.Date(System.currentTimeMillis()),"dd MMMMM yyyy")+" "+emps.getEmpsName()));
		cellTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		cellTable.getDefaultCell().setPaddingRight(2);
		cellTable.getDefaultCell().setPaddingLeft(-13);
		cellTable.addCell(PDFUtil.getUnd("Invoice No."+vInvoice.getInvoiceNo()));
		cellTable.addCell("");
		pdfPCell.addElement(cellTable);
		headTable.addCell(pdfPCell);
		return headTable;
	}
	
	public PdfPTable createTopLeftTable() throws DocumentException{
		PdfPTable table = new PdfPTable(3);
		table.setWidths(new int[]{34, 8, 10});
		table.getDefaultCell().setBorder(0);
		table.setTotalWidth(555);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(20);
		table.setWidthPercentage(60f);
		PdfPCell cell = new PdfPCell();
		boolean isAgent = vInvoice.getIsCheckAgent() == 1;
		cell.setRowspan(isAgent?14:13);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Invoice Date",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(PDFUtil.getUnd(PDFUtil.formatDate(vInvoice.getInvoiceDate())));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Buyer Vat No.",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(PDFUtil.getUnd(vInvoice.getVatNo()));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Supplier No.",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(PDFUtil.getUnd(vInvoice.getFactoryNo()));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("W&C Order No.",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(vInvoice.getBusinessPerson()+vInvoice.getOrderNo(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		cell.setBorder(0);
		table.addCell(cell);
		
//		cell = new PdfPCell(new Paragraph("Sales",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
//		cell.setBorder(0);
//		table.addCell(cell);
//		cell = new PdfPCell(new Paragraph(vInvoice.getBusinessPerson(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
//		cell.setBorder(0);
//		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Department",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(vInvoice.getTypeEnName(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Shipment Date.",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(RMB.changeDate(vInvoice.getShipmentDate(),"dd MMMMM yyyy"),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		cell.setBorder(0);
		table.addCell(cell);
		

		cell = new PdfPCell(new Paragraph("Delivery Date",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(RMB.changeDate(vInvoice.getDeliveryDate(),"dd MMMMM yyyy"),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Delivery Terms",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		if(vInvoice.getJgtk()==null || vInvoice.getJgtk().trim().equals(""))
			cell = new PdfPCell(new Paragraph(" "));
		else {
			boolean isClauseName = vInvoice.getJgtk().equalsIgnoreCase("fob");
			cell = new PdfPCell(new Paragraph(vInvoice.getJgtk()+" "+(isClauseName?vInvoice.getDepapture():vInvoice.getDestination()),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
//			cell = new PdfPCell(new Paragraph(vInvoice.getJgtk(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		}
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Payment Terms",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(vInvoice.getPay(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("Currency",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(vInvoice.getMoneyType(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		cell.setBorder(0);
		table.addCell(cell);
		
		if(isAgent){
			cell = new PdfPCell(new Paragraph("Commission",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
			cell.setBorder(0);
			table.addCell(cell);
			if(vInvoice.getCommisionScale()!=null)
				cell = new PdfPCell(new Paragraph(vInvoice.getCommisionScale().intValue()+"%",new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
			else
				cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(0);
			table.addCell(cell);
		}
	
		cell = new PdfPCell(new Paragraph("Comments",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph(vInvoice.getOrderRemark(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		cell.setBorder(0);
		table.addCell(cell);
		return table;
	}
	
	public PdfPTable createBody() throws MalformedURLException, IOException, DocumentException{
//		String title[] = {"Article No.","Client Article No.","Description","Barcode","Size","Quantity","Amount","Total"};
		String title[] = {"Description","Barcode","Client Article No.","Size","Quantity","Unit","Amount","Total"};
		// 设置表格的形式
		PdfPTable table = new PdfPTable(new float[]{22,10,10,10,6,4,8,8}); // 最外表
		table.setSplitLate(false);
		PdfPCell defaultCell = table.getDefaultCell();
		table.setWidthPercentage(100);
		table.setHeaderRows(1);
		defaultCell.setPaddingBottom(5);
		defaultCell.setBorder(0); // 无边框
		defaultCell.setBackgroundColor(new BaseColor(240,240,240));
		defaultCell.setBorderColorRight(BaseColor.WHITE);
		defaultCell.setBorderWidthRight(1f);
		
		Font boldFont = new Font();
		boldFont.setStyle(Font.BOLD);
		boldFont.setSize(8);
		PdfPCell cell = null;
		
		cell = new PdfPCell();
		cell.setColspan(title.length);
		cell.setBorder(0);
		cell.setPaddingBottom(30);
		cell.addElement(this.createHeader());
		table.addCell(cell);
		
		cell = new PdfPCell();
		cell.setColspan(title.length);
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(10);
		cell.addElement(this.mailHeaderTable());
		table.addCell(cell);
		
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
		for(int i = 0;i<detailList.size();i++){
			if(i%2==0)
				defaultCell.setBackgroundColor(new BaseColor(240,240,240));
			else
				defaultCell.setBackgroundColor(BaseColor.WHITE);
			defaultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			CotOrderOutdetail detail = detailList.get(i);
//			table.addCell(new Paragraph(detail.getEleId(),font));
			table.addCell(new Paragraph(detail.getEleNameEn(),font));
			table.addCell(new Paragraph(detail.getBarcode(),font));
			table.addCell(new Paragraph(detail.getCustNo(),font));
//			defaultCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(new Paragraph(detail.getEleInchDesc(),font));
			if(detail.getBoxCount()==null)
				table.addCell(new Paragraph("",font));
			else 
				table.addCell(new Paragraph(RMB.getPrecision(detail.getBoxCount(), 0),font));
			table.addCell(new Paragraph((detail.getEleUnit() == null?"":detail.getEleUnit()),font));
			table.addCell(new Paragraph(RMB.getPrecision(detail.getOrderPrice(), 2),font));
			table.addCell(new Paragraph(RMB.getPrecision(detail.getTotalMoney(), 2),font));
		}
		
		cell = new PdfPCell(new Paragraph(""));
		cell.setColspan(title.length);
		cell.setBorder(0);
		table.addCell(cell);
		if(vInvoice.getTaxLv()==null||vInvoice.getTaxLv()==0)
			cell = new PdfPCell(new Paragraph("Total Amount: "+RMB.getPrecision(vInvoice.getOmoney(), 2),new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
		else
			cell = new PdfPCell(new Paragraph("Vat: "+RMB.getPrecision(vInvoice.getOmoney()/4, 2)+"  "+
					"Total Amount: "+RMB.getPrecision(vInvoice.getOmoney()*1.25, 2),new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)));
			
		cell.setColspan(title.length);
		cell.setBorder(0);
		cell.setBorderColor(new BaseColor(120,120,120));
		cell.setBorderWidthTop(.1f);
		cell.setBorderWidthBottom(.1f);
//		cell.setUseDescender(true);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setPaddingBottom(6f);
		cell.setPaddingRight(1f);
		table.addCell(cell);
		
		this.mailRootTable(table);
		
		
		return table;
	}
	
	public PdfPTable mailHeaderTable(){
		boolean isAgent = vInvoice.getIsCheckAgent() == 1;
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
		String buyer = vInvoice.getBuyer();
		String seller = vInvoice.getSeller();
		buyer = buyer==null ? " ":buyer;
		seller = seller == null ? " " : seller;
		table.addCell(new Paragraph(buyer.replace(",", " "),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		table.addCell(new Paragraph(seller.replace(",", " "),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		if(isAgent){
			String agent = vInvoice.getAgent();
			agent = agent == null ? " " : agent;
			table.addCell(new Paragraph(agent.replace(",", " "),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		}
		
		return table;
	}
	public PdfPTable mailRootTable(PdfPTable table) throws DocumentException{
		PdfPCell defaultCell = table.getDefaultCell();
		defaultCell.setBackgroundColor(BaseColor.WHITE);
		defaultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		defaultCell.setLeading(0, 1.4f);
		defaultCell.setColspan(4);
		defaultCell.setPaddingRight(75f);
		
		defaultCell.setPaddingTop(20f);
		defaultCell.setColspan(3);
		defaultCell.setPaddingBottom(4f);
		table.addCell(new Paragraph("Quality",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
		defaultCell.setColspan(5);
		table.addCell(new Paragraph("Colours",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
		defaultCell.setPaddingTop(-3f);
		defaultCell.setColspan(3);
		defaultCell.setPaddingBottom(1f);
		table.addCell(new Paragraph(vInvoice.getQuality(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		defaultCell.setColspan(5);
		table.addCell(new Paragraph(vInvoice.getColours(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		
		defaultCell.setPaddingTop(20f);
		defaultCell.setColspan(3);
		defaultCell.setPaddingBottom(4f);
		table.addCell(new Paragraph("Packing",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
		defaultCell.setColspan(5);
		table.addCell(new Paragraph("Packing Assortment",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
		
		defaultCell.setPaddingTop(-2f);
		defaultCell.setPaddingBottom(1f);
		defaultCell.setColspan(3);
		PdfPTable unitTable = new PdfPTable(1);
		unitTable.setSplitLate(false);
		unitTable.getDefaultCell().setBorder(0);
		unitTable.getDefaultCell().setLeading(0, 1.3f);
		unitTable.getDefaultCell().setPaddingLeft(-1);
		unitTable.getDefaultCell().setPaddingTop(-1);
		unitTable.getDefaultCell().setPaddingBottom(0f);
		unitTable.addCell(new Paragraph("Sales Unit",new Font(FontFamily.UNDEFINED, 8, Font.ITALIC, null)) );
		unitTable.addCell(new Paragraph(vInvoice.getSaleUnit(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		unitTable.getDefaultCell().setPaddingTop(4f);
		unitTable.addCell(new Paragraph("Handling Unit",new Font(FontFamily.UNDEFINED, 8, Font.ITALIC, null)) );
		unitTable.getDefaultCell().setPaddingTop(-1f);
		unitTable.addCell(new Paragraph(vInvoice.getHandleUnit(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		table.addCell(unitTable);
		
		defaultCell.setRowspan(1);
		defaultCell.setColspan(5);
		table.addCell(new Paragraph(vInvoice.getAssortment(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		
		defaultCell.setPaddingTop(20f);
//		table.addCell(new Paragraph("Comments",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
//		defaultCell.setPaddingTop(-7f);
//		table.addCell(new Paragraph(vInvoice.getCommnets(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		
		defaultCell.setColspan(3);
		table.addCell(new Paragraph("",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
		defaultCell.setColspan(5);
		defaultCell.setPaddingBottom(4f);
		table.addCell(new Paragraph("Comments",new Font(FontFamily.UNDEFINED, 8, Font.BOLD, null)) );
		defaultCell.setPaddingTop(-2f);
		defaultCell.setColspan(3);
		table.addCell(new Paragraph("",new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		defaultCell.setColspan(5);
		defaultCell.setPaddingBottom(1f);
		table.addCell(new Paragraph(vInvoice.getCommnets(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)) );
		
		
		return table;
	}
	
	public void initComInfo(Document document){
		if(vInvoice.getOrderNo()!=null){
			document.addTitle("IN_"+vInvoice.getOrderNo());
			document.addSubject("IN_"+vInvoice.getOrderNo());
		}
		if(vInvoice.getShy()!=null)
			document.addAuthor(vInvoice.getShy());
		document.addKeywords("TCPDF, PDF, example, test, guide");
		document.addCreator("TCPDF");
		
	}
	
	
	
	public PdfPTable createSignature() throws BadElementException, MalformedURLException, IOException{
		Image gif = null;
		try {
			if(emps!=null)
				gif = Image.getInstance(SystemUtil.getRootPath()+"signature/"+emps.getId()+".jpg");
//			gif = Image.getInstance(SystemUtil.getRootPath()+"signature/"+vInvoice.getCheckperson()+".jpg");
			gif.scaleAbsolute(103, 60);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(150);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(60f);
		
		PdfPCell pdfPCell = new PdfPCell();
		pdfPCell.setBorder(0);
		if(gif!=null)
			pdfPCell.addElement(gif);
		else {
			pdfPCell.setFixedHeight(30f);
		}
		table.addCell(pdfPCell);
		
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
		table.getDefaultCell().setBorderWidthTop(0.1f);
//		table.addCell(new Paragraph(vInvoice.getShy(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		if(emps!=null)
			table.addCell(new Paragraph(emps.getEmpsId(),new Font(FontFamily.UNDEFINED, 8, Font.UNDEFINED, null)));
		return table;
	}
	
	public PdfPTable createRootTable(){
		PdfPTable table = new PdfPTable(new float[]{10,40});
		PdfPCell detailCell = table.getDefaultCell();
		detailCell.setPadding(0);
		detailCell.setBorder(0);
		table.setTotalWidth(300);
//		table.setWidthPercentage(65);
		
		table.addCell(PDFUtil.getBold("Bank:"));
		detailCell.setPaddingLeft(-36f);
		table.addCell(PDFUtil.getUnd("Nordea"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Payment:"));
		detailCell.setPaddingLeft(-21f);
		table.addCell(PDFUtil.getUnd("DDK  21863491421001   IBAN:DK4920003491421001"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Payment:"));
		detailCell.setPaddingLeft(-21f);
		table.addCell(PDFUtil.getUnd("USD  21865036271401   IBAN:DK3420005036271401"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Payment:"));
		detailCell.setPaddingLeft(-21f);
		table.addCell(PDFUtil.getUnd("EUR  21865036271428   IBAN:DK8120005036271428"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Swift Address:"));
		detailCell.setPaddingLeft(-2f);
		table.addCell(PDFUtil.getUnd("NDEADKKK"));
		return table;
	}
	
	public PdfPTable createRootTableWithOtherCompany(){
		PdfPTable table = new PdfPTable(new float[]{50,40});
		PdfPCell detailCell = table.getDefaultCell();
		detailCell.setPadding(0);
		detailCell.setBorder(0);
		table.setTotalWidth(300);
//		table.setWidthPercentage(65);
		
		table.addCell(PDFUtil.getBold("Beneficiary Bank Name          :"));
		detailCell.setPaddingLeft(-40f);
		table.addCell(PDFUtil.getUnd(" HSBC"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Bank Code                               :"));
		detailCell.setPaddingLeft(-40f);
		table.addCell(PDFUtil.getUnd(" 004"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Beneficiary Bank Address      :"));
		detailCell.setPaddingLeft(-40f);
		table.addCell(PDFUtil.getUnd(" 1 Queen’s Road Central, Hong Kong"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("SWIFT Code                             :"));
		detailCell.setPaddingLeft(-40f);
		table.addCell(PDFUtil.getUnd(" HSBCHKHHHKH"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Beneficiary Account name     :"));
		detailCell.setPaddingLeft(-40f);
		table.addCell(PDFUtil.getUnd(" HONG KONG HOME OUTFITTERS LIMITED"));
		detailCell.setPaddingLeft(0f);
		table.addCell(PDFUtil.getBold("Beneficiary Account Number :"));
		detailCell.setPaddingLeft(-40f);
		table.addCell(PDFUtil.getUnd(" 801-126 525-838"));
		return table;
	}
	
	public void writePdf(PdfWriter writer,Document document,boolean chk) throws MalformedURLException, DocumentException, IOException{
		writer.setBoxSize("art", new Rectangle(36, 54, 900, 10));
		TableHeader event = new TableHeader();
		writer.setPageEvent(event);
		document.open(); 
		this.initComInfo(document);
		this.createTopLeftTable().writeSelectedRows(0, -1, 34, 760, writer.getDirectContent());
		document.add(this.createBody());
		
		this.createSignature().writeSelectedRows(0, -1, 420, 20, writer.getDirectContent());
		
		if(chk){
			this.createRootTable().writeSelectedRows(0, -1, 22, 50, writer.getDirectContent());
		}else{
			this.createRootTableWithOtherCompany().writeSelectedRows(0, -1, 22, 50, writer.getDirectContent());
		}
		
		document.close();
	}
	@Test
	public void createInPDF(HttpServletResponse response,HttpServletRequest request,boolean isOpen,boolean chk) throws DocumentException, MalformedURLException, IOException{
		this.emps = (CotEmps) request.getSession().getAttribute("emp");
		Document document = this.initDocument();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		this.writePdf(writer,document,chk);
//		HeaderFooter event = new HeaderFooter();
		
//		response.setHeader("Expires", "0");
//        response.setHeader("Cache-Control",
//            "must-revalidate, post-check=0, pre-check=0");
//        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", (isOpen?"":"attachment;")+" filename="+vInvoice.getInvoiceNo());		
        // the contentlength
        response.setContentLength(baos.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();
	}
	public void createInPDF(String path,boolean chk) throws DocumentException, MalformedURLException, IOException{
		Document document = this.initDocument();
		PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path));
		this.writePdf(writer,document,chk);
	}

	public VInvoice getvInvoice() {
		return vInvoice;
	}

	public void setvInvoice(VInvoice vInvoice) {
		this.vInvoice = vInvoice;
	}

	public List<CotOrderOutdetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<CotOrderOutdetail> detailList) {
		this.detailList = detailList;
	}

}
