/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package tern.eclipse.ide.ui.hover;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;

import tern.eclipse.ide.core.IDETernProject;
import tern.eclipse.ide.internal.ui.Trace;
import tern.eclipse.ide.ui.JavaWordFinder;
import tern.eclipse.ide.ui.utils.EditorUtils;
import tern.eclipse.jface.text.HoverControlCreator;
import tern.eclipse.jface.text.PresenterControlCreator;
import tern.eclipse.jface.text.TernBrowserInformationControlInput;
import tern.server.protocol.type.TernTypeQuery;

/**
 * Tern Hover.
 *
 */
public class TernHover implements ITextHover, ITextHoverExtension,
		ITextHoverExtension2 {

	private IEditorPart editor;
	private IInformationControlCreator fHoverControlCreator;
	private IInformationControlCreator fPresenterControlCreator;

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		TernBrowserInformationControlInput info = (TernBrowserInformationControlInput) getHoverInfo2(
				textViewer, hoverRegion);
		return info != null ? info.getHtml() : null;
	}

	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		IFile scriptFile = getFile(textViewer);
		if (scriptFile == null) {
			return null;
		}
		IProject project = scriptFile.getProject();
		if (IDETernProject.hasTernNature(project)) {
			try {
				// project has tern nature, get hover info with tern.
				IDETernProject ternProject = IDETernProject
						.getTernProject(project);
				TernTypeQuery query = new TernTypeQuery(ternProject
						.getFileManager().getFileName(scriptFile),
						hoverRegion.getOffset());
				query.setDocs(true);
				query.setUrls(true);
				query.setTypes(true);

				HTMLTernTypeCollector collector = new HTMLTernTypeCollector();
				ternProject.request(query, scriptFile,
						textViewer.getDocument(), 0, collector);
				return new TernBrowserInformationControlInput(null,
						collector.getInfo(), 20);
			} catch (Exception e) {
				Trace.trace(Trace.WARNING, "Error while tern hyperlink", e);
			}
		}
		return null;
	}

	protected IFile getFile(ITextViewer textViewer) {
		if (editor != null) {
			return EditorUtils.getFile(editor);
		}

		return EditorUtils.getFile(textViewer.getDocument());
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return JavaWordFinder.findWord(textViewer.getDocument(), offset);
	}

	@Override
	public IInformationControlCreator getHoverControlCreator() {
		if (fHoverControlCreator == null)
			fHoverControlCreator = new HoverControlCreator(
					getInformationPresenterControlCreator());
		return fHoverControlCreator;
	}

	// @Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		if (fPresenterControlCreator == null)
			fPresenterControlCreator = new PresenterControlCreator();
		return fPresenterControlCreator;
	}

	public void setEditor(IEditorPart editor) {
		this.editor = editor;
	}

}