package org.openflexo.technologyadapter.emf.fml;

import org.openflexo.foundation.fml.ClassRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;

@ModelEntity
@ImplementationClass(EMFClassClassRole.EMFClassClassRoleImpl.class)
@XMLElement
public interface EMFClassClassRole extends ClassRole<EMFClassClass> {

	public static abstract class EMFClassClassRoleImpl extends ClassRoleImpl<EMFClassClass> implements
			EMFClassClassRole {

		public EMFClassClassRoleImpl() {
			super();
		}

	}
}