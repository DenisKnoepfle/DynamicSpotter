package org.spotter.eclipse.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ILinkHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spotter.eclipse.ui.Activator;
import org.spotter.eclipse.ui.editors.AbstractSpotterEditorInput;
import org.spotter.eclipse.ui.providers.NavigatorContentProvider;

/**
 * A helper class that links a selection in the Navigator to the corresponding
 * active editor and vice-versa if the "Link with Editor" option is enabled.
 * 
 * @author Denis Knoepfle
 *
 */
public class LinkHelper implements ILinkHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkHelper.class);

	@Override
	public IStructuredSelection findSelection(IEditorInput anInput) {
		if (!(anInput instanceof AbstractSpotterEditorInput)) {
			throw new RuntimeException("Invalid input type");
		}
		AbstractSpotterEditorInput input = (AbstractSpotterEditorInput) anInput;
		IProject correspondingProject = input.getProject();
		String editorId = input.getEditorId();
		
		NavigatorContentProvider provider = Activator.getDefault().getNavigatorContentProvider();
		CommonViewer viewer = provider.getViewer();
		Object[] parentObjects = provider.getChildren(viewer.getInput());
		
		for (Object rawParent : parentObjects) {
			ISpotterProjectElement parent = (ISpotterProjectElement) rawParent;
			if (parent.getProject().equals(correspondingProject)) {
				ISpotterProjectElement element = recursiveElementSearch(editorId, parent);
				if (element != null) {
					// found a valid matching selection, so make it visible
					if (viewer.testFindItem(element) == null) {
						expandToElement(viewer, element);
					}
					viewer.reveal(element);
					return new StructuredSelection(element);
				}
				break;
			}
		}

		return null;
	}

	private ISpotterProjectElement recursiveElementSearch(String editorId, ISpotterProjectElement parent) {
		NavigatorContentProvider provider = Activator.getDefault().getNavigatorContentProvider();
		if (!provider.hasChildren(parent)) {
			return null;
		}
		
		Object[] rawChildren = provider.getChildren(parent);
		Activator.getDefault().getNavigatorViewer().refresh(parent);
		
		for (Object rawChild : rawChildren) {
			ISpotterProjectElement element = (ISpotterProjectElement) rawChild;
			if (rawChild instanceof IOpenableProjectElement) {
				IOpenableProjectElement openableElement = (IOpenableProjectElement) rawChild;
				if (editorId.equals(openableElement.getOpenId())) {
					return element;
				}
			}
			element = recursiveElementSearch(editorId, element);
			if (element != null) {
				return element;
			}
		}
		return null;
	}

	private void expandToElement(CommonViewer viewer, ISpotterProjectElement element) {
		ISpotterProjectElement parent = element;
		List<ISpotterProjectElement> ancestorList = new ArrayList<>();
		while (!SpotterProjectParent.class.isInstance(parent)) {
			parent = (ISpotterProjectElement) parent.getParent();
			ancestorList.add(parent);
		}
		
		for (int i = ancestorList.size() - 1; i >= 0; i--) {
			viewer.expandToLevel(ancestorList.get(i), 1);
		}
	}

	@Override
	public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
		Object rawElement = aSelection.getFirstElement();
		if (!(rawElement instanceof ISpotterProjectElement) || !(rawElement instanceof IOpenableProjectElement)) {
			return;
		}
		if (!aPage.isEditorAreaVisible()) {
			aPage.setEditorAreaVisible(true);
		}

		ISpotterProjectElement element = (ISpotterProjectElement) rawElement;
		IOpenableProjectElement openableElement = (IOpenableProjectElement) rawElement;
		IProject project = element.getProject();
		
		for (IEditorReference reference : aPage.getEditorReferences()) {
			try {
				IEditorInput editorInput = reference.getEditorInput();
				if (editorInput instanceof AbstractSpotterEditorInput) {
					AbstractSpotterEditorInput input = (AbstractSpotterEditorInput) editorInput;
					if (project.equals(input.getProject()) && input.getEditorId().equals(openableElement.getOpenId())) {
						aPage.activate(reference.getEditor(true));
						return;
					}
				}
			} catch (PartInitException e) {
				LOGGER.warn("Skipping editor reference: failed to retrieve related editor input");
			}
		}
	}

}
