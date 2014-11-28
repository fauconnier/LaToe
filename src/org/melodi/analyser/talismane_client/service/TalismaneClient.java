///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2012 Jean-Philippe Fauconnier
//
//This file is part of TalismaneClient.
//
//TalismaneClient is free software: you can redistribute it and/or modify
//it under the terms of the GNU Affero General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//Talismane is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Affero General Public License for more details.
//
//You should have received a copy of the GNU Affero General Public License
//along with Talismane.  If not, see <http://www.gnu.org/licenses/>.
//////////////////////////////////////////////////////////////////////////////
package org.melodi.analyser.talismane_client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TalismaneClient {

	private String host;
	private int port;
	private String encoding;

	/**
	 * Simple socket client to send text to a Talismane server.
	 * 
	 * @author Jean-Philippe Fauconnier
	 */
	public TalismaneClient() {
		/* default values */
		host = "localhost";
		port = 7272;
		encoding = "UTF-8";
	}

	public TalismaneClient(String host, int port, String encoding) {
		this.host = host;
		this.port = port;
		this.encoding = encoding;
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

	public void closeServer() throws UnknownHostException, IOException, InterruptedException{
		//TODO : See in talismane code to fix that.
		this.closeServerFix();
		Thread.sleep(2000); // need to wait the end of threads on Talismane server.
		this.closeServerFix();
	}
	
	public void closeServerFix() throws UnknownHostException, IOException {
		//TODO : See in talismane code to fix that.
		Socket socket = new Socket(host, port);
		OutputStreamWriter out = new OutputStreamWriter(
				socket.getOutputStream(), encoding);

		String shutdown_tag = "@SHUTDOWN";
		out.write(shutdown_tag);
		out.flush();

		socket.close();
	}

	public String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public Structure analyse(String text) throws UnknownHostException,
			IOException, InterruptedException {
		return this.analyse(text, true);
	}

	public Structure analyse(String text, boolean print)
			throws UnknownHostException, IOException, InterruptedException {
		/* open socket and streams */
		Socket socket = new Socket(host, port);
		OutputStreamWriter out = new OutputStreamWriter(
				socket.getOutputStream(), Charset.forName(encoding));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream(), Charset.forName(encoding)));

		/* send text */
		text = "\f" + text + "\f\f\f";
		out.write(text);
		out.flush();

		/* get output */
		ArrayList<SentenceAnalyser> listAnalyser = new ArrayList<SentenceAnalyser>();
		Structure conll_structure = new Structure();
		int id_sentence = 0;
		String line = "";
		String previous_line = "";
		String sentence = "";

		while ((line = in.readLine()) != null) {
			/* new sentence */
			if (line.matches("^1\t(.*)")) {
				id_sentence++;
				sentence += line + "\n";
			}
			/* in a sentence */
			else if (!line.equals("")) {
				sentence += line + "\n";
			}

			/* end of a sentence. Run thread SentenceAnalyser */
			if (line.equals("") && !previous_line.equals("")) {
				SentenceAnalyser currSentenceAnalyser = new SentenceAnalyser(
						id_sentence, sentence, conll_structure, print);
				currSentenceAnalyser.start();
				listAnalyser.add(currSentenceAnalyser);
				sentence = "";
			}
			previous_line = line;
		}

		/* close connection */
		socket.close();

		// TODO check threads on large corpora
		/* synchronize all threads */
		for (SentenceAnalyser currAnalyser : listAnalyser) {
			currAnalyser.join();
		}

		// TODO check threads on large corpora
		/* sort sentences */
		Collections.sort(conll_structure, new Comparator<Sentence>() {
			@Override
			public int compare(Sentence o1, Sentence o2) {
				return o1.getId_sentence() - o2.getId_sentence();
			}
		});
		return conll_structure;
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

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
