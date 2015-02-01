import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

class Request {
	private String requestLine;
	private HashMap< String, String > headers;
	Log log = HttpServer.getLog();

	DocRoot root = HttpServer.getDocroot();

	public Request()
	{
		headers = new HashMap< String, String >();
	}

	public Response getResponse()
	{
		Response response = null;

		// If request was GET....
		if ( getOpcode().equals( "GET" ) )
		{
			try
			{
				// if the requested file doesn't exit in the docroot, 404
				if ( !root.exists( getPath() ) )
				{
					response = new Response( 404, "404.html" );

				}
				// If the request is for a valid file...
				else
				{
					// ...and hasn't been modified, 304
					if ( !isModified() )
					{
						response = new Response( 304, null );
					}
					// ...but has been modified, 200
					else
					{
						response = new Response( 200, getPath() );

					}
				}
			} catch ( SecurityException e )
			{
				// Tried to access a forbidden file
				response = new Response( 403, "403.html" );
			}

		}
		// Request was something other than GET, 501
		else
		{
			response = new Response( 501, null );
		}

		log.println( "******RESPONSE******\n" + response.toString() );

		return response;
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
		if ( headers.containsKey( "If-Modified-Since" ) )
		{
			try
			{
				Date ifDate = dateFormat.parse( headers
						.get( "If-Modified-Since" ) );
				Date actual = dateFormat.parse( root.modTime( getPath() ) );
				if ( actual.compareTo( ifDate ) > 0 )
					return true;
			} catch ( ParseException e )
			{
				e.printStackTrace();
			}
		}
		return false;

	}
}