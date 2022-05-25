package org.babyfish.jimmer.sql.ast.impl.mutation;

import org.babyfish.jimmer.meta.ImmutableProp;
import org.babyfish.jimmer.meta.ImmutableType;
import org.babyfish.jimmer.sql.ImmutableProps;
import org.babyfish.jimmer.sql.CascadeAction;
import org.babyfish.jimmer.sql.SqlClient;
import org.babyfish.jimmer.sql.ast.mutation.DeleteCommand;
import org.babyfish.jimmer.sql.ast.mutation.DeleteResult;
import org.babyfish.jimmer.sql.ast.table.Table;

import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

class DeleteCommandImpl implements DeleteCommand {

    private SqlClient sqlClient;

    private ImmutableType immutableType;

    private Collection<?> ids;

    private Data data;

    public DeleteCommandImpl(
            SqlClient sqlClient,
            ImmutableType immutableType,
            Collection<?> ids
    ) {
        this.sqlClient = sqlClient;
        this.immutableType = immutableType;
        this.ids = ids;
        this.data = new Data(sqlClient).freeze();
    }

    public DeleteCommandImpl(
            DeleteCommandImpl base,
            Data data
    ) {
        this.sqlClient = base.sqlClient;
        this.immutableType = base.immutableType;
        this.ids = base.ids;
        this.data = data.freeze();
    }

    @Override
    public DeleteCommand configure(Consumer<Cfg> block) {
        Data newData = new Data(this.data);
        block.accept(newData);
        if (newData.deleteActionMap.isEmpty()) {
            return this;
        }
        return new DeleteCommandImpl(this, newData);
    }

    @Override
    public DeleteResult execute() {
        return sqlClient
                .getConnectionManager()
                .execute(this::execute);
    }

    @Override
    public DeleteResult execute(Connection con) {
        Deleter deleter = new Deleter(data, con);
        deleter.addPreHandleInput(immutableType, ids);
        return deleter.execute();
    }

    static class Data implements Cfg {

        private SqlClient sqlClient;

        private Map<ImmutableProp, CascadeAction> deleteActionMap;

        private boolean frozen;

        Data(SqlClient sqlClient) {
            this.sqlClient = sqlClient;
            this.deleteActionMap = new LinkedHashMap<>();
        }

        Data(Data base) {
            this.sqlClient = base.sqlClient;
            this.deleteActionMap = new LinkedHashMap<>(base.deleteActionMap);
        }

        public SqlClient getSqlClient() {
            return sqlClient;
        }

        public CascadeAction getDeleteAction(ImmutableProp prop) {
            CascadeAction action = deleteActionMap.get(prop);
            return action != null ? action : prop.getDeleteAction();
        }

        public Data freeze() {
            if (!frozen) {
                deleteActionMap = Collections.unmodifiableMap(deleteActionMap);
                frozen = true;
            }
            return this;
        }

        @Override
        public Cfg setCascadeAction(ImmutableProp prop, CascadeAction cascadeAction) {
            if (frozen) {
                throw new IllegalStateException("The configuration is frozen");
            }

            if (!prop.isReference()) {
                throw new IllegalArgumentException("'" + prop + "' is not reference property");
            }
            if (cascadeAction == CascadeAction.SET_NULL && !prop.isNullable()) {
                throw new IllegalArgumentException(
                        "'" + prop + "' is not nullable so that it does not support 'on delete set null'"
                );
            }
            deleteActionMap.put(prop, cascadeAction);
            return this;
        }

        @Override
        public Cfg setCascadeAction(
                Class<?> entityType,
                String prop,
                CascadeAction cascadeAction
        ) {
            ImmutableType immutableType = ImmutableType.get(entityType);
            ImmutableProp immutableProp = immutableType.getProps().get(prop);
            if (immutableProp == null || !immutableProp.isReference()) {
                throw new IllegalArgumentException(
                        "'" + prop + "' is not reference property of \"" + entityType.getName() + "\""
                );
            }
            return setCascadeAction(immutableProp, cascadeAction);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Table<?>> Cfg setCascadeAction(
                Class<T> tableType,
                Function<T, Table<?>> block,
                CascadeAction cascadeAction
        ) {
            return setCascadeAction(ImmutableProps.join(tableType, block), cascadeAction);
        }
    }
}
