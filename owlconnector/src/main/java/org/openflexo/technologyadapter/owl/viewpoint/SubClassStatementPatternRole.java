package org.openflexo.technologyadapter.owl.viewpoint;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstanceModelFactory;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.SubClassStatement;

@ModelEntity
@ImplementationClass(SubClassStatementPatternRole.SubClassStatementPatternRoleImpl.class)
@XMLElement
public interface SubClassStatementPatternRole extends StatementPatternRole<SubClassStatement> {

	public static abstract class SubClassStatementPatternRoleImpl extends StatementPatternRoleImpl<SubClassStatement> implements
			SubClassStatementPatternRole {

		static final Logger logger = FlexoLogger.getLogger(SubClassStatementPatternRole.class.getPackage().toString());

		public SubClassStatementPatternRoleImpl() {
			super();
		}

		@Override
		public Type getType() {
			return null;
		}

		@Override
		public String getPreciseType() {
			return FlexoLocalization.localizedForKey("sub_class_statement");
		}

		@Override
		public SubClassStatementActorReference makeActorReference(SubClassStatement object, FlexoConceptInstance epi) {
			VirtualModelInstanceModelFactory factory = epi.getFactory();
			SubClassStatementActorReference returned = factory.newInstance(SubClassStatementActorReference.class);
			returned.setPatternRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}
	}
}
