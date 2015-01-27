class TestClient
{
	public static void main(String argv[]) throws Exception
	{
		HttpSocket sock = new HttpSocket("127.0.0.1",9876);
		
		sock.send("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n");
		sock.close();
		
	}
	
}