/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.latoe.layoutanalysis.pdf.pdfobject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author LaurentS
 */
@XStreamAlias("Word")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"mot"})
public class Word_PDF extends Box_PDF{
    
    public String mot;
    
    @XStreamAsAttribute
    public String font, style;

    @XStreamOmitField
    public int fontSize;
    
    public Word_PDF(int x1, int x2,int y1,int y2, String font, String style){
         super(x1,x2,y1,y2);
    }
    
    // Xstream ne passe pas par le constructeur définit pour la classe il utilise des serialisations
    public void constructor(){
        this.font = font.toLowerCase();
        this.style = style.toLowerCase();
        
        //Set fontSize attribute
	Pattern p = Pattern.compile("font-size:([0-9]{1,2})(pt|em|px)");
        Matcher m = p.matcher(this.style);
        
        if(m.find()){
            this.fontSize = Integer.parseInt(m.group(1)); 
        }
        else{
            this.fontSize = -1;
        }
    }
    
    public boolean isBold(){
        return ((this.font.contains("bold")) || (this.style.contains("bold")));
    }
    
    public boolean isItalic(){
        return ((this.font.contains("italic")) || (this.style.contains("italic")));
    }
}
