package org.melodi.learning.iitb.Segment;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

class TestDataWrite {
    PrintWriter out;
    BufferedReader rin;
    String outputBuffer;
    String rawLine;
    String delimit, tagDelimit, impDelimit;
    LabelMap labelmap;
    TestDataWrite(String outfile,String rawfile,String delimitP,String tagDelimitP,String impDelimitP, LabelMap linfo) {
        try {
            labelmap = linfo;
            out=new PrintWriter(new FileOutputStream(outfile+".tagged"));
            rin=new BufferedReader(new FileReader(rawfile+".raw"));
            outputBuffer=new String();
            delimit = delimitP;
            tagDelimit = tagDelimitP;
            impDelimit = impDelimitP;
        } catch(IOException e) {
            System.err.println("I/O Error"+e);
            System.exit(-1);
        }
    }	
    void writeRecord(int[] tok, int tokLen) {
        try {
            rawLine=rin.readLine();
            StringTokenizer rawTok=new StringTokenizer(rawLine,delimit,true);
            String tokArr[]=new String[rawTok.countTokens()];
            for(int j=0 ; j<tokArr.length ; j++) {
                tokArr[j]=rawTok.nextToken();
            }
            int ptr=0;
            int t=tok[0];
            for(int j=0 ; j<=tokLen ; j++) {
                if ((j < tokLen) && (t==tok[j])) {
                    while(ptr<tokArr.length && delimit.indexOf(tokArr[ptr])!=-1 && impDelimit.indexOf(tokArr[ptr])==-1) {
                        outputBuffer=new String(outputBuffer+tokArr[ptr]);

                        ptr++;
                    }
                    if (ptr<tokArr.length) {
                        outputBuffer=new String(outputBuffer+tokArr[ptr]);
                        ptr++;
                    }
                    while(ptr<tokArr.length && delimit.indexOf(tokArr[ptr])!=-1 && impDelimit.indexOf(tokArr[ptr])==-1) {
                        outputBuffer=new String(outputBuffer+tokArr[ptr]);
                        ptr++;
                    }
                } else {

                    int revScanPtr=outputBuffer.length()-1;
                    int goBackPtr=0;
                    boolean foundOpenChar=false;
                    while((revScanPtr >= 0) && (outputBuffer.charAt(revScanPtr)==' ' 
                        || outputBuffer.charAt(revScanPtr)=='(' || outputBuffer.charAt(revScanPtr)=='{' || outputBuffer.charAt(revScanPtr)=='[')) {
                        char currChar=outputBuffer.charAt(revScanPtr);
                        if (impDelimit.indexOf(currChar)!=-1) {
                            break;
                        }
                        if (currChar=='{' || currChar=='[' || currChar=='(') {
                            foundOpenChar=true;
                        }
                        revScanPtr--;
                        goBackPtr++;
                    }
                    if (foundOpenChar) {
                        outputBuffer=outputBuffer.substring(0,revScanPtr+1);
                        ptr-=goBackPtr;
                    }

                    outputBuffer=new String(outputBuffer+tagDelimit+labelmap.revMap(t));
                    out.println(outputBuffer);
                    outputBuffer=new String();
                    //						out.println(tagDelimit+t);
                    //						System.out.println(tagDelimit+t);
                    if (j < tokLen) {
                        t=tok[j];
                        j--;
                    }
                }
            }
            out.println();
        }  catch(IOException e) {
            System.err.println("I/O Error"+e);
            System.exit(-1);
        }
    }
    void close() {
        try {
            rin.close();
            out.close();
        }  catch(IOException e) {
            System.err.println("I/O Error"+e);
            System.exit(-1);
        } 
    }
};

