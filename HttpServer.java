import java.io.*;
import java.net.*;

class HttpServer
{
    public static void main(String argv[]) 
	{
		try
		{
			ServerSocket listenSocket = new ServerSocket(9876);
			HttpSocket socket = null;
			while(true)
			{
				try
				{
					socket = new HttpSocket( listenSocket.accept() );
					System.out.println("connection accepted");
					
					HttpRequest r = socket.recvRequest();
					System.out.println("opcode: " + r.opcode);
					System.out.println("path: " + r.path);
					System.out.println("version: " + r.version);
					System.out.println("host: " + r.headers.get("Host"));
					
					
					
					
					
					
				}
				catch(SocketTimeoutException ste)
				{
					socket.close();
					System.out.println("connectionclosed -- timeout");
					System.out.println();
				}
				catch( Exception e )
				{
					System.out.println( e.getMessage() );
				}
			}
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
	}	
}