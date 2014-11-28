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

import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.melodi.analyser.talismane_client.service.TalismaneClient;

public class Main_Jar {

	/**
	 * Main class for jar. Use 'ant dist' to build a new jar.
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 * @author Jean-Philippe Fauconnier
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {

		/* default values */
		String host = "localhost";
		int port = 7272;
		String pathFileInput = "";
		String encoding = "UTF-8";
		boolean pathFileStdin = false;

		/* dirty quick solution to parse args */
		Pattern pathFileStdin_pattern = Pattern.compile("true",
				Pattern.CASE_INSENSITIVE);
		for (String currArgs : args) {
			if (currArgs.matches("host=(.)*")) {
				host = currArgs.substring(5, currArgs.length());
			} else if (currArgs.matches("port=([0-9])*")) {
				port = Integer
						.parseInt(currArgs.substring(5, currArgs.length()));
			} else if (currArgs.matches("pathFileInput=(.)*")) {
				pathFileInput = currArgs.substring(14, currArgs.length());
			} else if (currArgs.matches("encoding=(.)*")) {
				encoding = currArgs.substring(9, currArgs.length());
			} else if (currArgs.matches("pathFileStdIn=(.)*")) {
				Matcher pathFileStdIn_matcher = pathFileStdin_pattern
						.matcher(currArgs);
				if (pathFileStdIn_matcher.find())
					pathFileStdin = true;
			} else if (currArgs.matches("--help")) {
				printUsage();
			}
		}

		/* Check */
		if (pathFileStdin && !pathFileInput.equals("")) {
			System.err
					.println("Warning : pathFileStdin and pathFileInput are enabled.\nBy default, pathFileStdin will be used.");
		}

		/* init. Talismaneclient */
		TalismaneClient talismane_client = new TalismaneClient(host, port,
				encoding);

		/* treat stdin */
		String stdin = "";
		if (pathFileInput.equals("")) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String stdin_line;
			while ((stdin_line = in.readLine()) != null) {
				stdin += stdin_line + "\n";
			}
		}

		/* one text */
		String text = "";
		if (!pathFileStdin) {
			/* if stdin */
			if (pathFileInput.equals("")) {
				text = stdin;
			}
			/* case where textFile is a path */
			else {
				text = talismane_client.readFile(pathFileInput);
			}

			/* analyse text */
			talismane_client.analyse(text);

		} else {
			/* multiple texts */
			String[] multiplePathFileInput = stdin.split("\n");
			for (String currPathFile : multiplePathFileInput) {
				if (!currPathFile.equals("")) {
					System.out.println("@NEWFILE");
					text = talismane_client.readFile(currPathFile);
					talismane_client.analyse(text);
				}
			}

		}
	}

	static void printUsage() {
		/* usage */
		System.err.println("Usage:\njava -jar talismane_client-1.x.x.jar "
				+ "host=[*host] port=[*7272] pathFileInput=[*stdin or path] "
				+ "encoding=[*UTF-8] pathFileStdIn=[*false|true]\n"
				+ "\n\t* indicates default values for args.");
		System.exit(1);
	}

}
