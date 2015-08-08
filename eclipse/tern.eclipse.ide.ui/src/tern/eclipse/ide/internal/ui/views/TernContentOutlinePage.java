package tern.eclipse.ide.internal.ui.views;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import tern.eclipse.ide.core.IIDETernProject;
import tern.eclipse.ide.core.TernCorePlugin;
import tern.eclipse.ide.core.resources.TernDocumentFile;
import tern.eclipse.ide.internal.ui.TernUIMessages;
import tern.server.TernPlugin;
import tern.server.protocol.outline.TernOutlineQuery;

public class TernContentOutlinePage extends ContentOutlinePage {

	private static final int UPDATE_DELAY = 200;

	private final TernOutline outline;
	private Boolean updating;
	private Boolean redone;

	public TernContentOutlinePage(TernDocumentFile ternFile) {
		this.outline = new TernOutline(ternFile);
		this.updating = false;
		this.redone = false;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		getTreeViewer().setContentProvider(new TernExplorerContentProvider(this));
		getTreeViewer().setLabelProvider(new DelegatingStyledCellLabelProvider(new TernExplorerLabelProvider()));
		getTreeViewer().setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		refreshOutline();
	}

	void refreshOutline() {
		if (updating) {
			redone = true;
			return;
		}
		synchronized (updating) {
			updating = true;
		}
		Job refresh = new Job(TernUIMessages.refreshOutline) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					final TreeViewer viewer = getTreeViewer();
					TernDocumentFile ternFile = outline.getTernFile();
					IIDETernProject ternProject = TernCorePlugin.getTernProject(ternFile.getFile().getProject());
					if (ternProject != null && ternProject.hasPlugin(TernPlugin.outline)) {

						if (redone) {
							return Status.CANCEL_STATUS;
						}
						// Call tern-outline
						TernOutlineQuery query = new TernOutlineQuery(ternFile.getFileName());
						ternProject.request(query, ternFile, outline);

						// Refresh UI Tree
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								Control refreshControl = viewer.getControl();
								if ((refreshControl != null) && !refreshControl.isDisposed() && !redone) {
									if (viewer.getInput() == null) {
										viewer.setInput(outline);
									} else {
										viewer.refresh();
										viewer.expandAll();
									}
								}
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					synchronized (updating) {
						updating = false;
					}
					if (redone) {
						redone = false;
						refreshOutline();
					}
				}
				return Status.OK_STATUS;
			}
		};
		refresh.setSystem(true);
		refresh.setPriority(Job.SHORT);
		refresh.schedule(UPDATE_DELAY);

	}

}
