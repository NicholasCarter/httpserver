import java.util.*;

class Request {
	private String opcode;
	private String path;
	private String version;
	private HashMap< String, String > headers;

	public Request( ArrayList< String > request )
	{
	}

	public String getOpcode()
	{
		return opcode;
	}

	public String getPath()
	{
		return path;
	}

	public String getVersion()
	{
		return version;
	}

	public HashMap< String, String > getHeaders()
	{
		return headers;
	}

	public String getResponse()
	{
		return null;
	}

}