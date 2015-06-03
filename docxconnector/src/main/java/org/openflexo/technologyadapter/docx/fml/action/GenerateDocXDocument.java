/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fml.action;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.docx4j.wml.P;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDocument.DocXDocumentImpl;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

@ModelEntity
@ImplementationClass(GenerateDocXDocument.GenerateDocXDocumentImpl.class)
@XMLElement
@FML("GenerateDocXDocument")
public interface GenerateDocXDocument extends DocXAction<DocXDocument> {

	/*@PropertyIdentifier(type = File.class)
	public static final String FILE_KEY = "file";

	@Getter(value = FILE_KEY)
	@XMLAttribute
	public File getFile();

	@Setter(FILE_KEY)
	public void setFile(File aFile);*/

	public static abstract class GenerateDocXDocumentImpl extends DocXActionImpl<DocXDocument> implements GenerateDocXDocument {

		private static final Logger logger = Logger.getLogger(GenerateDocXDocument.class.getPackage().getName());

		@Override
		public Type getAssignableType() {
			return DocXDocument.class;
		}

		@Override
		public DocXDocument execute(FlexoBehaviourAction action) throws FlexoException {

			DocXDocument generatedDocument = null;

			try {

				DocXDocumentResource templateResource = getModelSlot().getTemplateResource();
				DocXDocument templateDocument = templateResource.getResourceData(null);

				FreeModelSlotInstance<DocXDocument, DocXModelSlot> msInstance = (FreeModelSlotInstance<DocXDocument, DocXModelSlot>) getModelSlotInstance(action);

				FlexoResource<DocXDocument> generatedResource = msInstance.getResource();

				System.out.println("-------------> generating document " + generatedResource);

				/*FlexoResource<DocXDocument> generatedResource = DocXDocumentResourceImpl.makeDocXDocumentResource(getFile().toURI()
						.toString(), getFile(), (DocXTechnologyContextManager) getModelSlotTechnologyAdapter()
						.getTechnologyContextManager());*/

				// WordprocessingMLPackage generatedPackage = new WordprocessingMLPackage();

				// MainDocumentPart mdp = XmlUtils.deepCopy(templateDocument.getWordprocessingMLPackage().getMainDocumentPart());
				// generatedPackage.set
				// templateDocument.getWordprocessingMLPackage().getMainDocumentPart()

				generatedResource.setResourceData(templateDocument);
				generatedResource.save(null);
				generatedResource.unloadResourceData(false);
				generatedResource.loadResourceData(null);

				generatedDocument = generatedResource.getResourceData(null);

				for (P p : DocXDocumentImpl.getAllElementsFromObject(generatedDocument.getWordprocessingMLPackage().getMainDocumentPart(),
						P.class)) {
					String oldId = p.getParaId();
					p.setParaId(generatedDocument.getFactory().generateId());
					System.out.println("Paragraph " + p + " change id from " + oldId + " to " + p.getParaId());
				}

				System.out.println("Pour la resource " + generatedResource);
				System.out.println("La resource data c'est: " + generatedResource.getResourceData(null));

			}

			catch (Exception e) {
				e.printStackTrace();
				throw new FlexoException(e);
			}

			/*generatedResource.save(null);
			generatedResource.unloadResourceData();
			generatedResource.loadResourceData(null);
			generatedDocument = generatedResource.getResourceData(null);*/

			/*assertFalse(generatedDocument == templateDocument);

			assertEquals(13, generatedDocument.getElements().size());

			assertEquals(5, generatedDocument.getStyles().size());*/

			return generatedDocument;
		}
	}
}