package org.melodi.reader.larat.internal;

import java.util.Date;

public class Annotation implements java.io.Serializable{
	
	String author;
	Date date;
	String comment;
	String document_name;
	int id;
	
	public Annotation(){
		comment = "";
		document_name = "";
		id = -1;
		author = "";
	}

	public void setDocumentName(String document_name){
		this.document_name = document_name;
	}
	
	public String getDocumentName(){
		return this.document_name;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
