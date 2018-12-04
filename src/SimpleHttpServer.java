import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;

public class SimpleHttpServer {
	
	public static InputFileConverter fc;

	public static void main(String[] args) throws Exception {
	  
		if (args.length == 1) {
		
			fc = new InputFileConverter(args[0]);
			System.out.println(fc.get_all_files()[0]);
		} else {
			System.out.println("Invalid Command Line Argument");
			return;
		}
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8003), 0);
		server.createContext("/info", new InfoHandler());
		server.createContext("/get", new GetHandler(fc.get_all_files()[0]));
		server.createContext("/get2", new GetHandler(fc.get_all_files()[1]));
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static class InfoHandler implements HttpHandler {
		
		String response;
		
		public InfoHandler() {
			int n_files = fc.get_all_files().length; 
			
			for (int i=0; i < n_files; i++) {
				response += "File " + i + ": " + fc.get_all_files()[i] + "\n";
			}
			
		}

		public void handle(HttpExchange t) throws IOException {
			//String response = "Use /get to download a PDF";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	static class GetHandler implements HttpHandler {
		
		String this_file;
		
		public GetHandler(String file_path) {
			this_file = file_path;
		}
		
		public String get_this_file() {
			return this_file;
		}

		public void handle(HttpExchange t) throws IOException {

			// add the required response header for a PDF file
			Headers h = t.getResponseHeaders();
			h.add("Content-Type", "application/pdf");

			// a PDF (you provide your own!)
			// File file = new File ("/home/mitchell/eclipse-workspace/DummyProject/src/python_sockets.pdf");
			File file = new File (this.get_this_file());
			byte [] bytearray  = new byte [(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(bytearray, 0, bytearray.length);
			bis.close();

			// ok, we are ready to send the response.
			t.sendResponseHeaders(200, file.length());
			OutputStream os = t.getResponseBody();
			os.write(bytearray,0,bytearray.length);
			os.close();
		}
	}
}