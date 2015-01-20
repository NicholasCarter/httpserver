import java.io.File;
import java.net.*;

class HttpServer {
	public static void main(String argv[]) {
		try {
			@SuppressWarnings("resource")
			// HttpDocroot docroot = new HttpDocroot(new File("docroot"));
			ServerSocket listenSocket = new ServerSocket(9876);
			HttpSocket socket = null;
			int i = 0;
			while (true) {
				try {
					socket = new HttpSocket(listenSocket.accept());
					Runnable connectionHandler = new ConnectionHandler(socket,
							i++);
					Thread t = new Thread(connectionHandler);
					t.start();
				}

				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
