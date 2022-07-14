package com.wlp.ecm.weap.document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import com.wlp.ecm.weap.exception.WeapException;


public interface WeapDocument {
	public static final String MIMETYPE_PDF = "application/pdf"; 
	public static final String MIMETYPE_TIFF = "image/tiff"; 
	
	public abstract void write(OutputStream out) throws IOException;
	public abstract ByteArrayInputStream getInputStream();
	public abstract WeapDocument convert() throws WeapException;
	public abstract WeapDocumentType getDocumentType();
	public abstract String getMimeType();
	public abstract boolean isMultiPageDocument();
	public abstract Set<WeapImagePage> getPages();
	public abstract void addPage(WeapImagePage page); 
	public abstract int getPageCount() throws WeapException;
}
