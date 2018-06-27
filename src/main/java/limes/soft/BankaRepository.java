package limes.soft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BankaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly=true)
    public List<Banka> findAll() {
        return jdbcTemplate.query("select * from Banke",
                new BankaRowMapper());
    }
}
class BankaRowMapper implements RowMapper<Banka> {
    @Override
    public Banka mapRow(ResultSet resultSet, int i) throws SQLException {
        Banka banka = new Banka();
        banka.setSifBanke(resultSet.getString("SifBanke"));
        banka.setNaziv(resultSet.getString("Naziv"));
        banka.setTekuciRacun(resultSet.getString("TekuciRacun"));
        return banka;
    }
}