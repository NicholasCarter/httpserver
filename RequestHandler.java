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
		HttpServer.getLog().println( "CONNECT: " + socket );
		HttpServer.getLog().println( "" );
		try
		{
			String tmp = "";
			// Wait for a request
			while ( ( tmp = in.readLine() ) != null && !tmp.equals( "" ))
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
						String[] header = tmp.split( ": ", 2 );
						if ( header.length == 2 )
						{
							request.getHeaders().put( tmp.split( ": ", 2 )[0],
									tmp.split( ": ", 2 )[1] );
						}
						else
							request.getHeaders().put( tmp.split( ": ", 2 )[0],
									"" );
						tmp = in.readLine();
					}
				} catch ( final IOException e )
				{
					// --Ignore--
					// Couldn't read headers
				}
				// log request
				HttpServer.getLog().println(
						"REQUEST: " + socket + "\n" + request );

				final Response response = request.getResponse();

				HttpServer.getLog().println(
						"RESPONSE: " + socket + "\n" + response );

				// send response
				final byte[] responsebytes = response.toBytes();
				for ( int i = 0; i < responsebytes.length; ++i )
				{
					out.writeByte( responsebytes[i] );
				}
				// If Connection: close or 501, close the connection
				if ( response.code == 501
						|| ( request.getHeaders().containsKey( "Connection" ) && request
								.getHeaders().get( "Connection" )
								.equals( "close" ) ) )
				{
					break;
				}

			}
			HttpServer.getLog().println( "DISCONNECT: " + socket );
			HttpServer.getLog().println( "" );
			socket.close();
		} catch ( final SocketTimeoutException e )
		{
			HttpServer.getLog().println( "TIMEOUT: " + socket );
			HttpServer.getLog().println( "" );
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
			HttpServer.getLog().println( "CONNECTION LOST: " + socket );
			HttpServer.getLog().println( "" );
		}
	}
}