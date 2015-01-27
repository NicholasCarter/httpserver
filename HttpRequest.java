import java.util.*;

class HttpRequest {
	private String fullRequest;
	private String opcode;
	private String path;
	private String version;
	private HashMap<String, String> headers;

	public HttpRequest(String s) {
		fullRequest = s; 
		String[] lines = s.split("\r\n");

		String[] requestline = lines[0].split(" ");
		opcode = requestline[0];
		path = requestline[1];
		version = requestline[2];

		headers = new HashMap<String, String>();
		for (int i = 1; i < lines.length; ++i) {
			String[] st = lines[i].split(": ");
			headers.put(st[0], st[1]);
		}
	}

	public String toString() {
		return fullRequest;
	}

	public String getOpcode() {
		return opcode;
	}

	public String getPath() {
		return path;
	}

	public String getVersion() {
		return version;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

}