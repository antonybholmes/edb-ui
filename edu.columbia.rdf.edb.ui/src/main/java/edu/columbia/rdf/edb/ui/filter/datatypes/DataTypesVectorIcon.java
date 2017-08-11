/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.edb.ui.filter.datatypes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.abh.common.ui.graphics.icons.ModernVectorIcon;
import org.abh.common.ui.widget.ModernWidget;



// TODO: Auto-generated Javadoc
/**
 * Group vector icon showing 3 colored balls spaced equally around a circle.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class DataTypesVectorIcon extends ModernVectorIcon {
	
	
	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.icons.ModernIcon#drawForeground(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Object... params) {
		
		Color color = (Color)params[0];
		
		/*
		Graphics2D g2Temp = ImageUtils.createAAStrokeGraphics(g2);
		
		try {
			g2Temp.setColor(color);
			g2Temp.fillOval(x, y, w, w);
		} finally {
			g2Temp.dispose();
		}
		*/
		
		g2.setColor(color);
		g2.fillRect(x, y, w, w);
		
		g2.setColor(Color.WHITE);
		g2.setFont(ModernWidget.FONT);
		
		Point p = ModernWidget.getStringCenterPlotCoordinates(g2, w, h, "T");
		
		g2.drawString("T", x + p.x, y + p.y);
	}

}
