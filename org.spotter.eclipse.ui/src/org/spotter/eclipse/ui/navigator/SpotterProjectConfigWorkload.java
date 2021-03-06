/**
 * Copyright 2014 SAP AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spotter.eclipse.ui.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.spotter.eclipse.ui.Activator;
import org.spotter.eclipse.ui.editors.AbstractSpotterEditor;
import org.spotter.eclipse.ui.editors.WorkloadEditor;
import org.spotter.eclipse.ui.editors.WorkloadEditorInput;
import org.spotter.eclipse.ui.handlers.OpenHandler;
import org.spotter.eclipse.ui.menu.IOpenable;
import org.spotter.shared.configuration.FileManager;

/**
 * An element that represents the workload adapters node.
 * 
 * @author Denis Knoepfle
 * 
 */
public class SpotterProjectConfigWorkload extends AbstractProjectElement {

	public static final String IMAGE_PATH = "icons/workload-adapter.png"; //$NON-NLS-1$

	private static final String ELEMENT_NAME = "Workload Satellite Adapter";
	private static final String OPEN_ID = WorkloadEditor.ID;

	private ISpotterProjectElement parent;

	/**
	 * Creates a new instance of this element.
	 * 
	 * @param parent
	 *            the parent element
	 */
	public SpotterProjectConfigWorkload(ISpotterProjectElement parent) {
		super(IMAGE_PATH);
		this.parent = parent;
		addHandler(OpenHandler.OPEN_COMMAND_ID, new IOpenable() {
			@Override
			public void open() {
				SpotterProjectConfigWorkload.this.open();
			}

			@Override
			public String getOpenId() {
				return OPEN_ID;
			}

			@Override
			public String getElementName() {
				return ELEMENT_NAME;
			}
		});
	}

	@Override
	public String getText() {
		return ELEMENT_NAME;
	}

	@Override
	public Object getParent() {
		return parent;
	}

	@Override
	public IProject getProject() {
		return parent.getProject();
	}

	private void open() {
		if (!Activator.getDefault().testServiceStatus(getProject().getName(), true)) {
			return;
		}
		IFile file = getProject().getFile(FileManager.ENVIRONMENT_FILENAME);
		AbstractSpotterEditor.openInstance(new WorkloadEditorInput(file), OPEN_ID);
	}

}
