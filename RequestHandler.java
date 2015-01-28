import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

class RequestHandler implements Runnable {
	private int timeout = 20000;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	public RequestHandler( Socket socket )
	{
		this.socket = socket;

		// Set timeout
		try
		{
			this.socket.setSoTimeout( timeout );
			// initialize I/O
			out = new PrintWriter( socket.getOutputStream(), true );
			in = new BufferedReader( new InputStreamReader(
					socket.getInputStream() ) );

		} catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public String getInfo()
	{
		return socket.toString();
	}

	public void run()
	{
		try
		{
			try
			{
				// continually get requests while connection is open
				while ( true )
				{
					// read from in until blank line
					ArrayList< String > lines = new ArrayList< String >();

					String tmp = "";
					do
					{
						tmp = in.readLine();
						lines.add( tmp );
					} while ( tmp.equals( "" ) );

					// instantiate request
					Request request = new Request( lines );
					// if there is a message body read content length bytes and
					// skip them because we are only supporting GET requests and
					// don't care
					if ( request.getHeaders().containsKey( "content-length" ) )
					{
						long count = Long.parseLong( request.getHeaders().get(
								"content-length" ) );
						socket.getInputStream().skip( count );
					}
					out.print( request.getResponse() );

				}
			} catch ( SocketTimeoutException ste )
			{
				socket.close();
				System.out.println( "connectionclosed -- timeout " );
				System.out.println();
			}
		} catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
	}
}