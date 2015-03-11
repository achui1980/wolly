/**
 * 
 */
package com.sail.cot.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import com.keypoint.PngEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p>Title: 旗行办公自动化系统（OA）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * <p>Create Time: May 13, 2010 8:43:34 PM </p>
 * <p>Class Name: ImageCompressUtil.java </p>
 * @author achui
 *
 */
public class ImageCompressUtil {
	/**
	 * 描述：
	 * @param path 需要压缩的图片路径
	 * @param fileName 要压缩的图片名称
	 * @param toFileName 压缩后的图片名称
	 * @param scale 压缩比例 不能大于1,默认0.5
	 * @param quality 压缩品质介于0.1~1.0之间 
	 * @param width 压缩后的图片的宽度
	 * @param height 压缩后的图片的高度
	 * 返回值：void
	 */
	private static void imageCompress(String path,String fileName,String toFileName,float scale,float quality,int width,int height){
	    try {
	    	long start = System.currentTimeMillis();
		    Image image = javax.imageio.ImageIO.read(new File(path + fileName));
		    int imageWidth = image.getWidth(null);
	    	int imageHeight = image.getHeight(null);
	    	if(scale >0.5) scale = 0.5f;//默认压缩比为0.5，压缩比越大，对内存要去越高，可能导致内存溢出
	    	//按比例计算出来的压缩比
		    float realscale = getRatio(imageWidth,imageHeight,width,height);
	    	float finalScale = Math.min(scale, realscale);//取压缩比最小的进行压缩
		    imageWidth = (int)(finalScale*imageWidth);
		    imageHeight = (int)(finalScale*imageHeight);
		    
		    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_AREA_AVERAGING);
		    // Make a BufferedImage from the Image.
		    BufferedImage mBufferedImage = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
		    Graphics2D g2 = mBufferedImage.createGraphics();
	
		    g2.drawImage(image, 0, 0,imageWidth, imageHeight, Color.white,null);
		    g2.dispose();
		    
				float[] kernelData2 = { 
						-0.125f, -0.125f, -0.125f,
	          -0.125f,2, -0.125f,
	          -0.125f,-0.125f, -0.125f };
		    Kernel kernel = new Kernel(3, 3, kernelData2);
		    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		    mBufferedImage = cOp.filter(mBufferedImage, null);

		    FileOutputStream out = new FileOutputStream(path + toFileName);
	      //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
	      //param.setQuality(0.9f, true);
	      //encoder.setJPEGEncodeParam(param);
		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(mBufferedImage);
		    param.setQuality(quality, true);//默认0.75
		    encoder.setJPEGEncodeParam(param);
		    encoder.encode(mBufferedImage);
		    out.close();
		    long end = System.currentTimeMillis();
		    System.out.println("图片："+fileName+"，压缩时间："+(end - start) + "ms");
	    }catch (FileNotFoundException fnf){
	    }catch (IOException ioe){
	    	ioe.printStackTrace();
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }finally{
	    
	    }
	  }
	public static void imageCompress(String path,String fileName,String toFileName,float scale,int width,int height){
		imageCompress(path, fileName, toFileName, scale, 0.75f, width, height);
	}
	private static float getRatio(int width,int height,int maxWidth,int maxHeight){
	  	float Ratio = 1.0f;
	  	float widthRatio ;
	  	float heightRatio ;
	    widthRatio = (float)maxWidth/width;
	    heightRatio = (float)maxHeight/height;
	    if(widthRatio<1.0 || heightRatio<1.0){
	      Ratio = widthRatio<=heightRatio?widthRatio:heightRatio;
	    }
	    return Ratio;
	  }
	public static byte[] convertImage2Type(String imageFile,String imageType) throws Exception{
		File inputFile = new File(imageFile);
		ByteArrayOutputStream output =new ByteArrayOutputStream();
        BufferedImage input = ImageIO.read(inputFile);
        ImageIO.write(input, imageType, output);
        return output.toByteArray();
	}
	public static void convertImage2TypePng(String imageFile,String imageType) throws Exception{
		File inputFile = new File(imageFile);
		int suffixIndex = imageFile.lastIndexOf(".");
		String suffix =imageFile.substring(suffixIndex+1);
		if(!"png".equals(suffix)){//如果原图片的不是PNG格式的图片
			String fileName = imageFile.substring(0,suffixIndex+1)+"png";
			File output = new File(fileName);
			BufferedImage input = ImageIO.read(inputFile);
			ImageIO.write(input, imageType, output);
			//转换后删除原文件
			if(inputFile.exists())
				inputFile.delete();
		}
	}
	  public static void main(String[] args) throws Exception{
//		  long t1=System.currentTimeMillis();
//		  File inputFile = new File("E:\\apache-tomcat-6.0.18\\webapps\\CotSystem\\temp\\555.jpg");
//		  if(inputFile.isFile() && inputFile.exists()){
//		  //File outputFile = new File("D:\\test_compress.png");
//          //BufferedImage input = ImageIO.read(inputFile);
//			  inputFile.delete();
//		  }else{
//			  System.out.println("ddddd");
//		  }
//          String fileName ="d:\\HA01331-AM.jpg";
//          boolean cmyk = isCMYK(fileName);
//          System.out.println(cmyk);
		  test();
          //argChangegrad(fileName1);
          //cmyk2rgb(fileName);
		  
		  
//          ImageIO.write(input, "PNG", new File("D:\\test.png"));
//          input = ImageIO.read(new File("D:\\test.png"));
//          Image itemp=input.getScaledInstance(input.getWidth(),input.getHeight(),input.SCALE_AREA_AVERAGING);
//          AffineTransformOp op=new AffineTransformOp(AffineTransform.getScaleInstance(0.5,0.5),null);
//          itemp=op.filter(input,null);
//          PngEncoder encoder=new PngEncoder(itemp);
//          
//          encoder.setEncodeAlpha(false);
//          encoder.setCompressionLevel(1);
//         // encoder.pngEncode();
//          
//          byte[] result=encoder.pngEncode();
//          FileOutputStream fos=new FileOutputStream(outputFile);
//          fos.write(result);
//          fos.close();
//          System.out.println(System.currentTimeMillis()-t1+"ms");
//          System.out.println(input.getWidth());
//		  ImageIO.write(input, "PNG", outputFile);
		  //ImageCompressUtil.imageCompress("E:\\Downloads\\Tools\\73750989016177\\", "Prettybeauty069.jpg", "test_1.jpg",0.5f, input.getWidth(null),input.getHeight(null));
		  //ImageIO.write(ImageIO.read(new File("E:\\Downloads\\Tools\\73750989016177\\test_1.jpg")), "PNG", new File("E:\\Downloads\\Tools\\73750989016177\\test_1p.png"));
		  //		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_2.png",0.2f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_3.png",0.3f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_4.png",0.4f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_5.png",0.5f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_6.png",0.6f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_7.png",0.7f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_8.png",0.8f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_9.png",0.9f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.imageCompress("D:\\", "test.png", "test_10.png",1.0f, input.getWidth(null),input.getHeight(null));
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_09.jpg",0.9f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_08.jpg",0.8f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269.jpg",0.75f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_06.jpg",0.6f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_05.jpg",0.5f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_04.jpg",0.4f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_03.jpg",0.3f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_02.jpg",0.2f);
//		  ImageCompressUtil.ImageScale("F:\\achui_pic\\wedding pic\\", "DSC_0269.jpg", "DSC_0269_C_01.jpg",0.1f);
	  }
	  public static boolean isCMYK(String filename)
	    {
	        boolean result = false;
	        BufferedImage img = null;
	        try
	        {
	        	ImageInputStream im =new FileImageInputStream(new File(filename));

	        	InputStream is =new FileInputStream(new File(filename));
	            img = ImageIO.read(is);
	        }
	        catch (IOException e)
	        {
	            System.out.println(e.getMessage() + ": " + filename);
	        }
	        if (img != null)
	        {
	            int colorSpaceType = img.getColorModel().getColorSpace().getType();
	            result = colorSpaceType == ColorSpace.TYPE_CMYK;
	          // result = colorSpaceType ==ColorSpace.TYPE_RGB; 

	        }

	        return result;
	    }
	  public static void argChangegrad(String filename) throws IOException{
		  File imageFile = new File(filename);
		  String format = "gif";
	      String rgbFilename = filename;
	      BufferedImage image = ImageIO.read(imageFile);
	      if (image != null){
	    	  int colorSpaceType = image.getColorModel().getColorSpace().getType();
	    	  if (colorSpaceType == ColorSpace.TYPE_RGB){
	                BufferedImage newPic = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
	        		ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
	        		cco.filter(image, newPic);
	        		ImageIO.write(newPic, format, new File(rgbFilename));
	    	  }
	      }
	  }
	
	  private static String cmyk2rgb(String filename) throws IOException
	    {
	        // Change this format into any ImageIO supported format.
	        String format = "gif";
	        File imageFile = new File(filename);
	        String rgbFilename = filename;
	        BufferedImage image = ImageIO.read(imageFile);
	        if (image != null)
	        {
	            int colorSpaceType = image.getColorModel().getColorSpace().getType();
	            if (colorSpaceType == ColorSpace.TYPE_CMYK)
	            {
	                BufferedImage rgbImage =
	                    new BufferedImage(
	                        image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	                ColorConvertOp op = new ColorConvertOp(null);
	                op.filter(image, rgbImage);

	                rgbFilename = changeExtension(imageFile.getName(), format);
	                rgbFilename = new File(imageFile.getParent(), format + "_" + rgbFilename).getPath();
	                ImageIO.write(rgbImage, format, new File(rgbFilename));
	            }
	        }
	        return rgbFilename;
	    }
	  private static String changeExtension(String filename, String newExtension)
	    {
	        String result = filename;
	        if (filename != null && newExtension != null && newExtension.length() != 0);
	        {
	            int dot = filename.lastIndexOf('.');
	            if (dot != -1)
	            {
	                result = filename.substring(0, dot) + '.' + newExtension;
	            }
	        }
	        return result;
	    }
	  
	  public static void test() throws IOException{
//		  File f = new File("D:\\ps\\HA01331-AM.jpg");
//
//		    //Find a suitable ImageReader
//		    Iterator readers = ImageIO.getImageReadersByFormatName("JPEG");
//		    ImageReader reader = null;
//		    while(readers.hasNext()) {
//		        reader = (ImageReader)readers.next();
//		        if(reader.canReadRaster()) {
//		            break;
//		        }
//		    }
//		    
//		    //Stream the image file (the original CMYK image)
//		    ImageInputStream input =   ImageIO.createImageInputStream(f); 
//		    reader.setInput(input); 
//		    System.out.println(input.length());
//		    //Read the image raster
//		    Raster raster = reader.readRaster(0, null); 
//
//		    //Create a new RGB image
//		    BufferedImage bi = new BufferedImage(raster.getWidth(), raster.getHeight(), 
//		    BufferedImage.TYPE_4BYTE_ABGR); 
//
//		    //Fill the new image with the old raster
//		    bi.getRaster().setRect(raster);
//		    System.out.println("4");
		    
		    System.out.println("当前JRE：" + System.getProperty("java.version")); 
			System.out.println("当前JVM的默认字符集：" + Charset.defaultCharset());
	  }
	  
}
