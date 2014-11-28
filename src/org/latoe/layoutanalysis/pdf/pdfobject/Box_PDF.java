/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.latoe.layoutanalysis.pdf.pdfobject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 *
 * @author LaurentS
 */
@XStreamAlias("Box")
public class Box_PDF {
     
    @XStreamAsAttribute
    public int x1,x2,y1,y2;
     
    @XStreamOmitField
    public int largeur, hauteur, surface;
     
    public Box_PDF(int x1, int x2,int y1,int y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    
    public int getSurface(){
        return this.surface;
    }
    
    public void construct(){
        this.largeur = (x2-x1) > 0 ? (x2-x1) : 0;
        this.hauteur = (y2-y1) > 0 ? (y2-y1) : 0;
        this.surface = this.hauteur * this.largeur;
    }
}
