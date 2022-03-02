package io.castled.daos;

import io.castled.constants.TableFields;
import io.castled.dtos.querymodel.ModelInputDTO;
import io.castled.dtos.querymodel.QueryModelDetails;
import io.castled.models.QueryModel;
import io.castled.models.QueryModelPK;
import io.castled.models.users.User;
import io.castled.utils.JsonUtils;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@RegisterRowMapper(QueryModelDAO.QueryModelRowMapper.class)
@RegisterArgumentFactory(QueryModelDAO.QueryModelArgumentFactory.class)
@RegisterArgumentFactory(QueryModelDAO.QueryModelPKArgumentFactory.class)
public interface QueryModelDAO {

    @GetGeneratedKeys
    @SqlUpdate("insert into query_model(user_id, team_id,warehouse_id,model_name,model_type,model_details,query_pk,demo)" +
            " values(:user.id, :user.teamId, :modelDTO.warehouseId, :modelDTO.modelName, :modelDTO.modelType," +
            " :modelDTO.modelDetails, :modelDTO.queryModelPK, :modelDTO.demo)")
    long createModel(@BindBean("modelDTO") ModelInputDTO modelDTO, @BindBean("user") User user);

    @SqlQuery("select * from query_model where model_name = :modelName and is_deleted = 0")
    QueryModel getQueryModelByModelName(@Bind("modelName") String modelName);

    @SqlQuery("select * from query_model where id = :id and is_deleted = 0")
    QueryModel getQueryModel(@Bind("id") Long id);

    @SqlQuery("select * from query_model where warehouse_id =:whId and is_deleted = 0")
    List<QueryModel> getQueryModelsByWarehouse(@Bind("whId") Long whId);

    @SqlQuery("select * from query_model where team_id =:teamId and is_deleted = 0")
    List<QueryModel> getQueryModelsByTeam(@Bind("teamId") Long teamId);

    @SqlQuery("select * from query_model where user_id =:userId and is_deleted = 0")
    List<QueryModel> getQueryModelsByUser(@Bind("userId") Long userId);

    @SqlQuery("select * from query_model where warehouse_id =:whId and team_id =:teamId and is_deleted = 0")
    List<QueryModel> getQueryModelsByWarehouseAndTeam(@Bind("whId") Long whId, @Bind("teamId") Long teamId);

    @SqlUpdate("update query_model set is_deleted = 1 where id = :id")
    void deleteModel(@Bind("id") Long id);


    class QueryModelArgumentFactory extends AbstractArgumentFactory<QueryModelDetails> {

        public QueryModelArgumentFactory() {
            super(Types.VARCHAR);
        }

        @Override
        protected Argument build(QueryModelDetails modelDetails, ConfigRegistry config) {
            return (position, statement, ctx) -> statement.setString(position, JsonUtils.objectToString(modelDetails));
        }
    }

    class QueryModelPKArgumentFactory extends AbstractArgumentFactory<QueryModelPK> {

        public QueryModelPKArgumentFactory() {
            super(Types.VARCHAR);
        }

        @Override
        protected Argument build(QueryModelPK queryModelPK, ConfigRegistry config) {
            return (position, statement, ctx) -> statement.setString(position, JsonUtils.objectToString(queryModelPK));
        }
    }

    class QueryModelRowMapper implements RowMapper<QueryModel> {

        @Override
        public QueryModel map(ResultSet rs, StatementContext ctx) throws SQLException {
            QueryModelPK queryModelPK = JsonUtils.jsonStringToObject(rs.getString(TableFields.WAREHOUSE_PK), QueryModelPK.class);
            QueryModelDetails modelDetails = JsonUtils.jsonStringToObject(rs.getString(TableFields.QUERY_MODEL_DETAILS), QueryModelDetails.class);

            return QueryModel.builder().id(rs.getLong(TableFields.ID)).modelName(rs.getString(TableFields.MODEL_NAME))
                    .modelType(rs.getString(TableFields.MODEL_TYPE)).teamId(rs.getLong(TableFields.TEAM_ID))
                    .modelDetails(modelDetails).warehouseId(rs.getLong(TableFields.WAREHOUSE_ID))
                    .queryModelPK(queryModelPK).demo(rs.getBoolean(TableFields.DEMO_MODEL)).build();
        }
    }
}
