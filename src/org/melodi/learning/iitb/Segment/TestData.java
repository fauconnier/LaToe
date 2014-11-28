package org.melodi.learning.iitb.Segment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

class TestData {
    BufferedReader rin;
    String line;
    String seq[];
    String fname;
    String delimit, impDelimit;
    TestData(String file,String delimitP,String impDelimitP, String grpDelimit) {
        try {
            fname = file;
            rin =new BufferedReader(new FileReader(file+".raw"));
            delimit = delimitP;
            impDelimit = impDelimitP;
        }  catch(IOException e) {
            System.out.println("I/O Error"+e);
            System.exit(-1);
        }
    }
    void startScan() {
        try {
            rin =new BufferedReader(new FileReader(fname+".raw"));
        }  catch(IOException e) {
            System.out.println("I/O Error"+e);
            System.exit(-1);
        }   
    }
    int[] groupedTokens() {
        /*
	if (grp == null)
	    return null;
	return grp.groupingArray(seq.length);
         */
        return null;
    }
    String[] nextRecord() {
        try {
            if ((line=rin.readLine())!=null) {
                StringTokenizer tok=new StringTokenizer(line.toLowerCase(),delimit,true);
                int len = tok.countTokens();
                if ((seq == null) || (seq.length < len))
                    seq =new String[len];
                int count=0;
                for(int i=0 ; i<len; i++) {
                    String tokStr=tok.nextToken();
                    if (delimit.indexOf(tokStr)==-1 || impDelimit.indexOf(tokStr)!=-1) {
                        seq[count++]=new String(tokStr);
                    } 
                }
                String aseq[]=new String[count];
                for(int i=0 ; i<count ; i++) {
                    aseq[i]=seq[i];
                }
                return aseq;
            } else {
                rin.close();
                return null;
            }
        } catch(IOException e) {
            System.out.println("I/O Error"+e);
            System.exit(-1);
        }
        return null;
    }
};
