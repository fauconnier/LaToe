package org.melodi.tools.fuzzymatcher.server;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import org.melodi.tools.fuzzymatcher.datamodel.Message_Matcher;

class FuzzyMatcher_Thread extends Thread {
	private Socket socket = null;
	private FuzzyMatcher_Server server = null;
	private FuzzyMatcher_Service fuzzy_matcher;

	public FuzzyMatcher_Thread(FuzzyMatcher_Server server, Socket socket) {
		super("FuzzyMatcher Thread");
		this.server = server;
		this.socket = socket;
	}

	public void run() {
		try {

			InputStream input_stream = socket.getInputStream();
			ObjectInputStream input_stream_object = new ObjectInputStream(
					input_stream);
			ObjectOutputStream output_stream_object = new ObjectOutputStream(
					socket.getOutputStream());

			Message_Matcher msg_matcher = (Message_Matcher) input_stream_object
					.readObject();

			if (msg_matcher != null) {
				System.out.println("\n");
				System.out.println("New Request");

				System.out.println("Source : " + msg_matcher.getSource());
				System.out.println("Pattern : " + msg_matcher.getPattern());
				FuzzyMatcher_Service newMatcher = new FuzzyMatcher_Service();
				newMatcher.contains(msg_matcher);
				output_stream_object.writeObject(msg_matcher);
				newMatcher = null;
				System.gc();
			}

			socket.close();
		} catch (EOFException e) {
			// TODO : fix it!
		} catch(IOException e) {
			e.printStackTrace(); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static final class ShutDownListener {
		FuzzyMatcher_Server server;

		public ShutDownListener(FuzzyMatcher_Server server) {
			this.server = server;
		}
	}

	public void setMacherService(FuzzyMatcher_Service matcher_Service) {
		this.fuzzy_matcher = matcher_Service;
	}
}
