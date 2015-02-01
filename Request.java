import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

class Request {
	private String requestLine;
	private HashMap< String, String > headers;
	Log log = HttpServer.getLog();

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
		String s = requestLine.split( " " )[1];

		return s;
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
		Response response = null;
		DocRoot root = HttpServer.getDocroot();

		if ( getOpcode().equals( "GET" ) )
		{
			if ( getPath().contains( "../" ) )
			{
				response = new Response( 403, "403.html" );

			}
			else if ( !root.exists( getPath() ) )
			{
				response = new Response( 404, "404.html" );

			}
			else
			{
				if ( !isModified() )
				{
					response = new Response( 304, null );
				}
				else
				{
					response = new Response( 200, getPath() );

				}
			}
		}
		else
		{
			response = new Response( 501, null );
		}

		log.println( "******RESPONSE******\n" + response.toString() );

		return response.toBytes();
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

	private boolean isModified()
	{

		DateFormat dateFormat = new SimpleDateFormat(
				"E, dd MMM yyyy HH:mm:ss z" );
		try
		{
			return ( headers.containsKey( "If-Modified-Since" ) && dateFormat
					.parse( ( headers.get( "If-Modified-Since" ) ) ).compareTo(
							dateFormat.parse( HttpServer.getDocroot().modTime(
									getPath() ) ) ) < 0 );
		} catch ( ParseException e )
		{
			e.printStackTrace();
		}
		return false;
	}
}