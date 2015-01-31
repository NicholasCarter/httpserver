import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

class RequestHandler implements Runnable {
	private int timeout = 20000;
	private Socket socket;
	private DataOutputStream out;
	private BufferedReader in;

	public RequestHandler( Socket socket )
	{
		this.socket = socket;

		// Set timeout
		try
		{
			this.socket.setSoTimeout( timeout );
			// initialize I/O
			out = new DataOutputStream( socket.getOutputStream() );
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
			String tmp = "";

			while ( ( tmp = in.readLine() ) != null )
			{
				Request request = new Request();
				request.setRequestLine( tmp );
				try
				{
					tmp = in.readLine();
					while ( tmp != null && !tmp.equals( "" ) )
					{
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
					socket.getInputStream().skip(
							Long.parseLong( request.getHeaders().get(
									"Content-Length" ) ) );
				}
				HttpServer.getLog().println( "***REQUEST*****\n" + request );
				out.write( request.getResponse() );
			}
			socket.close();
		} catch ( SocketTimeoutException e )
		{
			try
			{
				socket.close();
			} catch ( IOException e1 )
			{
				e1.printStackTrace();
			}
		} catch ( NumberFormatException e )
		{
			e.printStackTrace();
		} catch ( IOException e )
		{
		}
	}
}