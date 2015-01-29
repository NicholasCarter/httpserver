/******************************************************************************
 * HttpServer.java
 * 
 * Author: Josh Hoiland
 * 
 * 
 *****************************************************************************/
import java.net.*;

class HttpServer {
	private static HttpDocroot docroot;
	private static HttpLog log;

	public HttpServer()
	{
		try
		{
			ServerSocket listenSocket = new ServerSocket( 9876 );

			System.out.println( "HTTP server running..." );
			while ( true )
			{
				try
				{
					Socket socket = listenSocket.accept();
					Runnable connectionHandler = new RequestHandler( socket );
					Thread t = new Thread( connectionHandler );
					t.start();

				}

				catch ( Exception e )
				{
					e.printStackTrace();
				}
			}
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static synchronized HttpDocroot getDocroot()
	{
		return docroot;
	}

	public static synchronized HttpLog getLog()
	{
		return log;
	}

	public static void main( String argv[] )
	{
		new HttpServer();
	}
}
