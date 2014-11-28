package org.melodi.reader.larat.view.highlighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class RectHighlightPainter implements Highlighter.HighlightPainter {

	
	private void paintLine(Graphics g, Rectangle r, int x2) {
		System.out.println("print Line");
		int ytop = r.y + r.height - 3;
		g.fillRect(r.x, ytop, x2 - r.x, 3);
		g.drawRect(r.x,  ytop, x2 - r.x, 40);
		int var = x2 - r.x;
		System.out.println(r.x + " " + ytop + " " + var + " " + 100);
	}

	public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {

		Rectangle r0 = null, r1 = null, rbounds = bounds.getBounds();
		int xmax = rbounds.x + rbounds.width; // x coordinate of right edge
		try { // convert positions to pixel coordinates
			r0 = c.modelToView(p0);
			r1 = c.modelToView(p1);
		} catch (BadLocationException ex) {
			return;
		}
		if ((r0 == null) || (r1 == null))
			return;

//		g.setColor(c.getSelectionColor());
		g.setColor(Color.red);

		// special case if p0 and p1 are on the same line
		if (r0.y == r1.y) {
			paintLine(g, r0, r1.x);
			return;
		}

		// first line, from p1 to end-of-line
		paintLine(g, r0, xmax);

		// all the full lines in between, if any (assumes that all lines have
		// the same height--not a good assumption with JEditorPane/JTextPane)
		r0.y += r0.height; // move r0 to next line
		r0.x = rbounds.x; // move r0 to left edge
		while (r0.y < r1.y) {
			paintLine(g, r0, xmax);
			r0.y += r0.height; // move r0 to next line
		}

		// last line, from beginning-of-line to p1
		paintLine(g, r0, r1.x);
	}

}
