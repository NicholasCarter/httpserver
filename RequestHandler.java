/*****************************************************************************
 * RequestHandler.java
 * 
 * Authors: Nicholas Carter
 * 			Charles Fallert
 * 			Josh Hoiland
 *          Zack Smith
 *****************************************************************************/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

class RequestHandler implements Runnable {

	private final Socket socket;
	private final DataOutputStream out;
	private final BufferedReader in;

	/**************************************************************************
	 * This constructor initializes I/O for new connections
	 *************************************************************************/
	public RequestHandler( final Socket socket ) throws IOException
	{
		this.socket = socket;
		// Set timeout
		this.socket.setSoTimeout( HttpServer.TIMEOUT );
		// initialize I/O
		out = new DataOutputStream( socket.getOutputStream() );
		in = new BufferedReader(
				new InputStreamReader( socket.getInputStream() ) );

	}

	/**************************************************************************
	 * This method is invoked when the thread is started. It waits for requests,
	 * then builds a request object and sends a response
	 *************************************************************************/
	public void run()
	{
		try
		{
			String tmp = "";
			// Wait for a request
			while ( ( tmp = in.readLine() ) != null )
			{
				// Create request object, set requestLine
				final Request request = new Request();
				request.setRequestLine( tmp );
				try
				{
					// Read and set headers
					tmp = in.readLine();
					while ( tmp != null && !tmp.equals( "" ) )
					{
						request.getHeaders().put( tmp.split( ": " )[0],
								tmp.split( ": " )[1] );
						tmp = in.readLine();
					}
				} catch ( final IOException e )
				{
					// --Ignore--
					// Couldn't read headers
				}
				// If the request contains a body skip it
				if ( request.getHeaders().containsKey( "Content-Length" ) )
				{
					socket.getInputStream().skip(
							Long.parseLong( request.getHeaders().get(
									"Content-Length" ) ) );
				}
				// log request
				HttpServer.getLog().println( "***REQUEST*****\n" + request );
				// send response
				out.write( request.getResponse().toBytes() );
			}
			socket.close();
		} catch ( final SocketTimeoutException e )
		{
			// Close socket on timeout
			try
			{
				socket.close();
			} catch ( final IOException e1 )
			{
				// --Ignore--
				// Couldn't close socket after read error.
			}
		} catch ( final IOException e )
		{
			// --Ignore--
			// Connection Closed by client
		}
	}
}