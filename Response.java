import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Response {

	DocRoot root = HttpServer.getDocroot();
	private String path;
	private String headerLines = "";
	private String responseLine = "";
	private byte[] content;

	public Response( int code, String path ) throws SecurityException
	{
		content = null;
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

	private void build403()
	{
		responseLine = "HTTP/1.1 403 Not Found\r\n";
		try
		{
			content = Files.readAllBytes( Paths.get( path ) );
			headerLines += "Content-Type: text/html\r\n";
			headerLines += "Content-Length: " + content.length + "\r\n";
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	private void build404()
	{
		responseLine = "HTTP/1.1 404 Not Found\r\n";
		try
		{
			content = Files.readAllBytes( Paths.get( path ) );
			headerLines += "Content-Type: text/html\r\n";
			headerLines += "Content-Length: " + content.length + "\r\n";
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	private void build200()
	{
		responseLine = "HTTP/1.1 200 OK\r\n";
		content = root.getFile( path );
		headerLines += "Last-Modified: " + root.modTime( path ) + "\r\n";
		headerLines += "Content-Type: " + root.ContentType( path ) + "\r\n";
		headerLines += "Content-Length: " + content.length + "\r\n";

	}

	private void build304()
	{
		responseLine = "HTTP/1.1 304 Not Modified\r\n";
	}

	private void build501()
	{
		responseLine = "HTTP/1.1 501 Not Implemented\r\n";
	}

	public byte[] toBytes()
	{
		byte[] out = null;
		byte[] rLBytes = responseLine.getBytes();
		byte[] hBytes = headerLines.getBytes();

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

	public String toString()
	{
		return responseLine + headerLines;
	}
}
