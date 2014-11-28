package org.melodi.reader.larat.internal;

import javax.swing.text.StyledEditorKit.BoldAction;

public class MelodiSentence {
	static LemSequence lems;
	static PosTagSequence postag;
	static TokenSequence token;
	static MorphologicSequence morpho;
	static HeadSequence head;
	static DepSyntaxicSequence syntaxic;
	
	public MelodiSentence(){
		lems = new LemSequence();
		postag = new PosTagSequence();
		token = new TokenSequence();
		morpho = new MorphologicSequence();
		head = new HeadSequence();
		syntaxic = new DepSyntaxicSequence();
	}
	
	public MorphologicSequence getMorpho() {
		return morpho;
	}

	public void setMorpho(MorphologicSequence morpho) {
		MelodiSentence.morpho = morpho;
	}

	public HeadSequence getHead() {
		return head;
	}

	public void setHead(HeadSequence head) {
		MelodiSentence.head = head;
	}

	public DepSyntaxicSequence getDepSyntaxic() {
		return syntaxic;
	}

	public void setDepSyntaxic(DepSyntaxicSequence syntaxic) {
		MelodiSentence.syntaxic = syntaxic;
	}

	public LemSequence getLems() {
		return lems;
	}

	public void setLems(LemSequence lems) {
		MelodiSentence.lems = lems;
	}

	public PosTagSequence getPostag() {
		return postag;
	}

	public void setPostag(PosTagSequence postag) {
		MelodiSentence.postag = postag;
	}

	public TokenSequence getToken() {
		return token;
	}

	public void setToken(TokenSequence token) {
		MelodiSentence.token = token;
	}
	
	public String toString(){
		String toString = "";
		toString = token.toString() + "\n" + lems.toString() + "\n" + postag.toString() + "\n" + morpho.toString() + "\n" + head.toString() + "\n" + syntaxic.toString();
		return toString;
	}
	
	public boolean check(){
		
		if ( (token.size() == lems.size()) && (token.size() == postag.size()) && (postag.size() == morpho.size()) && (morpho.size() == syntaxic.size()) && (head.size() == syntaxic.size()) ){
				return true;
			
		}
		else{
			return false;
		}
	}

}
