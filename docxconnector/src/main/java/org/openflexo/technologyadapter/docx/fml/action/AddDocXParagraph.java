/*
 * (c) Copyright 2013- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.docx.fml.action;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;

/**
 * Create DOCX paragraph
 * 
 * @author sylvain
 * 
 */

@ModelEntity
@ImplementationClass(AddDocXParagraph.AddDocXParagraphImpl.class)
@XMLElement
public interface AddDocXParagraph extends DocXAction<DocXParagraph> {

	public static abstract class AddDocXParagraphImpl extends DocXActionImpl<DocXParagraph> implements AddDocXParagraph {

		private static final Logger logger = Logger.getLogger(AddDocXParagraph.class.getPackage().getName());

		public AddDocXParagraphImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return DocXParagraph.class;
		}

		@Override
		public DocXParagraph execute(FlexoBehaviourAction action) {
			DocXParagraph result = null;
			// TODO : Implement Action
			return result;
		}

	}
}