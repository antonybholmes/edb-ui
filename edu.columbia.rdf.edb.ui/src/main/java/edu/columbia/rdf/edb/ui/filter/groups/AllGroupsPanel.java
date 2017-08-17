package edu.columbia.rdf.edb.ui.filter.groups;

import java.awt.Graphics2D;
import java.util.Collection;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.modern.UI;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.widget.ModernWidget;

import edu.columbia.rdf.edb.Group;

public class AllGroupsPanel extends ModernWidget {
	private static final long serialVersionUID = 1L;

	private static final int BLOCK_SIZE = ModernWidget.WIDGET_HEIGHT;
	private static final int ORB_SIZE = 20;
	private static final int Y_ORB_OFFSET = (BLOCK_SIZE - ORB_SIZE) / 2;
	
	private Collection<Group> mGroups;

	public AllGroupsPanel(Collection<Group> groups) {
		mGroups = CollectionUtils.sort(groups);
		
		setBorder(BORDER);
		
		UI.setSize(this, Short.MAX_VALUE, 
				mGroups.size() * BLOCK_SIZE + getPagePadding());
	}

	@Override
	public void drawBackgroundAA(Graphics2D g2) {
		int x = PADDING;
		int y = getInsets().top;
		
		// Draw the orbs
		
		Graphics2D g2Temp = ImageUtils.createAAStrokeGraphics(g2);
		
		try {
			for (Group g : mGroups) {
				g2Temp.setColor(g.getColor());
				g2Temp.fillOval(x, y + Y_ORB_OFFSET, ORB_SIZE, ORB_SIZE);
				
				y += BLOCK_SIZE;
			}
		} finally {
			g2Temp.dispose();
		}
		
		// Draw the labels
		
		y = getInsets().top;
		
		g2.setColor(TEXT_COLOR);
		
		x += ORB_SIZE + DOUBLE_PADDING; 
		int yOffset = getTextYPosCenter(g2, BLOCK_SIZE);
		
		for (Group g : mGroups) {
			g2.drawString(g.getName(), x, y + yOffset);
			
			y += BLOCK_SIZE;
		}
	}
}
