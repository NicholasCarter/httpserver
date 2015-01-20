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