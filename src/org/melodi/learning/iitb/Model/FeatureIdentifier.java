package org.melodi.learning.iitb.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.latoe.layoutanalysis.pdf.labelisation.Service_CRF;

/**
 * 
 * @author Sunita Sarawagi
 * @since 1.1
 * @version 1.3
 */
public class FeatureIdentifier implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4942036159933407311L;
	public int id;
	public Object name;
	public int stateId;

	public FeatureIdentifier() {
	}

	public FeatureIdentifier(int fid, int s, Object n) {
		init(fid, s, n);
	}

	public FeatureIdentifier(String strRep) {
		StringTokenizer strTok = new StringTokenizer(strRep, ":");
		name = strTok.nextToken();
		id = Integer.parseInt(strTok.nextToken());
		stateId = Integer.parseInt(strTok.nextToken());
	}

	public void init(int fid, int s, Object n) {
		name = n;
		id = fid;
		stateId = s;
	}

	public void init(int fid) {
		id = fid;
	}

	public void copy(FeatureIdentifier fid) {
		init(fid.id, fid.stateId, fid.name);
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(Object o) {
		return (id == ((FeatureIdentifier) o).id);
	}

	public String getName() {
		return name.toString();
	}

	public String toString() {
		// TODO : quick fix
		//DEBUG : j'ai enlevé le internal id dans les sorties.
//		return name.toString() + "->" + serviceCRF.intMapLabel.get(stateId);
		return name.toString();
//		return name.toString() + "[id:" + id + "]" + serviceCRF.intMapLabel.get(stateId);
	}

	public Object clone() {
		return new FeatureIdentifier(id, stateId, name);
	}
};
