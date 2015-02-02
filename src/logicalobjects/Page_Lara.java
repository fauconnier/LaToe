/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logicalobjects;

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
public class Page_Lara extends Box_Lara{
    @XStreamAsAttribute
    public int chunkCount,pageNumber,wordCount;
    
    @XStreamImplicit
    public List<Chunk_Lara> groupes;
    
    public Page_Lara(int x1, int x2,int y1,int y2,int chunkCount,int pageNumber,int wordCount){
        super(x1,x2,y1,y2);
        this.chunkCount = chunkCount;
        this.pageNumber = pageNumber;
        this.wordCount = wordCount;
        this.groupes = new ArrayList<>();
    }
    
    public void addChunk(Chunk_Lara c){
        this.groupes.add(c);
    }

}
