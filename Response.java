/*****************************************************************************
 * Response.java
 * 
 * Authors: Nicholas Carter
 * 			Charles Fallert
 * 			Josh Hoiland
 *          Zack Smith
 *****************************************************************************/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Response {

	private final String path;

	private String headerLines = "";
	private String responseLine = "";
	private byte[] content;
	public int code;

	/*****************************************************************************
	 * This constructor calls for a certain response to be built based on the
	 * code parameter.
	 *****************************************************************************/
	public Response( final int code, final String path )
			throws SecurityException
	{
		content = null;
		this.code = code;
		headerLines += "Date: " + HttpServer.currentTime() + "\r\n";
		this.path = path;
		switch ( code )
		{
		case 403:
		{
			build403();
			break;
		}
		case 404:
		{
			build404();
			break;
		}
		case 200:
		{
			build200();
			break;
		}
		case 501:
		{
			build501();
			break;
		}
		case 304:
		{
			build304();
			break;
		}
		default:
			break;
		}
		headerLines += "\r\n";
	}

	/*****************************************************************************
	 * Builds a 403 Forbidden Response
	 *****************************************************************************/
	private void build403()
	{
		responseLine = "HTTP/1.1 403 Forbidden\r\n";
		try
		{
			content = Files.readAllBytes( Paths.get( path ) );
			headerLines += "Content-Type: text/html\r\n";
			headerLines += "Content-Length: " + content.length + "\r\n";
		} catch ( final IOException e )
		{
			e.printStackTrace();
		}
	}

	/*****************************************************************************
	 * Builds a 404 Not Found Response
	 *****************************************************************************/
	private void build404()
	{
		responseLine = "HTTP/1.1 404 Not Found\r\n";
		try
		{
			content = Files.readAllBytes( Paths.get( path ) );
			headerLines += "Content-Type: text/html\r\n";
			headerLines += "Content-Length: " + content.length + "\r\n";
		} catch ( final IOException e )
		{
			e.printStackTrace();
		}
	}

	/*****************************************************************************
	 * Builds a 200 OK Response
	 *****************************************************************************/
	private void build200()
	{
		responseLine = "HTTP/1.1 200 OK\r\n";
		content = HttpServer.getDocRoot().getFile( path );
		headerLines += "Last-Modified: "
				+ HttpServer.getDocRoot().modTime( path ) + "\r\n";
		headerLines += "Content-Type: "
				+ HttpServer.getDocRoot().contentType( path ) + "\r\n";
		headerLines += "Content-Length: " + content.length + "\r\n";

	}

	/*****************************************************************************
	 * Builds a 304 Not Modified Response
	 *****************************************************************************/
	private void build304()
	{
		responseLine = "HTTP/1.1 304 Not Modified\r\n";
	}

	/*****************************************************************************
	 * Builds a 501 Not Implemented Response
	 *****************************************************************************/
	private void build501()
	{
		responseLine = "HTTP/1.1 501 Not Implemented\r\n";
		try
		{
			content = Files.readAllBytes( Paths.get( path ) );
			headerLines += "Content-Type: text/html\r\n";
			headerLines += "Content-Length: " + content.length + "\r\n";
		} catch ( final IOException e )
		{
			e.printStackTrace();
		}
	}

	/*****************************************************************************
	 * Concatenates all response components into a single byte array
	 *****************************************************************************/
	public byte[] toBytes()
	{
		byte[] out = null;
		final byte[] rLBytes = responseLine.getBytes();
		final byte[] hBytes = headerLines.getBytes();

		if ( content != null )
		{
			out = new byte[rLBytes.length + hBytes.length + content.length];
			System.arraycopy( rLBytes, 0, out, 0, rLBytes.length );
			System.arraycopy( hBytes, 0, out, rLBytes.length, hBytes.length );
			System.arraycopy( content, 0, out, hBytes.length + rLBytes.length,
					content.length );
		}
		else
		{
			out = new byte[rLBytes.length + hBytes.length];
			System.arraycopy( rLBytes, 0, out, 0, rLBytes.length );
			System.arraycopy( hBytes, 0, out, rLBytes.length, hBytes.length );
		}

		return out;
	}

	/*****************************************************************************
	 * Returns a string representation of the responseline and headers
	 *****************************************************************************/
	public String toString()
	{
		return responseLine + headerLines;
	}
}
