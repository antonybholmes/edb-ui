package edu.columbia.rdf.edb.ui.microarray;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.widget.ModernTwoStateWidget;
import org.jebtk.modern.window.ModernWindow;

public class Mas5Dialog extends NormalizationDialog implements ModernClickListener {
  private static final long serialVersionUID = 1L;

  private ModernTwoStateWidget checkSignal = new ModernCheckSwitch("Signal", true);

  private ModernTwoStateWidget checkDetected = new ModernCheckSwitch("Detected");

  private ModernTwoStateWidget checkPValue = new ModernCheckSwitch("P-Value");

  public Mas5Dialog(ModernWindow parent) {
    super(parent, "MAS5 Normalization");

    createUi();
  }

  private final void createUi() {
    Box box = VBox.create();

    sectionHeader("MAS5 data column options", box);

    box.add(checkSignal);
    box.add(ModernPanel.createVGap());
    box.add(checkDetected);
    box.add(ModernPanel.createVGap());
    box.add(checkPValue);

    box.add(UI.createVGap(20));

    createUi(box);
  }

  public List<Boolean> getColumns() {
    List<Boolean> columns = new ArrayList<Boolean>();

    columns.add(checkSignal.isSelected());
    columns.add(checkDetected.isSelected());
    columns.add(checkPValue.isSelected());

    return columns;
  }
}
