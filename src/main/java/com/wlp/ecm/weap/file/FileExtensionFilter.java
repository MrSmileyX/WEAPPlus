package com.wlp.ecm.weap.file;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FileExtensionFilter implements FilenameFilter {private Set<String> exts = new HashSet<String>();

	    public FileExtensionFilter(String... extensions) 
	    {
	        for (String ext : extensions) 
	        {
	            exts.add("." + ext.toLowerCase().trim());
	        }
	    }

	    public boolean accept(File dir, String name) 
	    {
	        final Iterator<String> extList = exts.iterator();
	        
	        while (extList.hasNext()) 
	        {
	            if (name.toLowerCase().endsWith(extList.next())) 
	            {
	                return true;
	            }
	        }
	        return false;
	    }
}