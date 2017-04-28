/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.diagram.controller;

import javax.swing.*;
import org.openflexo.fge.ScreenshotBuilder;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramEditor;
import org.openflexo.technologyadapter.diagram.model.Diagram;

public class DiagramScreenshotBuilder extends ScreenshotBuilder<Diagram> {
	@Override
	public String getScreenshotName(Diagram o) {
		return o.getName();
	}

	@Override
	public JComponent getScreenshotComponent(Diagram diagram) {

		FreeDiagramEditor editor = new FreeDiagramEditor(diagram, true);
		return editor.getDrawingView();

		/*ExternalVPMModule vpmModule = null;
		try {
			IModuleLoader moduleLoader = getViewPointLibrary().getServiceManager().getService(IModuleLoader.class);
			if (moduleLoader != null) {
				vpmModule = moduleLoader.getVPMModuleInstance();
			}
		} catch (ModuleLoadingException e) {
			logger.warning("cannot load VPM module (and so can't create screenshoot." + e.getMessage());
			e.printStackTrace();
		}

		if (vpmModule == null) {
			return null;
		}

		logger.info("Building " + getExpectedScreenshotImageFile().getAbsolutePath());

		JComponent c = vpmModule.createScreenshotForExampleDiagram(this);
		c.setOpaque(true);
		c.setBackground(Color.WHITE);
		JFrame frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setUndecorated(true);
		frame.getContentPane().add(c);
		frame.pack();
		c.validate();*/
		// return null;
	}
}
