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
package edu.columbia.rdf.edb.ui.login;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogFlatButton;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.theme.ModernTheme;
import org.jebtk.modern.theme.ThemeService;

/**
 * The class LoginButton.
 */
public class LoginButton extends ModernDialogFlatButton {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant ARROW_WIDTH.
	 */
	private static final int ARROW_WIDTH = 20;

	/**
	 * The constant LW.
	 */
	private static final int LW = 10;

	private static final Color BORDER_COLOR = 
			ThemeService.getInstance().colors().getColorHighlight32(16); //ColorUtils.getTransparentColor20(Color.WHITE);

	/**
	 * The height.
	 */
	private int HEIGHT = 64;

	private int WIDTH = 128;

	/**
	 * The roundel height.
	 */
	private int ROUNDEL_HEIGHT = 40;

	/**
	 * Instantiates a new login button.
	 */
	public LoginButton() {
		super(UI.BUTTON_SIGN_IN);

		UI.setSize(this, WIDTH, HEIGHT);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.button.ModernButtonWidget#drawBackground(java.awt.Graphics2D)
	 */
	@Override
	public void drawBackground(Graphics2D g2) {
		super.drawBackground(g2);

		//drawBorder(g2, BORDER_COLOR);
	}

	/* (non-Javadoc)
	 * @see org.abh.lib.ui.modern.button.ModernButton#drawForegroundAA(java.awt.Graphics2D)
	 */
	@Override
	public void drawForegroundAAText(Graphics2D g2) {
		 //Color.WHITE);

		Graphics2D g2Temp = ImageUtils.createAAStrokeGraphics(g2);

		try {
			g2Temp.setStroke(ModernTheme.DOUBLE_LINE_STROKE);

			int x = DOUBLE_PADDING; //(getWidth() - ROUNDEL_HEIGHT) / 2;
			int y = (getHeight() - ROUNDEL_HEIGHT) / 2;

			g2Temp.setColor(Color.WHITE);
			g2Temp.fillOval(x, y, ROUNDEL_HEIGHT, ROUNDEL_HEIGHT);
			
			g2Temp.setColor(BORDER_COLOR);
			g2Temp.drawOval(x, y, ROUNDEL_HEIGHT, ROUNDEL_HEIGHT);

			x += (ROUNDEL_HEIGHT - ARROW_WIDTH) / 2;
			y += ROUNDEL_HEIGHT / 2;

			g2Temp.drawLine(x, y, x + ARROW_WIDTH, y);

			x += ARROW_WIDTH;

			g2Temp.drawLine(x, y, x - LW, y - LW);
			g2Temp.drawLine(x, y, x - LW, y + LW);
		} finally {
			g2Temp.dispose();
		}

		g2.setColor(TEXT_COLOR);
		g2.setFont(HEADING_FONT);

		//Point p = centerText(g2, mText1, getWidth() / 2, getHeight() * 3 / 4);

		g2.drawString(mText1, 64, getTextYPosCenter(getHeight()));
	}
}