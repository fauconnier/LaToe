package org.melodi.tools.fuzzymatcher.server;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.util.Date;



public class FuzzyMatcher_Server {
	private int port;
	private boolean listening = true;
	
	public FuzzyMatcher_Server() {
		this.port = 6666; // default port
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public int getPort(){
		return this.port;
	}

	public void process() {
		long startTime = new Date().getTime();
		ServerSocket serverSocket = null;
		
		try {        	
        	
            serverSocket = new ServerSocket(port);
           	System.out.println("Server started on "+port+". Waiting for clients...");
           	while (listening) {
            	FuzzyMatcher_Thread thread = new FuzzyMatcher_Thread(this, serverSocket.accept());
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        	if (serverSocket!=null && !serverSocket.isClosed()) {
        		try {
					serverSocket.close();
				} catch (IOException e) {
		            throw new RuntimeException(e);
				}
        	}
        	System.out.println("Server shut down.");
			long endTime = new Date().getTime();
			long totalTime = endTime - startTime;
			System.out.println("Total server run time (ms): " + totalTime);
        }
	}
	

}