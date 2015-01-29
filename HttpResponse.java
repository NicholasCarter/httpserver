class HttpResponse
{
	HttpRequest request;
	int code;
	/*
	 * 1xx not needed
	 * 2xx request correct, recieved, and understood
	 * 304 file not changed since
	 * 4xx incorrect request
	 * 5xx correct request, but failure on server's part.
	 */
	String reason;
	
	public HttpResponse(HttpRequest request) {
		this.request = request;
	}
	
}