package org.openflexo.technologyadapter.freeplane.model.actions;

import java.util.Vector;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.ActionGroup;
import org.openflexo.foundation.action.ActionMenu;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

public class AddChildNode extends FlexoAction<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

    private static final class AddChildNodeActionType extends FlexoActionType<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

        protected AddChildNodeActionType(final String actionName, final ActionMenu actionMenu, final ActionGroup actionGroup,
                final int actionCategory) {
            super(actionName, actionMenu, actionGroup, actionCategory);
        }

        @Override
        public AddChildNode makeNewAction(final IFreeplaneNode node, final Vector<IFreeplaneMap> maps, final FlexoEditor flexoEditor) {
            return new AddChildNode(node, maps, flexoEditor);
        }

        @Override
        public boolean isVisibleForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> map) {
            return node != null && node.getNodeModel().isVisible();
        }

        @Override
        public boolean isEnabledForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> map) {
            return node != null && !node.getNodeModel().getMap().isReadOnly();
        }
    }

    public static final ActionMenu FREEPLANE_MENU = new ActionMenu("freeplane_actions", FlexoActionType.defaultGroup);

    public static final FlexoActionType<AddChildNode, IFreeplaneNode, IFreeplaneMap> actionType = new AddChildNodeActionType(
            "add_child_node", FREEPLANE_MENU, FlexoActionType.editGroup, FlexoActionType.ADD_ACTION_TYPE);

    public AddChildNode(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    static {
        FlexoObjectImpl.addActionForClass(actionType, IFreeplaneNode.class);
    }

    @Override
    protected void doAction(final Object objet) throws FlexoException {
        // Some Copy-paste from freeplane To allow us to update our model.
        final ModeController modeController = Controller.getCurrentModeController();
        final MMapController mapController = (MMapController) modeController.getMapController();
        final NodeModel child = mapController.addNewNode(MMapController.NEW_CHILD);
        this.getFocusedObject().addChild(child);
        this.getFocusedObject().setModified(true);
    }

}