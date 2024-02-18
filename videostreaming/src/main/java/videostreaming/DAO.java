package videostreaming;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class DAO {
    protected JdbcTemplate jdbcTemplate;

    public DAO() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        this.jdbcTemplate = new JdbcTemplate(dataSourceConfig.getDataSource());
    }

}
