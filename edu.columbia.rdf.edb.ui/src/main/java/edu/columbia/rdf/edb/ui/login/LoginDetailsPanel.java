package edu.columbia.rdf.edb.ui.login;

import java.io.UnsupportedEncodingException;

import org.abh.common.ui.panel.MatrixPanel;
import org.abh.common.ui.text.ModernLabel;
import org.abh.common.ui.text.ModernTextBorderPanel;
import org.abh.common.ui.text.ModernTextField;

import edu.columbia.rdf.edb.EDBWLogin;


public class LoginDetailsPanel extends MatrixPanel {
	private static final long serialVersionUID = 1L;

	private static final int[] ROWS = {WIDGET_HEIGHT};
	private static final int[] COLS = {80, 450};

	private ModernTextField mServerField = new LoginTextField();
	private ModernTextField mUserField = new LoginTextField();
	//private ModernTextField portField = new ModernClipboardTextField();
	private ModernTextField mKeyField = new LoginTextField();

	public LoginDetailsPanel() {
		super(ROWS, 
				COLS, 
				PADDING, 
				PADDING);

		//serverField.setEditable(false);
		//portField.setEditable(false);
		//userField.setEditable(false);
		//passwordField.setEditable(false);

		//mServerField.setBorder(BORDER);
		//mServerField.setOpaque(false);

		mKeyField.setBorder(BORDER);
		
		add(new ModernLabel("Server"));
		add(new ModernTextBorderPanel(mServerField));
		add(new ModernLabel("User"));
		add(new ModernTextBorderPanel(mUserField));
		add(new ModernLabel("Key"));
		add(new ModernTextBorderPanel(mKeyField));
	}

	public final void set(EDBWLogin details) {
		mServerField.setText(details.getServer());
		//portField.setText(Integer.toString(details.getPort()));

		mUserField.setText(details.getUser());

		mKeyField.setText(details.getFullKey());
	}

	public final EDBWLogin getLoginDetails() throws NumberFormatException, UnsupportedEncodingException  {
		return EDBWLogin.create(mServerField.getText(), 
				mUserField.getText(), 
				mKeyField.getText());
	}

	/*
	public final void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(DialogButton.COLOR_2);

		int x = 0;//ModernTheme.getInstance().getClass("widget").getInt("padding") * 2;

		g2.drawLine(x, 0, x, getHeight());
	}
	*/
}
