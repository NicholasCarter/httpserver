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

	public HttpServer()
	{
		try
		{
			ServerSocket listenSocket = new ServerSocket( 9876 );

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
					System.out.println( e.getMessage() );
				}
			}
		} catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
	}

	public static synchronized HttpDocroot getDocroot()
	{
		return docroot;
	}

	public static void main( String argv[] )
	{
		new HttpServer();
	}
}
