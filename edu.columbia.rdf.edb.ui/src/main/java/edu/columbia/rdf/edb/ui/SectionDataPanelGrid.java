package edu.columbia.rdf.edb.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.core.dictionary.SubstitutionService;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.widget.ModernWidget;

import edu.columbia.rdf.edb.DataView;
import edu.columbia.rdf.edb.DataViewField;
import edu.columbia.rdf.edb.DataViewSection;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleTag;

/**
 * Displays the results of an experiment using a grid and multiple Ui elements
 * to make it easier for users to cut and paste.
 *
 * @author Antony Holmes Holmes
 *
 */
public class SectionDataPanelGrid extends ModernPlotCanvas {
  private static final long serialVersionUID = 1L;

  private static final int ROW_HEIGHT = 24;

  private static final int WIDTH = 500;

  private static final int GAP = 20;

  public int height = 0;

  private DataView mView;

  private Sample mSample;

  public SectionDataPanelGrid(Sample sample, DataView view) {

    mSample = sample;
    mView = view; // DataViewService.getInstance().getView(sample.getExpressionType());

    int h = 0;

    for (DataViewSection dataViewSection : mView) {
      h += (1 + dataViewSection.size()) * ROW_HEIGHT + GAP;
    }

    setCanvasSize(new Dimension(WIDTH, h));
  }

  @Override
  public void plot(Graphics2D g2, DrawingContext context, Object... params) {
    int x = getInsets().left;
    int x2 = x + 180;
    int y = (ROW_HEIGHT + g2.getFontMetrics().getAscent()) / 2;

    g2.setColor(TEXT_COLOR);

    for (DataViewSection dataViewSection : mView) {

      g2.setFont(BOLD_FONT); // ModernWidget.HEADING_FONT);

      g2.drawString(dataViewSection.getName(), x, y);

      g2.setFont(ModernWidget.FONT);

      y += ROW_HEIGHT;

      for (DataViewField field : dataViewSection) {
        // g2.setColor(ALT_TEXT_COLOR);
        g2.drawString(field.getName(), x, y);

        SampleTag fieldValue = mSample.getTags().getTag(field);

        if (fieldValue != null) {
          g2.drawString(SubstitutionService.getInstance().getSubstitute(fieldValue.getValue()), x2, y);
        } else {
          g2.drawString(TextUtils.NA, x2, y);
        }

        y += ROW_HEIGHT;
      }

      y += GAP;
    }
  }
}
