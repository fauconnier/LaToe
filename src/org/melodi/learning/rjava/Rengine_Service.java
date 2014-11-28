package org.melodi.learning.rjava;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;


public class Rengine_Service {
	
	Rengine re;
	
	public Rengine_Service(){
		String args [] = new String[1];
    	args[0] = "--vanilla";
    	
//        System.out.println("Creating Rengine (with arguments)");
 		// 1) we pass the arguments from the command line
 		// 2) we won't use the main loop at first, we'll start it later
 		//    (that's the "false" as second argument)
 		// 3) the callbacks are implemented by the TextConsole class above
 		re=new Rengine(args, false, new TextConsole());
//        System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
        }
        
	}

	public Rengine getRengine(){
		return re;
	}
	
	class TextConsole implements RMainLoopCallbacks
	{
	    public void rWriteConsole(Rengine re, String text, int oType) {
	        System.out.print(text);
	    }
	    
	    public void rBusy(Rengine re, int which) {
	        System.out.println("rBusy("+which+")");
	    }
	    
	    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
	        System.out.print(prompt);
	        try {
	            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	            String s=br.readLine();
	            return (s==null||s.length()==0)?s:s+"\n";
	        } catch (Exception e) {
	            System.out.println("jriReadConsole exception: "+e.getMessage());
	        }
	        return null;
	    }
	    
	    public void rShowMessage(Rengine re, String message) {
	        System.out.println("rShowMessage \""+message+"\"");
	    }
		
	    public String rChooseFile(Rengine re, int newFile) {
		FileDialog fd = new FileDialog(new Frame(), (newFile==0)?"Select a file":"Select a new file", (newFile==0)?FileDialog.LOAD:FileDialog.SAVE);
		fd.show();
		String res=null;
		if (fd.getDirectory()!=null) res=fd.getDirectory();
		if (fd.getFile()!=null) res=(res==null)?fd.getFile():(res+fd.getFile());
		return res;
	    }
	    
	    public void   rFlushConsole (Rengine re) {
	    }
		
	    public void   rLoadHistory  (Rengine re, String filename) {
	    }			
	    
	    public void   rSaveHistory  (Rengine re, String filename) {
	    }			
	}
}
