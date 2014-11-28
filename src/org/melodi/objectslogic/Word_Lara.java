/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.melodi.objectslogic;


public class Word_Lara {
    public String font, style;
    public String mot;
    
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    public Word_Lara(){
//    	super(0,0,0,0);
    }
    
    public Word_Lara(int x1, int x2,int y1,int y2, String font, String style){
//         super(x1,x2,y1,y2);
         this.font = font;
         this.style = style;
    }
    
    public String toString(){
    	return this.mot + "("+x1+","+y1+","+x2+","+y2+")"+font+style;
    }
}
