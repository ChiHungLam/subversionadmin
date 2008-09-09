package com.gorthaur.svnadmin.client;

import com.google.gwt.core.client.EntryPoint;
import com.gorthaur.svnadmin.client.ui.LoginWindow;
import com.gorthaur.svnadmin.client.ui.MainPanel;
import com.gorthaur.svnadmin.client.ui.LoginWindow.LoginWindowListener;
import com.gorthaur.svnadmin.client.ui.MainPanel.MainPanelListener;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SvnAdministration extends Panel implements EntryPoint {

	final MainPanel mainPanel = new MainPanel();
	final LoginWindow login = new LoginWindow();
	private static Viewport viewPort;
	
	public SvnAdministration() {
		setBorder(false);  
		setPaddings(15);  
		setLayout(new FitLayout());
		
		mainPanel.setVisible(false);
			add(mainPanel);		
		
		login.addListener(new LoginWindowListener() {
			public void loginSuccess() {				
				mainPanel.setVisible(true);
				login.hide();
				SvnAdministration.this.doLayout();
				viewPort.doLayout();
			}
		});
		
		mainPanel.addListener(new MainPanelListener() {
			public void logout() {
				mainPanel.setVisible(false);
				login.show(SvnAdministration.this.getId());
				SvnAdministration.this.doLayout();
				viewPort.doLayout();
			}
		});
		
		login.show(this.getId());
		
	}

	public void onModuleLoad() {
		viewPort = new Viewport(this);
	}
}
