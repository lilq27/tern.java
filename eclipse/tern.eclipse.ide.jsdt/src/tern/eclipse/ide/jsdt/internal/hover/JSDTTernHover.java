package tern.eclipse.ide.jsdt.internal.hover;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.wst.jsdt.ui.text.java.hover.IJavaEditorTextHover;

import tern.eclipse.ide.ui.hover.TernHover;

/**
 * Implementation of JSDT Text Hover with Tern.
 *
 */
public class JSDTTernHover extends TernHover implements IJavaEditorTextHover {

	@Override
	protected IFile getFile(ITextViewer textViewer) {
		IFile file = super.getFile(textViewer);
		if (file != null) {
			return file;
		}
		ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager(); // get the buffer manager
		return null;
	}
}
