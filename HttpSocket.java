import java.io.*;
import java.net.*;

class HttpSocket{
		private Socket socket;
		private BufferedInputStream dataIn;
		private BufferedReader textIn;
		private DataOutputStream out;
		
		public JHSocket(String address, int port) throws Exception
		{
			this( new Socket( address, port ) );
		}
		public JHSocket(Socket s) throws Exception
		{
			socket = s;
			dataIn = new BufferedInputStream(socket.getInputStream());
			textIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out= new DataOutputStream(socket.getOutputStream());
		}
		
		public  void send(String s) throws IOException
		{
			out.writeBytes( s + "\n" );
		}
		
		public void send(int i) throws IOException
		{
			out.writeByte(i);
		}
		
		public  void send(byte[] b) throws IOException
		{
			out.write( b);
		}
		
		public  void send(byte[] b, int off, int len) throws IOException
		{
			out.write( b, off, len);
		}
		
		public  String recvText() throws IOException
		{
			return textIn.readLine();
		}
		
		public char recvChar() throws IOException
		{
			return textIn.read();
		}
		
		public  int recvBytes(byte[] b) throws IOException
		{
			return dataIn.read(b);
		}
		
		public int recv() throws IOException
		{
			return dataIn.read();
		}
		
		public void close() throws IOException
		{
			socket.close();
		}
	}