/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.edb.ui;

import java.io.UnsupportedEncodingException;

import javax.swing.Box;
import javax.swing.SwingWorker;

import org.jebtk.modern.BorderService;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonWidget;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.help.GuiAppInfo;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernSplashTitleLabel;
import org.jebtk.modern.window.ModernSplashScreen;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.ui.login.LoginButton;
import edu.columbia.rdf.edb.ui.login.LoginDetailsPanel;

/**
 * The class LoginDialog.
 */
public class LoginDialog extends ModernSplashScreen
    implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The m login panel.
   */
  private LoginDetailsPanel mLoginPanel = new LoginDetailsPanel();

  /**
   * The m activity bar.
   */
  // private ModernActivityBar mActivityBar;

  /**
   * The m cache button.
   */
  // private ModernClickWidget mCacheButton;

  /**
   * The m login.
   */
  protected EDBWLogin mLogin = null;

  /**
   * The class SetupTask.
   */
  private class SetupTask extends SwingWorker<Void, Void> {

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    public Void doInBackground() {
      success();

      // mActivityBar.stop();
      close();

      return null;
    }
  }

  /**
   * Instantiates a new login dialog.
   *
   * @param login the login
   */
  public LoginDialog(GuiAppInfo appInfo, EDBWLogin login) {
    super(appInfo);

    init(login);
  }

  public LoginDialog(ModernWindow parent, GuiAppInfo appInfo, EDBWLogin login) {
    super(parent, appInfo);

    init(login);
  }

  private void init(EDBWLogin login) {
    createUi();

    setup(login);

    setSize(640, 420);
  }

  /**
   * Creates the ui.
   */
  public final void createUi() {

    // topPanel.add(Ui.createHorizontalGap(10));

    // ModernImagePanel image =
    // new ModernImagePanel(getAppInfo().getLargeIcon(), new Dimension(128,
    // 128));

    // topPanel.add(image);

    // topPanel.add(Ui.createVerticalGap(30));
    // topPanel.add(new ModernHeadingLabel("Sign in", Color.WHITE));
    // topPanel.add(Ui.createVerticalGap(10));
    // topPanel.add(new ModernSubHeadingLabel("You must sign in to " +
    // getAppInfo().getName(), Color.WHITE));
    // topPanel.add(new ModernSubHeadingLabel("to authenticate your access.",
    // Color.WHITE));

    // leftPanel.add(Ui.createVerticalGap(250));

    // mCacheButton = new ModernLinkButton("Clear cache");
    // mCacheButton.setAlignmentY(BOTTOM_ALIGNMENT);
    // mCacheButton.addClickListener(this);
    // leftPanel.add(cacheButton);

    ModernPanel content = new ModernPanel(ModernSplashScreen.COLOR);

    VBox vBox = VBox.create();

    vBox.setBorder(BorderService.getInstance().createBorder(30));

    vBox.add(new ModernSplashTitleLabel(getAppInfo().getName())); // getAppInfo().getName()));

    content.setHeader(vBox);

    vBox = VBox.create();

    vBox.setBorder(BorderService.getInstance().createBorder(30));

    // vBox.add(UI.createVGap(20));

    // vBox.add(new ModernSplashTitleLabel(getAppInfo().getName()));

    // vBox.add(UI.createVGap(40));

    vBox.add(mLoginPanel);

    // vBox.add(UI.createVGap(20));

    // box.add(Box.createHorizontalGlue());

    Box box = HBox.create();

    ModernButtonWidget button = new LoginButton();
    button.addClickListener(this);

    box.add(UI.createHGap(85));
    box.add(button);
    // box.add(Box.createHorizontalGlue());
    // box.add(mCacheButton);

    vBox.add(box);

    // vBox.add(UI.createVGap(30));
    // mActivityBar = new ModernActivityBar();
    // UI.setSize(mActivityBar, 640, 24);
    // vBox.add(mActivityBar);

    // content.setBody(new ModernPanel(new TopShadowPanel(vBox)));
    content.setBody(new ModernPanel(vBox));

    setBody(content);

    UI.centerWindowToScreen(this);

  }

  /**
   * Sets the up.
   *
   * @param login the new up
   */
  public void setup(EDBWLogin login) {
    mLoginPanel.set(login);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * ui. event.ModernClickEvent)
   */
  @Override
  public final void clicked(ModernClickEvent e) {

    try {
      mLogin = mLoginPanel.getLoginDetails();
    } catch (NumberFormatException e1) {
      e1.printStackTrace();
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }

    // Write the settings to disk

    /*
     * // Batch save SettingsService.getInstance().setAutoSave(false);
     * SettingsService.getInstance().update("edb.modules.edbw.server",
     * mLogin.getServer());
     * SettingsService.getInstance().update("edb.modules.edbw.user",
     * mLogin.getUser());
     * SettingsService.getInstance().update("edb.modules.edbw.key",
     * mLogin.getKey());
     * SettingsService.getInstance().update("edb.modules.edbw.topt.epoch",
     * mLogin.getEpoch());
     * SettingsService.getInstance().update("edb.modules.edbw.topt.step-size",
     * mLogin.getStep()); SettingsService.getInstance().setAutoSave(true);
     */

    EDBWLogin.saveSettings(mLogin);

    if (mLogin == null) {
      ModernMessageDialog.createDialog(null,
          getAppInfo().getName(),
          "The Experiment Store server is not responding.",
          MessageDialogType.WARNING);

      return;
    }

    success();

    setStatus(ModernDialogStatus.OK);

    close();
  }

  protected void success() {
    // Do nothing
  }

  public EDBWLogin getLogin() {
    return mLogin;
  }
}
