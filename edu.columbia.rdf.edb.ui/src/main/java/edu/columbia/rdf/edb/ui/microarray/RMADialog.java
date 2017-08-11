package edu.columbia.rdf.edb.ui.microarray;

import org.abh.common.ui.panel.VBox;
import org.abh.common.ui.window.ModernWindow;

public class RMADialog extends NormalizationDialog {
	private static final long serialVersionUID = 1L;

	public RMADialog(ModernWindow parent) {
		super(parent, "RMA Normalization");

		createUi();
	}

	private final void createUi() {
		createUi(VBox.create());
	}
}
