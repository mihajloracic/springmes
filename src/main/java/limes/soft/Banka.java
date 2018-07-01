package limes.soft;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Banka {
    String sifBanke;
    String naziv;
    String tekuciRacun;

    int nekiBroj;

    public Banka(){}


    public Banka(String sifBanke, String naziv, String tekuciRacun, int nekiBroj) {
        this.sifBanke = sifBanke;
        this.naziv = naziv;
        this.tekuciRacun = tekuciRacun;
        this.nekiBroj = nekiBroj;
    }

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

    public int getNekiBroj() {
        return nekiBroj;
    }

    public void setNekiBroj(int nekiBroj) {
        this.nekiBroj = nekiBroj;
    }
}


