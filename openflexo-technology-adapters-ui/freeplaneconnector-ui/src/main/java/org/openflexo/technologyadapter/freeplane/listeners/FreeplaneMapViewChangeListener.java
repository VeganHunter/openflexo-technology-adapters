/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane.listeners;

import org.freeplane.features.map.IMapChangeListener;
import org.freeplane.features.map.MapChangeEvent;
import org.freeplane.features.map.NodeModel;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public class FreeplaneMapViewChangeListener implements IMapChangeListener {

    private final IFreeplaneMap bindedMap;

    public FreeplaneMapViewChangeListener(final IFreeplaneMap bindedMap) {
        super();
        this.bindedMap = bindedMap;
    }

    private void propagateChanges() {
        this.bindedMap.setModified(true);
    }

    @Override
    public void mapChanged(final MapChangeEvent event) {
        propagateChanges();
    }

    @Override
    public void onNodeDeleted(final NodeModel parent, final NodeModel child, final int index) {
        propagateChanges();
    }

    @Override
    public void onNodeInserted(final NodeModel parent, final NodeModel child, final int newIndex) {
        propagateChanges();
    }

    @Override
    public void onNodeMoved(final NodeModel oldParent, final int oldIndex, final NodeModel newParent, final NodeModel child,
            final int newIndex) {
        propagateChanges();
    }

    @Override
    public void onPreNodeMoved(final NodeModel oldParent, final int oldIndex, final NodeModel newParent, final NodeModel child,
            final int newIndex) {
        // Nothing to do in our case here.
    }

    @Override
    public void onPreNodeDelete(final NodeModel oldParent, final NodeModel selectedNode, final int index) {
        // Nothing to do in our case here.
    }

}