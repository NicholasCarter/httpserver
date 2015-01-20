import java.io.*;
import java.net.*;

class HttpServer
{
    public static void main(String argv[]) 
	{
		try
		{
			ServerSocket listenSocket = new ServerSocket(9876);
			
			while(true)
			{
				try
				{
					HttpSocket socket = new HttpSocket( listenSocket.accept() );
					System.out.println("connection accepted");
					StringBuilder sb = new StringBuilder();
					
					
					
						socket.close();
						System.out.println("connectionclosed");
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