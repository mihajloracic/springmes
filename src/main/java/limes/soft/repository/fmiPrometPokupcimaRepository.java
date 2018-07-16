package limes.soft.repository;

import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public class fmiPrometPokupcimaRepository extends GeneralRepository {

    public JSONArray findlAll(){
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList("if EXISTS (select * from tempdb..sysobjects where id= object_id('tempdb..#ProdajaPoKupA')) drop table #ProdajaPoKupA select f.SifKomint,f.Datum,f.sifPlacanja,sum(f.iznosKupac)IznosFaktura,sum(f.iznosrabata)Rabat,sum(f.iznosRobe)IznosRobe,sum(f.iznosPoreza) Porez into #ProdajaPoKupA from  fakturaizzag f left outer join V_komintenti_ptt k  on f.sifkomint=k.sifkomint group by  f.sifkomint,f.datum,f.sifplacanja  select SKD.*, k.NazivKomint,k.Mesto,s.Naziv as NazivPlacanja from #ProdajaPoKupA SKD, V_komintenti_ptt k ,sifplacanja s where SKD.sifkomint=k.sifkomint and skd.Sifplacanja=s.sifplacanja order by skd.sifkomint,skd.Datum  ");
        return getObjects(rows);
    }
}
