package edu.columbia.rdf.edb.ui;

import java.awt.Color;
import java.awt.Dimension;

import org.jebtk.modern.UI;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.theme.ThemeService;

public class PropertyLabel extends ModernAutoSizeLabel {
  private static final long serialVersionUID = 1L;

  public static final Color LABEL_COLOR = ThemeService.getInstance().getColors()
      .getTheme32(20);

  public PropertyLabel(String text) {
    super(text);

    // setForeground(LABEL_COLOR);
  }

  public PropertyLabel(String text, Dimension size) {
    this(text);

    UI.setSize(this, size);
  }

}
