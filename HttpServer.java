/******************************************************************************
 * HttpServer.java
 * 
 * Author: Josh Hoiland
 * 
 * 
 *****************************************************************************/
import java.io.File;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class HttpServer {
	private static DocRoot docroot;
	private static Log log;

	public HttpServer()
	{
		try
		{
			ServerSocket listenSocket = new ServerSocket( 9876 );
			// log = new HttpLog( "httplog.txt" );
			log = new Log();
			docroot = new DocRoot( new File( "root" ) );
			System.out.println( "HTTP server running..." );
			System.out.println( currentTime() );

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

	public static synchronized DocRoot getDocroot()
	{
		return docroot;
	}

	public static synchronized Log getLog()
	{
		return log;
	}

	public static synchronized String currentTime()
	{
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );
		DateFormat format = new SimpleDateFormat( "E, dd MMM yyyy HH:mm:ss z" );
		return format.format( new Date() );
	}

	public static void main( String argv[] )
	{
		new HttpServer();
	}
}
