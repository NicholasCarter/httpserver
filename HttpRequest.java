import java.util.*;

class HttpRequest
{
	public String opcode;
	public String path;
	public String version;
	public HashMap <String, String> headers;
	
	public HttpRequest(String s)
	{
		String[] lines = s.split("\r\n");
		
		String[] requestline = lines[0].split(" ");
		opcode = requestline[0];
		path = requestline[1];
		version = requestline[2];
		
		headers = new HashMap<String, String>();
		for(int i = 1; i < lines.length; ++i)
		{
			String[] st = lines[i].split(": ");
			headers.put(st[0], st[1]);
		}
		
	}
}