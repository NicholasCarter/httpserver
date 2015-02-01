/******************************************************************************
 * HttpServer.java
 * 
 * Authors: Nicholas Carter 
 * 			Charles Fallert
 * 			Josh Hoiland 			
 *          Zack Smith
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

	/*****************************************************************************
	 * 
	 *****************************************************************************/
	public HttpServer()
	{
		try
		{
			// bind to port 8080
			@SuppressWarnings( "resource" )
			ServerSocket listenSocket = new ServerSocket( 8080 );

			// log to file
			// log = new HttpLog( "httplog.txt" );

			// log to console
			log = new Log();

			// set docroot
			docroot = new DocRoot( new File( "root" ) );

			log.println( currentTime() );
			log.println( "HTTP server running..." );

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
					log.println( "failed to connect to client" );
				}
			}
		} catch ( Exception e )
		{
			log.println( "Failed to bind to port 8080" );
		}
	}

	/*****************************************************************************
	 * 
	 *****************************************************************************/
	public static synchronized DocRoot getDocroot()
	{
		return docroot;
	}

	/*****************************************************************************
	 * 
	 *****************************************************************************/
	public static synchronized Log getLog()
	{
		return log;
	}

	/*****************************************************************************
	 * 
	 *****************************************************************************/
	public static synchronized String currentTime()
	{
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );
		DateFormat format = new SimpleDateFormat( "E, dd MMM yyyy HH:mm:ss z" );
		return format.format( new Date() );
	}

	/*****************************************************************************
	 * MAIN
	 *****************************************************************************/
	public static void main( String argv[] )
	{
		new HttpServer();
	}
}
