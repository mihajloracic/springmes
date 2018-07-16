package limes.soft.repository;

import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public class IzvestajTroskovaRepository extends GeneralRepository {

    public JSONArray findlAll(){
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList("");
        return getObjects(rows);
    }
}
