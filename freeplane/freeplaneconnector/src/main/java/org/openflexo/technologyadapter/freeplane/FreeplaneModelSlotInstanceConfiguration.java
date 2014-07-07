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

package org.openflexo.technologyadapter.freeplane;

import java.util.Collections;
import java.util.List;

import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public class FreeplaneModelSlotInstanceConfiguration extends FreeModelSlotInstanceConfiguration<IFreeplaneMap, IFreeplaneModelSlot> {

    protected FreeplaneModelSlotInstanceConfiguration(final IFreeplaneModelSlot ms, final CreateVirtualModelInstance action) {
        super(ms, action);
    }

    @Override
    public void setOption(final ModelSlotInstanceConfigurationOption option) {
        super.setOption(option);
    }

    /**
     * @return empty list
     */
    @Override
    public List<ModelSlotInstanceConfigurationOption> getAvailableOptions() {
        return Collections.emptyList();
    }

}
