package limes.soft;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Banka {
    String sifBanke;
    String naziv;
    String tekuciRacun;

    public String getSifBanke() {
        return sifBanke;
    }

    public void setSifBanke(String sifBanke) {
        this.sifBanke = sifBanke;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTekuciRacun() {
        return tekuciRacun;
    }

    public void setTekuciRacun(String tekuciRacun) {
        this.tekuciRacun = tekuciRacun;
    }
}


