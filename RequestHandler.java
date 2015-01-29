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
			System.out.println( socket.getPort() + ": "
					+ "Connection Established." );
			String tmp = "";

			System.out.println( socket.getPort() + ": "
					+ "waiting for request." );
			while ( ( tmp = in.readLine() ) != null )
			{
				System.out.println( socket.getPort() + ": "
						+ "Request Received." );
				Request request = new Request();
				request.setRequestLine( tmp );
				try
				{
					tmp = in.readLine();
					while ( tmp != null && !tmp.equals( "" ) )
					{
						System.out.println( socket.getPort() + ": "
								+ "Header Received." );
						request.getHeaders().put( tmp.split( ": " )[0],
								tmp.split( ": " )[1] );
						tmp = in.readLine();
					}
				} catch ( Exception e )
				{
					e.printStackTrace();
				}
				if ( request.getHeaders().containsKey( "Content-Length" ) )
				{
					System.out.println( socket.getPort() + ": "
							+ "Body Received--unsupported." );
					socket.getInputStream().skip(
							Long.parseLong( request.getHeaders().get(
									"Content-Length" ) ) );
				}
				System.out.println( socket.getPort() + ": "
						+ request.toString() );
				out.println( request.getResponse() );
				System.out.println( socket.getPort() + ": " + "Response Sent" );
			}
			System.out.println( socket.getPort() + ": "
					+ "Connection Closed by client" );
			socket.close();
		} catch ( SocketTimeoutException e )
		{
			System.out.print( socket.getPort() + ": " + "Connection Timedout." );
			try
			{
				socket.close();
			} catch ( IOException e1 )
			{
				System.out.print( socket.getPort() + ": " );
				e1.printStackTrace();
			}
		} catch ( NumberFormatException e )
		{
			e.printStackTrace();
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
}