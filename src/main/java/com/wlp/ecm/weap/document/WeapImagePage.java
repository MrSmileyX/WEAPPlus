package com.wlp.ecm.weap.document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.exception.WeapException;

public class WeapImagePage implements WeapDocument {
	private byte[] imageBytes;

	public WeapImagePage(byte[] bytes) {
		this.imageBytes = bytes;
	}

	@Override
	public void write(OutputStream out) throws IOException {
		LoggingUtil.LogTraceStartMsg();
		out.write(imageBytes);
		LoggingUtil.LogTraceEndMsg();
	}

	@Override
	public WeapDocumentType getDocumentType() {
		return WeapDocumentType.IMAGE;
	}

	@Override
	public String getMimeType() {
		return MIMETYPE_TIFF;
	}

	@Override
	public WeapDocument convert() {
		return this;
	}

	@Override
	public ByteArrayInputStream getInputStream() {
		return new ByteArrayInputStream(imageBytes);
	}

	@Override
	public boolean isMultiPageDocument() {
		return false;
	}

	@Override
	public Set<WeapImagePage> getPages() {
		return null;
	}

	@Override
	public void addPage(WeapImagePage page) {
		// can't do this here
	}

	@Override
	public int getPageCount() throws WeapException {
		// TODO Auto-generated method stub
		return 0;
	}
}
