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

package org.openflexo.technologyadapter.csv;

import java.lang.reflect.Type;

import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclareFetchRequests;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.fml.CSVRole;

/**
 * Implementation of the ModelSlot class for the CSV technology adapter<br>
 * We expect here to connect an CSV model conform to an CSVMetaModel
 * 
 * @author Jean Le Paon
 * 
 */
@DeclarePatternRoles({ // All pattern roles available through this model slot
        @DeclarePatternRole(flexoRoleClass = CSVRole.class, FML = "Object"),
    })
@DeclareEditionActions({ // All edition actions available through this model slot
    })
@DeclareFetchRequests({ // All requests available through this model slot
    })
@ModelEntity
@ImplementationClass(CSVModelSlot.CSVModelSlotImpl.class)
@XMLElement
public interface CSVModelSlot extends FreeModelSlot<CSVModel> {

    @Override
    public CSVTechnologyAdapter getTechnologyAdapter();

    public static abstract class CSVModelSlotImpl extends FreeModelSlotImpl<CSVModel> implements CSVModelSlot {

        @Override
        public Class<CSVTechnologyAdapter> getTechnologyAdapterClass() {
            return CSVTechnologyAdapter.class;
        }

        /**
         * Instanciate a new model slot instance configuration for this model slot
         */
        @Override
        public CSVModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
            return new CSVModelSlotInstanceConfiguration(this, action);
        }

        @Override
        public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
            if (CSVRole.class.isAssignableFrom(patternRoleClass)) {
                return "Object";
        	}
            return "";
        }

        @Override
        public Type getType() {
            return CSVModel.class;
        }

        @Override
        public CSVTechnologyAdapter getTechnologyAdapter() {
            return (CSVTechnologyAdapter) super.getTechnologyAdapter();
        }

    }
}
