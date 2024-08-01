package org.babyfish.jimmer.sql.ast.impl.mutation.save;

import org.babyfish.jimmer.meta.ImmutableProp;
import org.babyfish.jimmer.sql.DissociateAction;
import org.babyfish.jimmer.sql.OnDissociate;
import org.babyfish.jimmer.sql.ast.impl.mutation.DeleteOptions;
import org.babyfish.jimmer.sql.ast.mutation.DeleteMode;
import org.babyfish.jimmer.sql.event.Triggers;
import org.babyfish.jimmer.sql.runtime.JSqlClientImplementor;

class DeleteOptionsImpl implements DeleteOptions {

    private final JSqlClientImplementor sqlClient;

    private final DeleteMode mode;

    private final DissociateAction dissociateAction;

    DeleteOptionsImpl(
            JSqlClientImplementor sqlClient,
            DeleteMode mode,
            DissociateAction dissociateAction) {
        this.sqlClient = sqlClient;
        this.mode = mode;
        this.dissociateAction = dissociateAction;
    }

    @Override
    public JSqlClientImplementor getSqlClient() {
        return sqlClient;
    }

    @Override
    public DeleteMode getMode() {
        return mode;
    }

    @Override
    public DissociateAction getDissociateAction(ImmutableProp backReferenceProp) {
        if (dissociateAction != DissociateAction.NONE) {
            return dissociateAction;
        }
        OnDissociate onDissociate = backReferenceProp.getAnnotation(OnDissociate.class);
        if (onDissociate == null) {
            return DissociateAction.LAX;
        }
        return onDissociate.value();
    }

    @Override
    public Triggers getTriggers() {
        return null;
    }
}
