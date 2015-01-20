import java.io.File;

class HttpDocroot {
	public HttpDocroot(File docroot) {
		if (!docroot.isDirectory()) {
			throw new IllegalArgumentException("docroot must be a directory");
		}

	}

	public byte[] getFile(File f) {
		return null;
	}
}