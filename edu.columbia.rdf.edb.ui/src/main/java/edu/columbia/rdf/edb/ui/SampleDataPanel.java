package edu.columbia.rdf.edb.ui;

import java.awt.Component;
import java.io.IOException;

import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.clipboard.ClipboardService;
import org.jebtk.modern.clipboard.ModernClipboardWidget;
import org.jebtk.modern.panel.ModernLineBottomBorderPanel;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarLocation;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;

import edu.columbia.rdf.edb.DataView;
import edu.columbia.rdf.edb.Sample;

/**
 * Displays all of the information pertaining to a particular sample.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class SampleDataPanel extends ModernClipboardWidget {
  private static final long serialVersionUID = 1L;

  // private ModernIconMenuItem expandMenuItem =
  // new ModernIconMenuItem("Expand All",
  // UIResources.getInstance().loadIcon(PlusVectorIcon.class, 16));

  // private ModernIconMenuItem collapseMenuItem =
  // new ModernIconMenuItem("Collapse All",
  // UIResources.getInstance().loadIcon(MinusVectorIcon.class, 16));

  private Sample mSample;

  // private ModernPopupMenu menu;

  // private ModernCollapsePane collapsePane;

  public SampleDataPanel(Sample sample, DataView view) {

    mSample = sample;

    SectionDataPanelGrid2 grid = new SectionDataPanelGrid2(sample, view);

    ModernScrollPane scrollPane = new ModernScrollPane(grid)
        .setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
        .setVScrollBarLocation(ScrollBarLocation.FLOATING)
        .setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW);

    // scrollPane.setBorder(LARGE_BORDER);
    // scrollPane.setBackground(Color.WHITE);
    // scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
    // scrollPane.getViewport().setBackground(Color.WHITE);

    setBody(scrollPane);

    // createMenu();
  }

  @Override
  public void setHeader(Component c) {
    super.setHeader(new ModernComponent(
        new ModernLineBottomBorderPanel(new ModernComponent(c)),
        BorderService.getInstance().createBottomBorder(10)));
  }

  public Sample getSample() {
    return mSample;
  }

  @Override
  public boolean copyEnabled() {
    return true;
  }

  @Override
  public void copy() {
    StringBuilder buffer = new StringBuilder();

    buffer.append("Sample");
    buffer.append(TextUtils.TAB_DELIMITER);

    try {
      mSample.formattedTxt(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }

    ClipboardService.copyToClipboard(buffer.toString());
  }
}
