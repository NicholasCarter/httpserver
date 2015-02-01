/*****************************************************************************
 * Log.java
 * 
 * Authors: Nicholas Carter
 * 			Charles Fallert
 * 			Josh Hoiland
 *          Zack Smith
 *****************************************************************************/
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Log {
	private Writer out;

	/*****************************************************************************
	 * This constructor initializes the log. If filename is null or can't be
	 * used System.out is used instead
	 *****************************************************************************/
	public Log( final String filename )
	{
		try
		{
			out = new FileWriter( filename );
		} catch ( IOException | NullPointerException e )
		{
			out = new PrintWriter( System.out );
		}

	}

	/*****************************************************************************
	 * This method prints a string followed by a linefeed to the log
	 *****************************************************************************/
	public void println( final String s )
	{
		try
		{
			out.write( s + "\n" );
			out.flush();
		} catch ( final IOException e )
		{
			System.out.println( "Couldn't write to log" );
		}

	}
}
