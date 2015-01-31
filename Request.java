import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

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

	public byte[] getResponse()
	{
		DateFormat format = new SimpleDateFormat( "E, dd MMM yyyy HH:mm:ss z" );
		byte[] body = null;
		byte[] hBytes = null;
		byte[] rLBytes = null;
		String headerLines = "";
		String responseLine = "";

		headerLines += "Date: " + HttpServer.currentTime() + "\r\n";
		if ( getOpcode().equals( "GET" ) )
		{
			if ( ( body = HttpServer.getDocroot().getFile( getPath() ) ) != null )
			{
				try
				{
					// if the if mod since time is before the mod time 200 else
					// 304
					if ( headers.containsKey( "If-Modified-Since" )
							&& format.parse(
									( headers.get( "If-Modified-Since" ) ) )
									.compareTo(
											format.parse( HttpServer
													.getDocroot().modTime(
															getPath() ) ) ) >= 0 )
					{
						responseLine = "HTTP/1.1 304 Not Modified\r\n";
						body = null;
					}
					else
					{

						responseLine = "HTTP/1.1 200 OK\r\n";
						headerLines += "Last-Modified: "
								+ HttpServer.getDocroot().modTime( getPath() )
								+ "\r\n";
						headerLines += "Content-Type: "
								+ HttpServer.getDocroot().ContentType(
										getPath() ) + "\r\n";
						headerLines += "Content-Length: " + body.length
								+ "\r\n";
					}
				} catch ( ParseException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				responseLine = "HTTP/1.1 404 Not Found\r\n";
				try
				{
					body = Files.readAllBytes( Paths.get( "404.html" ) );
					headerLines += "Content-Type: text/html\r\n";
					headerLines += "Content-Length: " + body.length + "\r\n";
				} catch ( IOException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			responseLine = "HTTP/1.1 501 Not Implemented\r\n";
		}

		headerLines += "\r\n";
		rLBytes = responseLine.getBytes();
		hBytes = headerLines.getBytes();

		HttpServer.getLog().println( "******RESPONSE******\n"
				+ responseLine + headerLines );

		byte[] out = null;
		if ( body != null )
		{
			out = new byte[rLBytes.length + hBytes.length + body.length];
			System.arraycopy( rLBytes, 0, out, 0, rLBytes.length );
			System.arraycopy( hBytes, 0, out, rLBytes.length, hBytes.length );
			System.arraycopy( body, 0, out, hBytes.length + rLBytes.length,
					body.length );
		}
		else
		{
			out = new byte[rLBytes.length + hBytes.length];
			System.arraycopy( rLBytes, 0, out, 0, rLBytes.length );
			System.arraycopy( hBytes, 0, out, rLBytes.length, hBytes.length );
		}
		return out;
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
		String s = requestLine + "\n";
		for ( Entry< String, String > entry : headers.entrySet() )
		{
			s += entry.getKey() + ": " + entry.getValue() + "\n";
		}
		return s;
	}

}