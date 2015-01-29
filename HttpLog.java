import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpLog {
	public PrintWriter out;

	public HttpLog()
	{
		out = new PrintWriter( System.out, true );
	}

	public HttpLog( String filename ) throws FileNotFoundException
	{
		try
		{
			out = new PrintWriter( new FileOutputStream( filename, true ) );
		} catch ( FileNotFoundException e )
		{
			throw new FileNotFoundException( e.getMessage() );
		}
	}

	public void write( String msg, String socketInfo )
	{
		Date date = new Date();
		DateFormat format = new SimpleDateFormat( "EEE, d MMM yyyy HH:mm:ss Z" );
		String timestamp = format.format( date );
		out.println( timestamp + "::" + socketInfo + "::" + msg );

	}
}
