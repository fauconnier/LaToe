/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.melodi.objectslogic;


/**
 *
 * @author LaurentS
 */
public class Box_Lara {
     
    public int x1,x2,y1,y2;
     
    public int surface;
     
    public Box_Lara(int x1, int x2,int y1,int y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.surface = (x2-x1)*(y2-y1);
    }
}
