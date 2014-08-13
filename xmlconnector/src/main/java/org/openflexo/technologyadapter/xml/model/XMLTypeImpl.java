/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2014 Openflexo
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
package org.openflexo.technologyadapter.xml.model;

import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;

public abstract class XMLTypeImpl  implements XMLType {


	/* Properties */

	private final String NSPrefix;

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(XMLTypeImpl.class.getPackage().getName());

	/**
	 * Default Constructor
	 * 
	 * @param qName
	 * 
	 * @param adapter
	 */


	public XMLTypeImpl() {
		super();
		this.NSPrefix = null;
	}
	

	public XMLTypeImpl(String nsURI, String lName, String qName, XMLMetaModel model) {
		// TODO : je ne suis pas sur que ces attributs aient encore du sens
		super();
		this.setName(lName);
		NSPrefix = qName.replaceAll(":" + lName, "");
	}
	
	@Override
	public String getFullyQualifiedName() {
		if (getURI() != null && !getURI().isEmpty())
			return getURI();
		else
			return getName();
	}

	/*
	@Override
	public String getURI() {
		if (getNameSpaceURI() != null) {
			return getNameSpaceURI() + "#" + getName();
		}
		else {
			return getName();
		}
	}
*/
	
	public XMLTechnologyAdapter getTechnologyAdapter() {
		return getMetaModel().getTechnologyAdapter();
	}


}