/*****************************************************************************
 * Request.java
 * 
 * Authors: Nicholas Carter
 * 			Charles Fallert
 * 			Josh Hoiland
 *          Zack Smith
 *****************************************************************************/
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

class Request {

	private final Log log = HttpServer.getLog();
	private final DocRoot root = HttpServer.getDocRoot();

	private String requestLine;
	private final HashMap< String, String > headers;

	/*****************************************************************************
	 * This constructor initializes the headers map
	 *****************************************************************************/
	public Request()
	{
		requestLine = "";
		headers = new HashMap< String, String >();
	}

	/*****************************************************************************
	 * This method determines how to respond to this request
	 *****************************************************************************/
	public Response getResponse()
	{
		if ( requestLine == null )
			throw new IllegalStateException(
					"Unable to get response; request line is null." );

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
			} catch ( final SecurityException e )
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

	/*****************************************************************************
	 * This method parses the request line and returns the opcode
	 *****************************************************************************/
	private String getOpcode()
	{
		return requestLine.split( " " )[0];
	}

	/*****************************************************************************
	 * This method parses the request line and returns the requested path
	 *****************************************************************************/
	private String getPath()
	{
		String s = requestLine.split( " " )[1];
		if ( s.equals( "/" ) )
			s = "/index.html";

		return s;
	}

	/*****************************************************************************
	 * This method provides public access to the headers map
	 *****************************************************************************/
	public HashMap< String, String > getHeaders()
	{
		return headers;
	}

	/*****************************************************************************
	 * This method sets the requestLine field
	 *****************************************************************************/
	public void setRequestLine( final String requestLine )
	{
		this.requestLine = requestLine;
	}

	/*****************************************************************************
	 * This method returns a String representation of the entire request
	 *****************************************************************************/
	public String toString()
	{
		String s = requestLine + "\n";
		for ( final Entry< String, String > entry : headers.entrySet() )
		{
			s += entry.getKey() + ": " + entry.getValue() + "\n";
		}
		return s;
	}

	/*****************************************************************************
	 * This method returns true if the requested file has been modified since
	 * the If-Modified-Since time or if there is no If-Modified-Since header
	 *****************************************************************************/
	private boolean isModified()
	{
		final DateFormat dateFormat = new SimpleDateFormat(
				"E, dd MMM yyyy HH:mm:ss z" );
		if ( headers.containsKey( "If-Modified-Since" ) )
		{
			try
			{
				final Date ifDate = dateFormat.parse( headers
						.get( "If-Modified-Since" ) );
				final Date actual = dateFormat
						.parse( root.modTime( getPath() ) );
				if ( actual.compareTo( ifDate ) <= 0 )
					return false;
			} catch ( final ParseException e )
			{
				// Bad date format, return true
			}
		}
		return true;

	}
}