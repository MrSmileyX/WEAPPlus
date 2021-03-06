package com.wlp.ecm.weap.mock;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MockMessageException extends RuntimeException  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2292809066117050491L;
	private Throwable nested;
    
    public MockMessageException(String message, Throwable nested)
    {
        super(message);
        this.nested = nested;
    }
    
    public MockMessageException(Throwable nested)
    {
        this.nested = nested;
    }
    
    /**
     * Returns the nested exception 
     * (which may also be a <code>NestedApplicationException</code>)
     * @return the nested exception
     */
    public Throwable getNested()
    {
        return nested;
    }
    
    /**
     * Returns the root cause, i.e. the first exception that is
     * not an instance of <code>NestedApplicationException</code>.
     * @return the root exception
     */
    public Throwable getRootCause()
    {
        if(nested == null) return null;
        if(!(nested instanceof MockMessageException)) return nested;
        return ((MockMessageException)nested).getRootCause();
    }
    
    public String getMessage()
    {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        String message = super.getMessage();
        if(null != message)
        {
            printWriter.println(super.getMessage());
        }
        else
        {
            printWriter.println();
        }
        Throwable cause = getRootCause();
        if(null != cause)
        {
            printWriter.print("Cause: ");
            cause.printStackTrace(printWriter);
        }
        writer.flush();
        return writer.toString();
    }

}
