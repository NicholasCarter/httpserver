import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class HttpDocroot {
	
	//File representation of DocRoot
	File root;
	//Absolute,Canonical path String of DocRoot
	String path;
	
	public HttpDocroot(File docroot) {
		if (!docroot.isDirectory()) {
			throw new IllegalArgumentException("Docroot must be a Directory!");
		}
		else{
			root = docroot;
			try {
				path = root.getCanonicalPath() + File.separator;
			} catch (IOException e) {
				throw new SecurityException("Directory Path Error");
			}
		}

	}

	public byte[] getFile(File f) {
		byte[] data = null;
		String dir = null;
		
		//Specify location of file
		try {
			dir = f.getCanonicalPath();
		} catch (IOException e) {
			throw new SecurityException("File Path Error");
		}
		Path locale = Paths.get(dir);
		
		//If path is within DocRoot, or any subdirectory of DocRoot, read in its bytes
		if(dir.startsWith(path)){
			try {
				data = Files.readAllBytes(locale);
			} catch (IOException e) {
				throw new SecurityException("File Unreadable");
			}
		}
		
		//Returns the byte array of the file if it is appropriate, returns null otherwise.
		return data;
	}
	
	public String modTime(File f){
		
		long time = f.lastModified();
		
		Date date = new Date(time);
		//day of the week(3 letter), day of the month, month, year, hour:minutes:seconds GMT
		DateFormat format = new SimpleDateFormat("E:d:MMM:yyyy:HH:mm:ss:zzz");
		String modifiedTime = format.format(date);
		
		return modifiedTime;
	}
		
	
}