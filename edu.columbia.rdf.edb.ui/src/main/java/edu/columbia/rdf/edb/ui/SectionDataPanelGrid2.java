package edu.columbia.rdf.edb.ui;

import org.jebtk.core.dictionary.SubstitutionService;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.VBoxAutoWidth;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.text.ModernLabelBold;
import org.jebtk.modern.text.ModernTextBorderPanel;

import edu.columbia.rdf.edb.DataView;
import edu.columbia.rdf.edb.DataViewField;
import edu.columbia.rdf.edb.DataViewSection;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleTag;

/**
 * Displays the results of an experiment using a grid and multiple Ui elements
 * to make it easier for users to cut and paste.
 *
 * @author Antony Holmes
 *
 */
public class SectionDataPanelGrid2 extends VBoxAutoWidth {
  private static final long serialVersionUID = 1L;

  private static final int[] ROWS = { ModernWidget.WIDGET_HEIGHT };

  private static final int[] COLUMNS = { 180, 400 };

  private DataView mView;

  private Sample mSample;

  public SectionDataPanelGrid2(Sample sample, DataView view) {
    mSample = sample;
    mView = view; // DataViewService.getInstance().getView(sample.getExpressionType());

    for (DataViewSection dataViewSection : mView) {
      add(new ModernLabelBold(dataViewSection.getName()));

      add(UI.createVGap(5));

      MatrixPanel p = new MatrixPanel(ROWS, COLUMNS, 0, PADDING);

      for (DataViewField field : dataViewSection) {

        p.add(new ModernLabel(field.getName()));

        SampleTag fieldValue = mSample.getTags().getTag(field);

        String text;

        if (fieldValue != null) {
          text = SubstitutionService.getInstance()
              .getSubstitute(fieldValue.getValue());
        } else {
          text = TextUtils.NA;
        }

        p.add(new ModernTextBorderPanel(
            new ModernClipboardTextField(text, false)));
      }

      add(p);

      add(UI.createVGap(20));
    }
  }
}
