import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

class RequestHandler implements Runnable {
	private int timeout = 20000;
	private Socket socket;
	private DataOutputStream out;
	private BufferedReader in;
	private Log log = HttpServer.getLog();

	public RequestHandler( Socket socket )
	{
		this.socket = socket;
		try
		{
			// Set timeout
			this.socket.setSoTimeout( timeout );
			// initialize I/O
			out = new DataOutputStream( socket.getOutputStream() );
			in = new BufferedReader( new InputStreamReader(
					socket.getInputStream() ) );

		} catch ( IOException e )
		{
			// couldn't initialze Request handler
		}
	}

	public void run()
	{
		try
		{
			String tmp = "";
			// Wait for a request
			while ( ( tmp = in.readLine() ) != null )
			{
				// Create request object, set requestLine
				Request request = new Request();
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
				} catch ( Exception e )
				{
					// Couldn't read in headers
				}
				// If the request contains a body skip ip
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
		} catch ( SocketTimeoutException e )
		{
			log.println( "Socket Read Timeout" );
			try
			{
				socket.close();
			} catch ( IOException e1 )
			{
				log.println( "Couldn't close Socket after Read Timeout" );
			}
		} catch ( IOException e )
		{
			log.println( "Connection Close by client" );
		}
	}
}