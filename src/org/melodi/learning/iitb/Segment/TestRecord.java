package org.melodi.learning.iitb.Segment;

import org.melodi.learning.iitb.CRF.SegmentDataSequence;
import org.melodi.learning.iitb.CRF.Segmentation;

class TestRecord implements SegmentDataSequence, Segmentation {

	private static final long serialVersionUID = -9126147224366724551L;
	String seq[];
	int path[];

	TestRecord(String s[]) {
		seq = s;
		path = new int[seq.length];
	}

	void init(String s[]) {
		seq = s;
		if ((path == null) || (path.length < seq.length)) {
			path = new int[seq.length];
		}
	}

	public void set_y(int i, int l) {
		path[i] = l;
	} // not applicable for training data.

	public int y(int i) {
		return path[i];
	}

	public int length() {
		return seq.length;
	}

	public Object x(int i) {
		return seq[i];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iitb.CRF.SegmentDataSequence#getSegmentEnd(int)
	 */
	public int getSegmentEnd(int segmentStart) {
		if ((segmentStart > 0) && (y(segmentStart) == y(segmentStart - 1)))
			return -1;
		for (int i = segmentStart + 1; i < length(); i++) {
			if (y(i) != y(segmentStart))
				return i - 1;
		}
		return length() - 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iitb.CRF.SegmentDataSequence#setSegment(int, int, int)
	 */
	public void setSegment(int segmentStart, int segmentEnd, int y) {
		for (int i = segmentStart; i <= segmentEnd; i++)
			set_y(i, y);
	}

	public int getSegmentId(int offset) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public int numSegments() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public int segmentEnd(int segmentNum) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public int segmentLabel(int segmentNum) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public int segmentStart(int segmentNum) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
};