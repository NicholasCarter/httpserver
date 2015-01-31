import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class HttpDocroot {

	// File representation of DocRoot
	File root;
	// Absolute,Canonical path String of DocRoot
	String path;

	public HttpDocroot( File docroot )
	{
		if ( !docroot.isDirectory() )
		{
			throw new IllegalArgumentException( "Docroot must be a Directory!" );
		}
		root = docroot;
		path = root.getAbsolutePath();
	}

	public byte[] getFile( String f )
	{
		byte[] data = null;
		File file = new File( path + f );
		if ( file.exists() )
		{
			try
			{
				data = Files.readAllBytes( Paths.get( file.getAbsolutePath() ) );
			} catch ( IOException e )
			{
				e.printStackTrace();
			}
		}
		return data;
	}

	public String modTime( String f )
	{
		File file = new File( path + f );
		long time = file.lastModified();
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );
		Date date = new Date( time );
		// day of the week(3 letter), day of the month, month, year,
		// hour:minutes:seconds GMT
		DateFormat format = new SimpleDateFormat( "E, dd MMM yyyy HH:mm:ss z" );
		String modifiedTime = format.format( date );

		return modifiedTime;
	}

	public String ContentType( String f )
	{
		int i = f.lastIndexOf( "." );
		String ext = f.substring( i + 1 );
		if ( ext.equals( "jpg" ) )
		{
			return "image/" + ext;
		}
		else if ( ext.equals( "html" ) )
		{
			return "text/" + ext;
		}
		else
			return "";
	}
}