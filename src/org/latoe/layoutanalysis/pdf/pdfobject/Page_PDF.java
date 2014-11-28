/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.latoe.layoutanalysis.pdf.pdfobject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author LaurentS
 */
@XStreamAlias("Page")
public class Page_PDF extends Box_PDF{
    @XStreamAsAttribute
    public int chunkCount,pageNumber,wordCount;
    
    @XStreamImplicit
    public List<Chunk_PDF> groupes;
    
    public Page_PDF(int x1, int x2,int y1,int y2,int chunkCount,int pageNumber,int wordCount){
        super(x1,x2,y1,y2);
        this.chunkCount = chunkCount;
        this.pageNumber = pageNumber;
        this.wordCount = wordCount;
        this.groupes = new ArrayList<>();
    }
    
    public void addChunk(Chunk_PDF c){
        this.groupes.add(c);
    }
    
    @Override
    public void construct(){
        super.construct();
        for(Chunk_PDF c: this.groupes){
            c.construct();
        }
        //tells which chunk is the first and the last chunk of the page
        this.groupes.get(0).isFirstOnPage = true;
        this.groupes.get(this.groupes.size()-1).isLastOnPage = true;
    }

}
