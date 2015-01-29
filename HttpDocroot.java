import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
				System.err.print("Directory Path Error");
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
			System.err.print("File Path Error");
		}
		Path locale = Paths.get(dir);
		
		//If path is within DocRoot, or any subdirectory of DocRoot, read in its bytes
		if(dir.startsWith(path)){
			try {
				data = Files.readAllBytes(locale);
			} catch (IOException e) {
				System.err.print("File Unreadable");
			}
		}
		
		//Returns the byte array of the file if it is appropriate, returns null otherwise.
		return data;
	}
}