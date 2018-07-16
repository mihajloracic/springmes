package limes.soft.repository;

import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public class PrometPoKupcimaRepository extends GeneralRepository {

    public JSONArray findlAll(){
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList("if EXISTS (select * from tempdb..sysobjectswhere id= object_id('tempdb..#ProdajaPoKupA'))drop table #ProdajaPoKupAselect f.SifKomint,f.Datum,f.sifPlacanja,sum(f.iznosKupac)IznosFaktura,sum(f.iznosrabata)Rabat,sum(f.iznosRobe)IznosRobe,sum(f.iznosPoreza) Porezinto #ProdajaPoKupAfrom  fakturaizzag fleft outer join V_komintenti_ptt k on f.sifkomint=k.sifkomintgroup by  f.sifkomint,f.datum,f.sifplacanjaselect SKD.*, k.NazivKomint,k.Mesto,s.Naziv as NazivPlacanjafrom #ProdajaPoKupA SKD, V_komintenti_ptt k ,sifplacanja swhere SKD.sifkomint=k.sifkomint and skd.Sifplacanja=s.sifplacanjaorder by skd.sifkomint,skd.Datum ");
        return getObjects(rows);
    }
}
