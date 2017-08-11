package edu.columbia.rdf.edb.ui.microarray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;

import org.abh.common.ui.UI;
import org.abh.common.ui.button.ModernCheckSwitch;
import org.abh.common.ui.dialog.ModernDialogTaskWindow;
import org.abh.common.ui.event.ModernClickEvent;
import org.abh.common.ui.event.ModernClickListener;
import org.abh.common.ui.scrollpane.ModernScrollPane;
import org.abh.common.ui.scrollpane.ScrollBarPolicy;
import org.abh.common.ui.widget.ModernClickWidget;
import org.abh.common.ui.widget.ModernTwoStateWidget;
import org.abh.common.ui.window.ModernWindow;

import edu.columbia.rdf.edb.DataView;
import edu.columbia.rdf.edb.DataViewField;
import edu.columbia.rdf.edb.DataViewSection;

public class NormalizationDialog extends ModernDialogTaskWindow implements ModernClickListener {
	private static final long serialVersionUID = 1L;

	public static final String MICROARRAY_EXPRESSION_DATA = "Microarray";
	
	private static final DataView DATA_VIEW = new MicroarrayDataView();
	
	private ModernTwoStateWidget mCheckSelectAll = 
			new ModernCheckSwitch(UI.MENU_SELECT_ALL);
	
	//private ModernListModel<String> model = new ModernListModel<String>();
	
	//private ModernList<String> list = new ModernList<String>();
	
	private List<DataViewField> fields = new ArrayList<DataViewField>();
	
	private Map<DataViewField, ModernTwoStateWidget> mFieldCheckMap =
			new HashMap<DataViewField, ModernTwoStateWidget>();

	
	private class SelectAllEvents implements ModernClickListener {

		@Override
		public void clicked(ModernClickEvent e) {
			selectAll();
		}	
	}
	

	
	public NormalizationDialog(ModernWindow parent, String title) {
		super(parent);

		setTitle(title);

		setResizable(false);

		setSize(500, 400);

		setup();
	}



	protected final void createUi(Box box) {
		sectionHeader("Annotation options", box);

		//box2 = new HBoxPanel();
		//box2.setAlignmentX(Box.LEFT_ALIGNMENT);
		//box2.add(checkAnnotation);
		
		//box.add(ModernTheme.createVerticalGap());
		//box.add(box2);
		
		//DataView dataViewSample = 
		//		new MicroarrayDataView(); //ViewPluginService.getInstance().getView(MICROARRAY_EXPRESSION_DATA).getDataView();
		
		Box box2 = Box.createVerticalBox();
		
		box2.add(mCheckSelectAll);
		
		for (DataViewSection dataViewSection : DATA_VIEW) {			
			for (DataViewField field : dataViewSection) {
				ModernTwoStateWidget checkBox = 
						new ModernCheckSwitch(field.getName());
				
				fields.add(field);
				box2.add(checkBox);
				mFieldCheckMap.put(field, checkBox);
			}
		}

		ModernScrollPane scrollPane = new ModernScrollPane(box2);
		scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		
		//box2 = new HBoxPanel();
		
		//ModernPanel panel = new ModernLineBorderPanel(scrollPane);
		
		//box2.add(panel);
		
		//scrollPane.setBorder(BorderService.getInstance().createLeftRightBorder(10));
		//box2.setAlignmentX(LEFT_ALIGNMENT);
		box.add(scrollPane);

		//JPanel buttonPanel = new Panel(new FlowLayout(FlowLayout.LEFT));

		//importButton.setCanvasSize(new Dimension(100, ModernTheme.getInstance().getClass("widget").getInt("height")));
		//exportButton.setCanvasSize(new Dimension(100, ModernTheme.getInstance().getClass("widget").getInt("height")));

		//buttonPanel.add(importButton);
		//buttonPanel.add(exportButton);

		//panel.add(buttonPanel, BorderLayout.PAGE_END);
		//box.setBorder(ModernWidget.LARGE_BORDER);

		setDialogCardContent(box);
	}

	private void setup() {
		mCheckSelectAll.addClickListener(new SelectAllEvents());
		
		UI.centerWindowToScreen(this);
	}
	
	private void selectAll() {
		for (ModernClickWidget checkBox : mFieldCheckMap.values()) {
			checkBox.setSelected(mCheckSelectAll.isSelected());
		}
	}

	public List<Boolean> getColumns() {
		return new ArrayList<Boolean>();
	}

	public List<DataViewField> getColumnAnnotations() {
		//DataView dataViewSample = 
		//		ViewPluginService.getInstance().getView(MICROARRAY_EXPRESSION_DATA).getDataView();
		
		List<DataViewField> ret = new ArrayList<DataViewField>();
		
		for (DataViewSection dataViewSection : DATA_VIEW) {			
			for (DataViewField field : dataViewSection) {
				if (mFieldCheckMap.get(field).isSelected()) {
					ret.add(field);
				}
			}
		}
		
		return ret;
	}
}
