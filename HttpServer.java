/******************************************************************************
 * HttpServer.java
 * 
 * Authors: Nicholas Carter
 * 			Charles Fallert
 * 			Josh Hoiland
 *          Zack Smith
 *****************************************************************************/
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.TimeZone;

class HttpServer {

	public static final int TIMEOUT = 20000;
	private static DocRoot docRoot;
	private static Log log;

	/**************************************************************************
	 * This constructor opens a port and listens. When a connection is made, a
	 * new thread is started to handle that connection
	 *************************************************************************/
	public HttpServer( final int port, final String docRoot,
			final String logFile )
	{
		try
		{
			setLog( logFile );
			setDocRoot( docRoot );
			String[] errorPages = { "404.html", "403.html", "501.html" };
			for ( int i = 0; i < errorPages.length; ++i )
			{
				if ( !new File( errorPages[i] ).exists() )
					throw new MissingResourceException( "Missing error pages",
							null, null );
			}
			@SuppressWarnings( "resource" )
			final ServerSocket listenSocket = new ServerSocket( 8080 );

			log.println( "**********************************************************************" );
			log.println( currentTime() );
			log.println( "HTTP server started." );
			log.println( "**********************************************************************" );
			log.println( "" );

			while ( true )
			{
				try
				{
					final Socket socket = listenSocket.accept();
					final Runnable connectionHandler = new RequestHandler(
							socket );
					final Thread t = new Thread( connectionHandler );
					t.start();
				}

				catch ( final IOException e )
				{
					log.println( "failed to connect to client" );
				}
			}
		} catch ( final IOException e )
		{
			System.out.println( "Failed to bind to port 8080" );
		} catch ( final MissingResourceException e )
		{
			System.out.println( e.getMessage() );
		}
	}

	/**************************************************************************
	 * This method provides public synchronized access to the current time
	 * formatted in the HTTP standard
	 *************************************************************************/
	public static synchronized String currentTime()
	{
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );
		final DateFormat format = new SimpleDateFormat(
				"E, dd MMM yyyy HH:mm:ss z" );
		return format.format( new Date() );
	}

	/**************************************************************************
	 * This method provides public synchronized acces to the Document Root
	 *************************************************************************/
	public static synchronized DocRoot getDocRoot()
	{
		return docRoot;
	}

	/**************************************************************************
	 * This method allows static setting of the DocRoot field
	 *************************************************************************/
	private static void setDocRoot( final String dir )
	{
		docRoot = new DocRoot( new File( dir ) );

	}

	/**************************************************************************
	 * This method provides public synchronized access to the Log
	 *************************************************************************/
	public static synchronized Log getLog()
	{
		return log;
	}

	/**************************************************************************
	 * This method allows static setting of the log field
	 *************************************************************************/
	private static void setLog( final String filename )
	{
		log = new Log( filename );
	}

	/**************************************************************************
	 * MAIN
	 *************************************************************************/
	public static void main( final String argv[] )
	{
		if ( argv.length == 3 )
		{
			new HttpServer( Integer.parseInt( argv[0] ), argv[1], argv[2] );
		}
		else if ( argv.length == 2 )
		{
			new HttpServer( Integer.parseInt( argv[0] ), argv[1], null );
		}
		else
		{
			System.out
					.println( "Usage: \njava HttpServer <port> <Document Root> <Log File> \nor\njava HttpServer <port> <Document Root>" );
		}

	}
}
