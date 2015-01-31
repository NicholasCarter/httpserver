import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpLog {
	public Writer out;

	public HttpLog()
	{
		out = new PrintWriter( System.out, true );
	}

	public HttpLog( String filename ) throws FileNotFoundException
	{
		try
		{
			out = new FileWriter( filename );

		} catch ( IOException e )
		{
			throw new FileNotFoundException( e.getMessage() );
		}
	}

	public void println( String s )
	{
		try
		{
			out.write( s + "\n" );
			out.flush();
		} catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
