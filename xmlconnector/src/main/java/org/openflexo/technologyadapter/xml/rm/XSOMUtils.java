/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.rm;

import java.io.InputStream;
import java.util.logging.Level;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

public class XSOMUtils {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(XSOMUtils.class.getPackage().getName());

	public static XSSchemaSet read(InputStream xsdInputStream) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Loading an XSD " + xsdInputStream);
		}
		XSOMParser parser = new XSOMParser();
		parser.setErrorHandler(new ErrorHandler() {

			@Override
			public void error(SAXParseException exception) throws SAXException {
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("XSOM-Error: " + exception.getMessage());
				}
			}

			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("XSOM-Fatal: " + exception.getMessage());
				}
			}

			@Override
			public void warning(SAXParseException exception) throws SAXException {
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("XSOM-Warning: " + exception.getMessage());
				}
			}

		});

		try {
			parser.parse(xsdInputStream);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		XSSchemaSet result = null;
		try {
			result = parser.getResult();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return result;
	}

}
