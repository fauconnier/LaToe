package org.melodi.tools.fuzzymatcher.client;

import java.net.*;
import java.io.*;

import org.melodi.tools.fuzzymatcher.datamodel.Message_Matcher;

public class FuzzyMatcher_Client {

	private String host;
	private int port;

	/**
	 * Simple socket client to send text to a FuzzyMatcher Server.
	 * 
	 * @author Jean-Philippe Fauconnier
	 */
	public FuzzyMatcher_Client() {
		/* default values */
		host = "localhost";
		port = 6666;
	}
	
	
	public Message_Matcher analyse(String source, String pattern) throws UnknownHostException, IOException, ClassNotFoundException{
		
		
		/** pre traitement **/
		source = source.trim();
		pattern = pattern.trim();
		source = source.replaceAll("\n", "");
		pattern = pattern.replaceAll("\n", "");
		
		/** server var **/
		Socket socket_client = new Socket(host, port);
		OutputStream output_stream = socket_client.getOutputStream();
		ObjectOutputStream object_output_stream = new ObjectOutputStream(
				output_stream);
		InputStream input_stream = socket_client.getInputStream();
		ObjectInputStream input_stream_object = new ObjectInputStream(
				input_stream);
		
		/** sending request **/
		Message_Matcher msg_matcher = new Message_Matcher(source, pattern);
		object_output_stream.writeObject(msg_matcher);

		/** get answer **/
		msg_matcher = (Message_Matcher) input_stream_object
				.readObject();

		output_stream.close();
		socket_client.close();
		input_stream.close();
		input_stream_object.close();
		
		return msg_matcher;
	}
	
	
	/*
	 * Getters and Setters
	 */
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
	public boolean pingServer() throws UnknownHostException, IOException {
		Socket socket = null;
		boolean isAlive = false;
		try {
			socket = new Socket(host, port);
			isAlive = true;
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		return isAlive;
	}
}