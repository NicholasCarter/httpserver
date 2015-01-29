import java.util.*;

class Request {
	private String requestLine;
	private HashMap< String, String > headers;

	public Request()
	{
		headers = new HashMap< String, String >();
	}

	public String getOpcode()
	{
		return requestLine.split( " " )[0];
	}

	public String getPath()
	{
		return requestLine.split( " " )[1];
	}

	public String getVersion()
	{
		return requestLine.split( " " )[2];
	}

	public HashMap< String, String > getHeaders()
	{
		return headers;
	}

	public String getResponse()
	{
		return "HTTP/1.1 202 OKr\nContent-type: text/html\r\nContent-length: 58\r\n\r\n<html><head><title>OK</title></head><body>OK.</body><html>";
	}

	public String getRequestLine()
	{
		return requestLine;
	}

	public void setRequestLine( String requestLine )
	{
		this.requestLine = requestLine;
	}

	public String toString()
	{
		String s = requestLine + " " + headers.toString();
		return s;
	}

}