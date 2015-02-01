/*****************************************************************************
 * Log.java
 * 
 * Authors: Nicholas Carter 
 * 			Charles Fallert
 * 			Josh Hoiland 			
 *          Zack Smith
 *****************************************************************************/
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Log {
	private Writer out;

	/*****************************************************************************
	 * 
	 *****************************************************************************/
	public Log()
	{
		out = new PrintWriter( System.out, true );
	}

	/*****************************************************************************
	 * 
	 *****************************************************************************/
	public Log( String filename ) throws FileNotFoundException
	{
		try
		{
			out = new FileWriter( filename );

		} catch ( IOException e )
		{
			throw new FileNotFoundException( e.getMessage() );
		}
	}

	/*****************************************************************************
	 * 
	 *****************************************************************************/
	public void println( String s )
	{
		try
		{
			out.write( s + "\n" );
			out.flush();
		} catch ( IOException e )
		{
			System.out.println( "Couldn't write to log" );
		}

	}
}
