package com.wlp.ecm.weap.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.pdfbox.ImportXFDF;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.fdf.FDFDocument;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.exception.WeapException;


public class WeapXfdfDocument implements WeapDocument {
	
	String xfdfSource = null;
	private static final String WHITESPACE_XPATH_EXPRESSION = "//text()[normalize-space(.) = '']";

	public WeapXfdfDocument(String xfdfSource) {
		LoggingUtil.LogTraceStartMsg();
		this.xfdfSource = xfdfSource;
		LoggingUtil.LogTraceEndMsg();
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		LoggingUtil.LogTraceStartMsg();

		out.write(xfdfSource.getBytes());

		LoggingUtil.LogTraceEndMsg();
	}

	/**
	 * The method removes whitespace of all child nodes.
	 * 
	 * The FDF feature of PDBBox doesnt handle Text nodes(whitespace).  When it
	 * traverses through the child nodes of the fields element it assumes all
	 * children are Elements.  It throws an exception when it encounters
	 * 
	 * @param n The node which needs the removal of whitespace
	 */
	protected void RemoveWhitespace(Node n) throws XPathExpressionException {
		LoggingUtil.LogTraceStartMsg();

		XPathFactory xpathFactory = XPathFactory.newInstance();
		// XPath to find empty text nodes.
		
		XPathExpression xpathExp;
		xpathExp = xpathFactory.newXPath().compile(WHITESPACE_XPATH_EXPRESSION);
		NodeList emptyTextNodes = (NodeList)xpathExp.evaluate(n, XPathConstants.NODESET);
		// Remove each empty text node from document.
		for (int i = 0; i < emptyTextNodes.getLength(); i++) {
		    Node emptyTextNode = emptyTextNodes.item(i);
		    emptyTextNode.getParentNode().removeChild(emptyTextNode);
		}

		LoggingUtil.LogTraceEndMsg();
	}

	@Override
	public WeapDocumentType getDocumentType() {
		return WeapDocumentType.XFDF;
	}

	@Override
	public String getMimeType() {
		return MIMETYPE_PDF;
	}

	@Override
	public WeapDocument convert() throws WeapException {
		LoggingUtil.LogTraceStartMsg();

		//WeapConfigSettings config = ConfigUtils.getWeapConfigSettings(); 
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		WeapConfigSettings config = ctx.getBean(WeapConfigSettings.class);

		ImportXFDF xfdfImport = new ImportXFDF();
		WeapPdfDocument newDocument = null; 

		/** TODO: add a schema reference to http://partners.adobe.com/public/developer/en/xml/xfdf.xsd.
		  * This will help to validate the doc before its sent to PDFBox and provide more meaningful
		  * validation failure messages.  PDFBox does not validate but will throw exceptions messages
		  * that are very difficult to diagnose.  
		*/
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(xfdfSource.getBytes());
			Document xmlDoc = db.parse(is);
			
			// PDFBox doesn't handle whitespace. We need a DOM that does not have any Text or Comment
			RemoveWhitespace(xmlDoc);
			
			NodeList nodeList = xmlDoc.getElementsByTagName("f");
			Element el = (Element)nodeList.item(0);
			String templPath = el.getAttribute("href");
			File fileRef = new File(templPath);
			
			if (!fileRef.exists()) {
				templPath = config.getTemplateDirectory() + fileRef.getName();
				el.setAttribute("href", templPath);
			}
			
			//is = new ByteArrayInputStream(xmlDoc.get);
			//BufferedInputStream bis = new BufferedInputStream(is);
	
			PDDocument templPdfDoc = null;
			FDFDocument fdfDocument = null;
			try {
				templPdfDoc = PDDocument.load(templPath);
		
				// Need to re-create the input stream
				//is.reset();
				//is = new ByteArrayInputStream(xfdfSource.getBytes());
				//FDFDocument fdfDocument = FDFDocument.loadXFDF(is);
				fdfDocument = new FDFDocument(xmlDoc);
				
				xfdfImport.importFDF(templPdfDoc, fdfDocument);
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				templPdfDoc.save(out);
				newDocument = new WeapPdfDocument(out.toByteArray()); 
			} catch (COSVisitorException e) {
				LoggingUtil.LogErrorMsg(e);
				throw new WeapException("E1010", e);
			} finally {
				if (templPdfDoc != null) {
					templPdfDoc.close();
				}
				if (fdfDocument != null) {
					fdfDocument.close();
				}
			}
		} catch (ParserConfigurationException e) {
			LoggingUtil.LogErrorMsg(e);
			throw new WeapException("E1011", e);
		} catch (XPathExpressionException e) {
			LoggingUtil.LogErrorMsg(e);
			throw new WeapException("E1012", e);
		} catch (SAXException e) {
			LoggingUtil.LogErrorMsg(e);
			throw new WeapException("E1013", e);
		} catch (IOException e) {
			LoggingUtil.LogErrorMsg(e);
			throw new WeapException("E1014", e);
		}
		LoggingUtil.LogTraceEndMsg();
		return newDocument;
	}

	@Override
	public ByteArrayInputStream getInputStream() {
		return new ByteArrayInputStream(xfdfSource.getBytes());
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
		// not supported
	}

	@Override
	public int getPageCount() throws WeapException {
		// TODO Auto-generated method stub
		return 0;
	}

}
