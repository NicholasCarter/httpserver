import java.net.*;

class ConnectionHandler implements Runnable {
	private HttpSocket socket;
	private int i;

	public ConnectionHandler(HttpSocket socket, int i) {
		this.socket = socket;
		this.i = i;
	}

	public void run() {
		System.out.println("connection accepted " + i);
		try {
			try {
				while (true) {
					HttpRequest r = socket.recvRequest();
					System.out.print(r.toString());
					System.out.println("opcode: " + r.getOpcode());
					System.out.println("path: " + r.getPath());
					System.out.println("version: " + r.getVersion());
					System.out.println("host: " + r.getHeaders().get("Host"));
					System.out.println();
					HttpResponse response = new HttpResponse(r);
					socket.send(response);

				}
			} catch (SocketTimeoutException ste) {
				socket.close();
				System.out.println("connectionclosed -- timeout " + i);
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}