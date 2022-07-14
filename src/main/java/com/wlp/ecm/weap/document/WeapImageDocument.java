package com.wlp.ecm.weap.document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
//import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.exception.WeapException;

public class WeapImageDocument implements WeapDocument 
{
	Set<WeapImagePage> pages = null;	
	
	public WeapImageDocument() 
	{
		pages = new LinkedHashSet<WeapImagePage>();
	}
	
	@Override
	public void write(OutputStream out) throws IOException 
	{
		LoggingUtil.LogTraceStartMsg();
		
		for (WeapImagePage page : pages) 
		{
			page.write(out);
		}
		
		LoggingUtil.LogTraceEndMsg();
	}

	@Override
	public WeapDocumentType getDocumentType() {
		return WeapDocumentType.IMAGE;
	}

	@Override
	public String getMimeType() 
	{
		return MIMETYPE_TIFF;
	}

	@Override
	public WeapDocument convert() 
	{
		return this;
	}

	@Override
	public ByteArrayInputStream getInputStream() 
	{
		//return new ByteArrayInputStream(imageBytes);
		return null;
	}

	@Override
	public boolean isMultiPageDocument() 
	{
		return true;
	}

	@Override
	public Set<WeapImagePage> getPages() 
	{
		return pages;
	}

	@Override
	public void addPage(WeapImagePage page) 
	{
		LoggingUtil.LogTraceStartMsg();
		pages.add(page);
		LoggingUtil.LogTraceEndMsg();
	}

	@Override
	public int getPageCount() throws WeapException {
		// TODO Auto-generated method stub
		return 0;
	}
}
