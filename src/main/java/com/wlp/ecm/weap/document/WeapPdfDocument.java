package com.wlp.ecm.weap.document;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.PixelDensity;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.exception.WeapException;


public class WeapPdfDocument implements WeapDocument 
{
	private byte[] docBytes;
	
	public WeapPdfDocument(byte[] docBytes) 
	{
		super();
		this.docBytes = docBytes;
	}
	
	@Override
	public void write(OutputStream out) throws IOException 
	{
		LoggingUtil.LogTraceStartMsg();
		out.write(docBytes);
		LoggingUtil.LogTraceEndMsg();
	}

	@Override
	public WeapDocumentType getDocumentType() 
	{
		return WeapDocumentType.PDF;
	}

	@Override
	public String getMimeType() 
	{
		return MIMETYPE_PDF;
	}

	@Override
	public WeapDocument convert() throws WeapException 
	{
		LoggingUtil.LogTraceStartMsg();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(docBytes);
		
		WeapImageDocument newImageDoc = new WeapImageDocument();
		
		synchronized(PdfDecoder.class) {
			PdfDecoder decoder = new PdfDecoder(true);
			float pageScaling = 300f/72f;
			
			try 
			{
				decoder.openPdfFileFromInputStream(bais, false);
				if(decoder.isEncrypted() && !decoder.isFileViewable()) 
				{
					throw new WeapException("E1005");
				}
				else if ((decoder.isEncrypted()&&(!decoder.isPasswordSupplied())) && (!decoder.isExtractionAllowed())) 
				{
					throw new WeapException("E1006");
				}
				
				int pdfPageCount = decoder.getPageCount();
				
				for (int pageNumber = 1; pageNumber <= pdfPageCount; pageNumber++) 
				{
					decoder.useHiResScreenDisplay(true);
					decoder.setPageParameters(pageScaling, pageNumber);
					BufferedImage image = decoder.getPageAsImage(pageNumber);
					
					if (image != null) 
					{
						final ImageFormat format = ImageFormat.IMAGE_FORMAT_TIFF;
						final Map<String, Object> params = new HashMap<String, Object>();
		
						params.put(ImagingConstants.PARAM_KEY_COMPRESSION, new Integer(TiffConstants.TIFF_COMPRESSION_CCITT_GROUP_4));
						PixelDensity pd = PixelDensity.createFromPixelsPerInch(300d, 300d);
						params.put(ImagingConstants.PARAM_KEY_PIXEL_DENSITY, pd);
		
						byte[] pageBytes = Imaging.writeImageToBytes(image, format, params);
						WeapImagePage page = new WeapImagePage(pageBytes);
						newImageDoc.addPage(page);
						LoggingUtil.LogInfoMsg("Adding page " + Integer.toString(pageNumber));
					}
				}
			} 
			catch (PdfException e) 
			{
				LoggingUtil.LogErrorMsg(e);
				throw new WeapException("E1007", e);
			} 
			catch (ImagingException e) 
			{
				LoggingUtil.LogErrorMsg(e);
				throw new WeapException("E1008", e);
			} 
			catch (IOException e) 
			{
				LoggingUtil.LogErrorMsg(e);
				throw new WeapException("E1009", e);
			} 
			finally 
			{
				if (decoder.isOpen())
				{
					decoder.closePdfFile();
				}
			}
		}
		
		LoggingUtil.LogTraceEndMsg();
		return newImageDoc;
	}

	@Override
	public int getPageCount() throws WeapException 
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(docBytes);
		
		WeapImageDocument newImageDoc = new WeapImageDocument();
		int pdfPageCount = 0;
		
		synchronized(PdfDecoder.class) 
		{
			PdfDecoder decoder = new PdfDecoder(true);

			try 
			{
				decoder.openPdfFileFromInputStream(bais, false);
				
				if(decoder.isEncrypted() && !decoder.isFileViewable()) 
				{
					throw new WeapException("E1005");
				}
				else if ((decoder.isEncrypted()&&(!decoder.isPasswordSupplied())) && (!decoder.isExtractionAllowed())) 
				{
					throw new WeapException("E1006");
				}
				
				pdfPageCount = decoder.getPageCount();
			}
			catch (PdfException e) 
			{
				LoggingUtil.LogErrorMsg(e);
				throw new WeapException("E1007", e);
			} 
			finally 
			{
				if (decoder.isOpen())
				{
					decoder.closePdfFile();
				}
			}
		}
		
		return pdfPageCount;
	}		
				
				
				
	@Override
	public ByteArrayInputStream getInputStream() 
	{
		return new ByteArrayInputStream(docBytes);
	}

	@Override
	public boolean isMultiPageDocument() 
	{
		return false;
	}

	@Override
	public Set<WeapImagePage> getPages() 
	{
		return null;
	}

	@Override
	public void addPage(WeapImagePage page) 
	{
		// nothing to do
	}
}
