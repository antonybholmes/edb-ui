/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.edb.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.UIService;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.theme.RenderMode;
import org.jebtk.modern.theme.ThemeService;
import org.jebtk.modern.tree.ModernTreeNodeRenderer;
import org.jebtk.modern.tree.Tree;
import org.jebtk.modern.tree.TreeIconNodeCountRenderer;
import org.jebtk.modern.widget.ModernWidget;

import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.Sample;


public class SamplesListTreeNodeRenderer extends ModernTreeNodeRenderer {
	private static final long serialVersionUID = 1L;


	private static final int HEADER_HEIGHT = 32;
	private static final int HEIGHT = 60;
	//private static final int LINE_HEIGHT = Resources.ICON_SIZE_24;


	private static final Color FILL_COLOR = 
			ThemeService.getInstance().colors().getHighlight(2);


	private static final int ORB_SIZE = 8;
	private static final int ORB_GAP = 2;


	private static final int ORB_SPACE = ORB_SIZE + ORB_GAP;


	/** The maximum number of groups to show */
	private static final int MAX_GROUP_DISPLAY = 5;

	private String mText1 = null;
	private String mText2 = null;
	private String mText3 = null;
	//private String mText4 = null;


	private List<Color> mColors;


	@Override
	public final void drawBackground(Graphics2D g2) {
		if (mNode.isParent()) {
			fill(g2, FILL_COLOR, mRect);
		} else if (mNodeIsSelected) {
			getWidgetRenderer().buttonFillPaint(g2, mRect, RenderMode.SELECTED, false);
			
			ImageUtils.fillRect(g2, mRect);
		} else if (mNodeIsHighlighted) {
			getWidgetRenderer().buttonFillPaint(g2, mRect, RenderMode.HIGHLIGHT, false);
			
			ImageUtils.fillRect(g2, mRect);
		} else {
			// do nothing
		}
	}

	@Override
	public void drawForegroundAAText(Graphics2D g2) {

		int y;

		int x;

		// Draw the dividing line only if the node is not highlighted, otherwise
		// it interferes with the highlighting rectangle


		if (mNode.isParent()) {
			x = PADDING;
			y = (HEADER_HEIGHT - UIService.ICON_SIZE_16) / 2;

			if (mNode.isExpanded()) {
				TreeIconNodeCountRenderer.BRANCH_OPEN_ICON.drawIcon(g2, x, y, 16);
			} else {
				TreeIconNodeCountRenderer.BRANCH_CLOSED_ICON.drawIcon(g2, x, y, 16);
			}

			x += TreeIconNodeCountRenderer.BRANCH_OPEN_ICON.getWidth() + PADDING; // + ModernTheme.getInstance().getClass("widget").getInt("padding");

			 //(HEADER_HEIGHT + g2.getFontMetrics().getAscent()) / 2;

			//g2.clipRect(0, 0, getWidth(), getHeight());

			g2.setFont(ModernWidget.BOLD_FONT);
			g2.setColor(TEXT_COLOR);
			
			y = ModernWidget.getTextYPosCenter(g2, HEADER_HEIGHT);
			g2.drawString(getTruncatedText(g2, mText1, x, mRect.getW()), x, y);

			g2.setColor(ModernWidget.LINE_COLOR);

			//g2.drawLine(0, 0, mRect.getW() - 1, 0);

			y = mRect.getH() - 1;
			g2.drawLine(0, y, mRect.getW() - 1, y);
		} else {
			x = DOUBLE_PADDING;
			//x += PADDINTreeIconNodeCountRenderer.BRANCH_OPEN_ICON.getWidth(); // + ModernTheme.getInstance().getClass("widget").getInt("padding");

			g2.setFont(ModernWidget.SUB_SUB_HEADING_FONT);
			g2.setColor(TEXT_COLOR);
			y = 20;
			g2.drawString(getTruncatedText(g2, mText1, x, mRect.getW()), x, y);

			//g2.setColor(mNodeIsSelected ? TEXT_COLOR : ALT_TEXT_COLOR);
			g2.setFont(ModernWidget.FONT);

			y += 16;
			g2.drawString(getTruncatedText(g2, mText2, x, mRect.getW()), x, y);

			y += 16;
			g2.drawString(getTruncatedText(g2, mText3, x, mRect.getW()), x, y);

			//
			// colored orbs
			//
			
			
			y = (getHeight() - ORB_SIZE) / 2;
			x = mRect.getW() - ModernWidget.TRIPLE_PADDING - mColors.size() * ORB_SIZE - (mColors.size() - 1) * ORB_GAP;
			
			Graphics2D g2Temp = ImageUtils.createAAStrokeGraphics(g2);
			
			int c = 0;
			
			try {
				for (Color color : mColors) {
					g2Temp.setColor(color);
					g2Temp.fillOval(x, y, ORB_SIZE, ORB_SIZE);
					
					//g2Temp.setColor(Color.WHITE);
					//g2Temp.drawOval(x, y, ORB_SIZE, ORB_SIZE);
					
					x += ORB_SPACE;
					
					if (c == MAX_GROUP_DISPLAY) {
						break;
					}
					
					++c;
				}
			} finally {
				g2Temp.dispose();
			}
			
			//g2.drawString(getTruncatedText(g2, mText4, x, mRect.getW()), x, y);

			if (!mNodeIsHighlighted) {
				g2.setColor(ModernWidget.LINE_COLOR);
				g2.drawLine(0, mRect.getH() - 1, mRect.getW() - 1, mRect.getH() - 1);
			}
		}	
	}

	@Override
	public ModernTreeNodeRenderer getRenderer(Tree<?> tree,
			TreeNode<?> node,
			boolean nodeIsHighlighted,
			boolean nodeIsSelected,
			boolean hasFocus,
			boolean isDragToNode,
			int depth,
			int row) {
		super.getRenderer(tree, 
				node, 
				nodeIsHighlighted, 
				nodeIsSelected, 
				hasFocus, 
				isDragToNode, 
				depth, 
				row);

		if (node.isParent()) {
			mText1 = node.getName();
		} else {
			Sample sample = (Sample)node.getValue();

			mText1 = sample.getName();

			mText2 = sample.getOrganism().getScientificName();

			mText3 = sample.getPersons().iterator().next().getName();

			//mText4 = Group.formatNames(sample.getGroups());
			
			mColors = Group.formatColors(sample.getGroups());
			
			//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			//mText4 = formatter.format(sample.getDate()); //sample.getDate().toString();
		}

		return this;
	}

	@Override
	public void setSize(Tree<?> tree,
			TreeNode<?> node,
			int depth,
			int row) {
		if (node.isParent()) {
			setSize(tree.getInternalRect().getW(), HEADER_HEIGHT);
		} else {
			setSize(tree.getInternalRect().getW(), HEIGHT);
		}
	}
}