package myapp.payment.utilities;

import javax.servlet.http.HttpServletRequest;

/**
 * Creates URL string for a given request which is a string of href
 * 
 * 
 * @author Nandan
 *
 */
public class ResponseUrlCreator
{
	/**
	 * Creates URL string for a given request which is a string of href
	 * 
	 * @param request
	 * @return
	 * URL in String
	 */
	public String createURL(HttpServletRequest request) {
	    String scheme = request.getScheme();
	    String serverName = request.getServerName();
	    int serverPort = request.getServerPort();
	    String contextPath = request.getContextPath();
	    StringBuffer url =  new StringBuffer();
	    url.append(scheme).append("://").append(serverName);
	    if ((serverPort != 80) && (serverPort != 443)) {
	        url.append(":").append(serverPort);
	    }
	    url.append(contextPath);
	    if(url.toString().endsWith("/")){
	    	url.append("/");
	    }
	    return url.toString();
	}


}
