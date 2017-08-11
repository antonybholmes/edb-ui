package edu.columbia.rdf.edb.ui.microarray;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;

import org.abh.common.ui.UI;
import org.abh.common.ui.button.ModernCheckSwitch;
import org.abh.common.ui.event.ModernClickListener;
import org.abh.common.ui.panel.ModernPanel;
import org.abh.common.ui.panel.VBox;
import org.abh.common.ui.widget.ModernTwoStateWidget;
import org.abh.common.ui.window.ModernWindow;

public class Mas5Dialog extends NormalizationDialog implements ModernClickListener {
	private static final long serialVersionUID = 1L;

	private ModernTwoStateWidget checkSignal = 
			new ModernCheckSwitch("Signal", true);
		
	private ModernTwoStateWidget checkDetected = 
			new ModernCheckSwitch("Detected");
	
	private ModernTwoStateWidget checkPValue =
			new ModernCheckSwitch("P-Value");

	
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
