package edu.columbia.rdf.edb.ui.login;

import java.awt.Color;
import java.awt.Graphics;

import org.jebtk.core.ColorUtils;
import org.jebtk.modern.text.ModernTextField;

public class LoginTextField extends ModernTextField {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final Color FILL = ColorUtils.getTransparentColor60(Color.WHITE);

  public LoginTextField() {
    setup();
  }

  public LoginTextField(String text) {
    super(text);

    setup();
  }

  public void setup() {
    // setOpaque(false);

    // setBackground(ColorUtils.getTransparentColor50(Color.WHITE));

    // setForeground(Color.WHITE);

    // setSelectedTextColor(Color.WHITE);
    // setSelectionColor(ColorUtils.getTransparentColor50(Color.WHITE));
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(FILL);

    g.fillRect(0, 0, getWidth(), getHeight());

    super.paintComponent(g);
  }
}
