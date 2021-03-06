/**
 *  Copyright (c) 2013-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package tern.eclipse.ide.linter.internal.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Tern Linter UI messages.
 * 
 */
public final class TernLinterUIMessages extends NLS {

	private static final String BUNDLE_NAME = "tern.eclipse.ide.linter.internal.ui.TernLinterUIMessages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	// Buttons
	public static String Button_browse_project;
	public static String Button_browse_workspace;
	public static String Button_browse_filesystem;

	// Preferences
	public static String TernLinterOptionsBlock_enable;
	public static String TernLinterOptionsBlock_useConfigFiles;

	public static String Validation_jobName;
	public static String Validation_Project;
	public static String Validation_Title;

	private TernLinterUIMessages() {
	}

	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (MissingResourceException x) {
			fResourceBundle = null;
		}
		return fResourceBundle;
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, TernLinterUIMessages.class);
	}
}
