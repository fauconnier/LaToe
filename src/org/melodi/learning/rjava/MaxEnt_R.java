package org.melodi.learning.rjava;

import java.util.Enumeration;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.Rengine;

public class MaxEnt_R {
	
    public static void main(String[] args) {
    	System.out.println(System.getenv("R_HOME"));
    	System.out.println(System.getProperty("java.library.path"));
    	
    	args = new String[1];
    	args[0] = "--vanilla";
    	
//        System.out.println("Creating Rengine (with arguments)");
 		// 1) we pass the arguments from the command line
 		// 2) we won't use the main loop at first, we'll start it later
 		//    (that's the "false" as second argument)
 		// 3) the callbacks are implemented by the TextConsole class above
 		Rengine re=new Rengine(args, false, new TextConsole());
//        System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
        
        
        try {
        	REXP x;
        	System.out.println(x=re.eval("library('maximumentropy')"));
        	System.out.println(re.eval("model <- maximumentropy(Species ~., data=iris,addslack=T)"));
        	System.out.println(re.eval("save(model,file='mode.RData')"));
        	System.out.println(x=re.eval("predict(model,subset(iris,select=-Species))"));
        	
        	
        	
        	
        	
        } catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}
         
    }

}
