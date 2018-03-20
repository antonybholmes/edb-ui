package edu.columbia.rdf.edb.ui.login;

import java.io.UnsupportedEncodingException;

import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.text.ModernLabel;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;

import edu.columbia.rdf.edb.EDBWLogin;

public class LoginDetailsPanel extends MatrixPanel {
  private static final long serialVersionUID = 1L;

  private static final int[] ROWS = { WIDGET_HEIGHT };
  private static final int[] COLS = { 80, 450 };

  private ModernTextField mServerField = new ModernTextField();
  private ModernTextField mKeyField = new ModernTextField();
  // private ModernTextField portField = new ModernClipboardTextField();
  private ModernTextField mTotpField = new ModernTextField();

  public LoginDetailsPanel() {
    super(ROWS, COLS, PADDING, PADDING);

    // serverField.setEditable(false);
    // portField.setEditable(false);
    // userField.setEditable(false);
    // passwordField.setEditable(false);

    // mServerField.setBorder(BORDER);
    // mServerField.setOpaque(false);

    mTotpField.setBorder(BORDER);

    add(new ModernLabel("Server"));
    add(new ModernTextBorderPanel(mServerField));
    add(new ModernLabel("API Key"));
    add(new ModernTextBorderPanel(mKeyField));
    add(new ModernLabel("TOTP"));
    add(new ModernTextBorderPanel(mTotpField));
  }

  public final void set(EDBWLogin details) {
    mServerField.setText(details.getServer());
    // portField.setText(Integer.toString(details.getPort()));

    mKeyField.setText(details.getKey());

    mTotpField.setText(details.getTOTP());
  }

  public final EDBWLogin getLoginDetails()
      throws NumberFormatException, UnsupportedEncodingException {
    return EDBWLogin.create(mServerField.getText(),
        mKeyField.getText(),
        mTotpField.getText());
  }

  /*
   * public final void paintComponent(Graphics g) { Graphics2D g2 =
   * (Graphics2D)g;
   * 
   * g2.setColor(DialogButton.COLOR_2);
   * 
   * int x = 0;//ModernTheme.getInstance().getClass("widget").getInt("padding")
   * * 2;
   * 
   * g2.drawLine(x, 0, x, getHeight()); }
   */
}
