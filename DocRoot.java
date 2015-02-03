/*****************************************************************************
 * DocRoot.java
 * 
 * Authors: Nicholas Carter 
 * 			Charles Fallert
 * 			Josh Hoiland 			
 *          Zack Smith
 *****************************************************************************/
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class DocRoot {

	// File representation of DocRoot
	private File root;
	// Absolute,Canonical path String of DocRoot
	private String path;

	/*****************************************************************************
	 * This constructor makes sure the supplied File is a directory and then
	 * gets its absolute path
	 *****************************************************************************/
	public DocRoot( File dir )
	{
		if ( !dir.isDirectory() )
		{
			throw new IllegalArgumentException( "Docroot must be a Directory!" );
		}
		root = dir;
		path = root.getAbsolutePath();
	}

	/*****************************************************************************
	 * This method returns the specified file in a byte array
	 *****************************************************************************/
	public byte[] getFile( String f )
	{
		if ( f.contains( "../" ) )
		{
			throw new SecurityException( "Forbidden" );
		}
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

	/*****************************************************************************
	 * This method checks if the specified file exists with in the doc root
	 * directory
	 *****************************************************************************/
	public boolean exists( String f )
	{
		if ( f.contains( "../" ) )
		{
			throw new SecurityException( "Forbidden" );
		}
		if ( new File( path + f ).exists() )
			return true;
		return false;
	}

	/*****************************************************************************
	 * This method returns the last modified time of the specified file in HTTP
	 * format
	 *****************************************************************************/
	public String modTime( String f )
	{
		if ( f.contains( "../" ) )
		{
			throw new SecurityException( "Forbidden" );
		}
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

	/*****************************************************************************
	 * This method returns an MIME type for the specified file. Defaults to
	 * plain text
	 *****************************************************************************/
	public String contentType( String f )
	{
		try
		{
			return Files.probeContentType( Paths.get( path + f ) );
		} catch ( IOException e )
		{
			return "text/plain";
		}
	}
}