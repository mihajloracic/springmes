package limes.soft;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class DBRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JSONArray findlAllPregledTroskova(String startDate, String endDate){
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList("select f.* ,n.* ,k.NazivKonta\n" +
                "from nalogZaKnjizenjeFin n, KontniPlan k, finansijskaKartica f\n" +
                "where f.rbrnaloga = n.rbrnaloga \n" +
                "and f.Konto = k.Konto and n.Storno<>1\n" +
                "and f.Konto like '5%' \n" +
                "and n.DatumNaloga<= ?                \n" +
                "and n.DatumNaloga>= ?\n" +
                "order by  n.DatumNaloga",endDate,startDate);
        return getObjects(rows);
    }
    public JSONArray findAllmasterdetailGetDetails(String param){
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList("declare @rbr int\n" +
                "select @rbr= ? \n" +
                "\n" +
                "select  godina,BrUgovora,VR, Rbr,Datum,Valuta,\n" +
                "IznosRobe,IznosPoreza,IznosRabata,IznosKupac,\n" +
                "IznosAvansa,RbrTendera,SifKomint\n" +
                "from FaktureUgovoriOld\n" +
                "where BrUgovora=@rbr\n" +
                "union all\n" +
                "select year(Datum) as godina,BrUgovora,'RN'as VR,RbrIzFakture as Rbr,Datum,Valuta,\n" +
                "IznosRobe,IznosPoreza,IznosRabata,IznosKupac,\n" +
                "IznosAvansa,RbrTendera,SifKomint\n" +
                "from FakturaIzzag\n" +
                "where BrUgovora=@rbr\n" +
                "and (storno is null or storno=0)\n" +
                "union all\n" +
                "select year(Datum) as godina,BrUgovoraK as BrUgovora,'TR'as VR,RbrTKalk as Rbr,Datum,ValutaRacun as Valuta,\n" +
                "VpVrednostRacun as IznosRobe,PorezRacun as IznosPoreza,\n" +
                "RabatRacun as IznosRabata,IznosZaUplatu as IznosKupac,\n" +
                "IznosAvansa,RbrTendera, SifKomintKupac\n" +
                "from TranzitniKalkRacun\n" +
                "where BrUgovoraK=@rbr\n" +
                "and (storno is null or storno=0)\n" +
                "union all\n" +
                "select year(Datum) as godina,RbrUgovora as BrUgovra,'RU'as VR,RbrIzFakture as Rbr,Datum,Valuta,\n" +
                "Osnovica,IznosPoreza,0 as IznosRabata,IznosKupac,\n" +
                "IznosAvansa,RbrTendera, SifKomint\n" +
                "from FakturaIzzagUsl\n" +
                "where RBrUgovora=@rbr\n" +
                "and (storno is null or storno=0)\n" +
                "union all\n" +
                "select year(Datum) as godina,BrUgovora,'KL'as VR,RbrKnjLista as Rbr,Datum,Datum as Valuta,\n" +
                "IznosRobe,IznosPorez as znosPoreza,IznosRabat as IznosRabata, IznosDokumenta as IznosKupac,\n" +
                "0 as IznosAvansa,0  as RbrTendera,SifKomint\n" +
                "from KnjizniListRobni\n" +
                "where BrUgovora=@rbr\n" +
                "and (storno is null or storno=0)",param);
        return getObjects(rows);
    }
    public JSONArray findlAllmasterdetail(){
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList("select u.BrUgovora,u.SifKomint,k.NazivKomint,k.Mesto,u.Datum,u.DatumRok,u.RokUgovora,\n" +
                "u.TipDokumenta,u.RbrTendera,\n" +
                "u.VrstaObezbedjenja,u.RokValjanosti,u.VrObezbedjenja as VrednostObezbedjenja,\n" +
                "u.Valuta,u.NapomenaVal,u.VrstaUgovRobe,\n" +
                "isnull(u.VpVrUgovora,0) as VpVrednostUgovora,isnull(u.UkVrUgovora,0) as UkupnaVrednostUgovora,\n" +
                "DelovodiBrojKomitenta,DatumDelovodnogBrojaK,DelovodiBrojNas,DatumDelovodnogBrojaN,\n" +
                "(select uu.status\n" +
                "from ugovori uu\n" +
                "where uu.BrUgovora=u.BrUgovora) status,\n" +
                "(\n" +
                "(select isnull(convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  FaktureUgovoriOLD f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                ")\n" +
                "+\n" +
                " (select isnull(convert(money,sum(ff.Osnovica)),0)\n" +
                "     from  FakturaIzzagUsl ff\n" +
                "  where ff.RBrUgovora=u.brUgovora\n" +
                "and (ff.storno is null or ff.storno=0))\n" +
                " +\n" +
                " ( select isnull(convert(money,sum(ft.VpVrednostRacun)),0)\n" +
                "     from  TranzitniKalkRacun ft\n" +
                "  where ft.BrUgovoraK=u.brUgovora\n" +
                "and (ft.storno is null or ft.storno=0))\n" +
                "+\n" +
                " (select  isnull( convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  FakturaIzzag f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "and (f.storno is null or f.storno=0))\n" +
                "+\n" +
                "(select  isnull( convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  KnjizniListRobni f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "and (f.storno is null or f.storno=0))\n" +
                ")RacuniVpVrednost,\n" +
                "(\n" +
                "isnull(u.VpVrUgovora,0)\n" +
                "-\n" +
                "(\n" +
                "(select isnull(convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  FaktureUgovoriOLD f\n" +
                "  where f.BrUgovora=u.brUgovora)\n" +
                "+\n" +
                " (select isnull(convert(money,sum(ff.Osnovica)),0)\n" +
                "     from  FakturaIzzagUsl ff\n" +
                "  where ff.RBrUgovora=u.brUgovora\n" +
                "and (ff.storno is null or ff.storno=0))\n" +
                " +\n" +
                " ( select isnull(convert(money,sum(ft.VpVrednostRacun)),0)\n" +
                "     from  TranzitniKalkRacun ft\n" +
                "  where ft.BrUgovoraK=u.brUgovora\n" +
                "and (ft.storno is null or ft.storno=0))\n" +
                "+\n" +
                " (select  isnull( convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  FakturaIzzag f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "and (f.storno is null or f.storno=0))\n" +
                "+\n" +
                "(select  isnull( convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  KnjizniListRobni f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "and (f.storno is null or f.storno=0))\n" +
                ")\n" +
                ")RazlikaVpVrednost,\n" +
                "\n" +
                "(\n" +
                "(select isnull( convert(money,sum(f.IznosKupac)),0)\n" +
                "     from  FaktureUgovoriOLD f\n" +
                "  where f.BrUgovora=u.brUgovora)\n" +
                "+\n" +
                "(select  isnull( convert(money,sum(ff.IznosKupac)),0)\n" +
                "     from  FakturaIzzagUsl ff\n" +
                "  where ff.RBrUgovora=u.brUgovora\n" +
                "  and (ff.storno is null or ff.storno=0))\n" +
                " +\n" +
                " ( select isnull( convert(money,sum(ft.IznosZaUplatu)),0)\n" +
                "     from  TranzitniKalkRacun ft\n" +
                "  where ft.BrUgovoraK=u.brUgovora\n" +
                "  and (ft.storno is null or ft.storno=0))\n" +
                "+\n" +
                " (select isnull( convert(money,sum(f.IznosKupac)),0)\n" +
                "     from  FakturaIzzag f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "  and (f.storno is null or f.storno=0))\n" +
                "+\n" +
                "(select  isnull( convert(money,sum(f.IznosDokumenta)),0)\n" +
                "     from  KnjizniListRobni f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "  and (f.storno is null or f.storno=0))\n" +
                ")RacuniUkupnaVrednost,\n" +
                "(\n" +
                "isnull(u.UkVrUgovora,0)\n" +
                "-\n" +
                "(\n" +
                "(select isnull( convert(money,sum(f.IznosKupac)),0)\n" +
                "     from  FaktureUgovoriOLD f\n" +
                "  where f.BrUgovora=u.brUgovora)\n" +
                "+\n" +
                "(select  isnull( convert(money,sum(ff.IznosKupac)),0)\n" +
                "     from  FakturaIzzagUsl ff\n" +
                "  where ff.RBrUgovora=u.brUgovora\n" +
                "  and (ff.storno is null or ff.storno=0))\n" +
                " +\n" +
                " ( select isnull( convert(money,sum(ft.IznosZaUplatu)),0)\n" +
                "     from  TranzitniKalkRacun ft\n" +
                "  where ft.BrUgovoraK=u.brUgovora\n" +
                "  and (ft.storno is null or ft.storno=0))\n" +
                "+\n" +
                " (select isnull( convert(money,sum(f.IznosKupac)),0)\n" +
                "     from  FakturaIzzag f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "  and (f.storno is null or f.storno=0))\n" +
                "+\n" +
                "(select  isnull( convert(money,sum(f.IznosDokumenta)),0)\n" +
                "     from  KnjizniListRobni f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "  and (f.storno is null or f.storno=0))\n" +
                ")\n" +
                "\n" +
                ")RazlikaUkupnaVrednost,\n" +
                "\n" +
                "(select isnull(sum(i.JosOstalo * i.Cena),0)\n" +
                "    from IzjavaKupcaSta i , IzjavaKupcaZag iz\n" +
                "    where i.RbrIzjave=iz.RbrIzjave\n" +
                "          and i.JosOstalo>0\n" +
                "          and iz.BrUgovora=u.BrUgovora)OstaloZaRealizacijuIzjave,\n" +
                "(select isnull(sum(i.JosOstalo * i.Cena),0)\n" +
                "    from GeneralnaIzjavaSta i , GeneralnaIzjavaZag iz\n" +
                "    where i.RbrIzjave=iz.RbrIzjave\n" +
                "          and i.JosOstalo>0\n" +
                "          and iz.BrUgovora=u.BrUgovora)OstaloZaRealizacijuGodIzjave,\n" +
                "(select isnull(sum(i.JosOstalo * i.Cena),0)\n" +
                "    from PredracunSta i , PredracunZag iz\n" +
                "    where i.RbrPredracuna=iz.RbrPredracuna\n" +
                "          and i.JosOstalo>0\n" +
                "          and iz.BrUgovora=u.BrUgovora)OstaloZaRealizacijuPredracuna,\n" +
                "-- RazlikaVpVrednost-OstaloZaRealizacijuIzjave-OstaloZaRealizacijuGodIzjave-OstaloZaRealizacijuPredracuna\n" +
                "((\n" +
                "isnull(u.VpVrUgovora,0)\n" +
                "-\n" +
                "(\n" +
                "(select isnull(convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  FaktureUgovoriOLD f\n" +
                "  where f.BrUgovora=u.brUgovora)\n" +
                "+\n" +
                " (select isnull(convert(money,sum(ff.Osnovica)),0)\n" +
                "     from  FakturaIzzagUsl ff\n" +
                "  where ff.RBrUgovora=u.brUgovora\n" +
                "and (ff.storno is null or ff.storno=0))\n" +
                " +\n" +
                " ( select isnull(convert(money,sum(ft.VpVrednostRacun)),0)\n" +
                "     from  TranzitniKalkRacun ft\n" +
                "  where ft.BrUgovoraK=u.brUgovora\n" +
                "and (ft.storno is null or ft.storno=0))\n" +
                "+\n" +
                " (select  isnull( convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  FakturaIzzag f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "and (f.storno is null or f.storno=0))\n" +
                "+\n" +
                "(select  isnull( convert(money,sum(f.IznosRobe)),0)\n" +
                "     from  KnjizniListRobni f\n" +
                "  where f.BrUgovora=u.brUgovora\n" +
                "and (f.storno is null or f.storno=0))\n" +
                ")\n" +
                "\n" +
                "-\n" +
                "(select isnull(sum(i.JosOstalo * i.Cena),0)\n" +
                "    from IzjavaKupcaSta i , IzjavaKupcaZag iz\n" +
                "    where i.RbrIzjave=iz.RbrIzjave\n" +
                "          and i.JosOstalo>0\n" +
                "          and iz.BrUgovora=u.BrUgovora)\n" +
                "-\n" +
                "(select isnull(sum(i.JosOstalo * i.Cena),0)\n" +
                "    from GeneralnaIzjavaSta i , GeneralnaIzjavaZag iz\n" +
                "    where i.RbrIzjave=iz.RbrIzjave\n" +
                "          and i.JosOstalo>0\n" +
                "          and iz.BrUgovora=u.BrUgovora)\n" +
                "-\n" +
                "(select isnull(sum(i.JosOstalo * i.Cena),0)\n" +
                "    from PredracunSta i , PredracunZag iz\n" +
                "    where i.RbrPredracuna=iz.RbrPredracuna\n" +
                "          and i.JosOstalo>0\n" +
                "          and iz.BrUgovora=u.BrUgovora)\n" +
                "\n" +
                "))KontrolaRealizacije,\n" +
                "(\n" +
                "(select isnull(convert(money,sum(f.Osnovica)),0)\n" +
                "     from  FakturaUlZagOld f\n" +
                "  where (f.BrUgovora=u.brUgovora or f.BrUgovora2=u.brUgovora ) and f.storno=0\n" +
                ")\n" +
                "+\n" +
                " (select isnull(convert(money,sum(ff.Osnovica)),0)\n" +
                "     from  FakturaUlZag ff\n" +
                "  where (ff.BrUgovora=u.brUgovora or ff.BrUgovora2=u.brUgovora) \n" +
                "and (ff.storno is null or ff.storno=0))\n" +
                ") as UlazniRnVrBezPDV,\n" +
                "\n" +
                "(\n" +
                "(select isnull(convert(money,sum(f.VrednostZaUplatu)),0)\n" +
                "     from  FakturaUlZagOld f\n" +
                "  where (f.BrUgovora=u.brUgovora or f.BrUgovora2=u.brUgovora ) and f.storno=0\n" +
                ")\n" +
                "+\n" +
                " (select isnull(convert(money,sum(ff.VrednostZaUplatu)),0)\n" +
                "     from  FakturaUlZag ff\n" +
                "  where (ff.BrUgovora=u.brUgovora or ff.BrUgovora2=u.brUgovora) \n" +
                "and (ff.storno is null or ff.storno=0))\n" +
                ") as UlazniRnUkupno,\n" +
                "\n" +
                "pon.Referent as PonReferent,pr.Ime as ImePonReferent,\n" +
                " ten.referent as TenReferent,tr.Ime as ImeTenReferent,\n" +
                "u.RbrPonude,pon.BrojPonude,pon.Datum as datumPonude,\n" +
                "(select k.Supervizor \n" +
                "  from Komintenti k \n" +
                "where k.SifKomint=u.SifKomint) as Supervizor,\n" +
                "(select rrk.Ime \n" +
                "  from Komintenti k\n" +
                "  left outer join Referenti rrk\n" +
                "  on K.Supervizor=rrk.SifRadnika\n" +
                "  where k.SifKomint=u.SifKomint\n" +
                ") as ImeSupervizora\n" +
                "\n" +
                " from V_Komintenti_ptt k, Ugovori u\n" +
                "  left outer join Tenderi ten\n" +
                "     on u.RbrTendera=ten.RbrTendera\n" +
                "  left outer join PonudaZag pon\n" +
                "    on u.RbrPonude=pon.RbrPonude\n" +
                "  left outer join Referenti pr\n" +
                "    on pon.Referent=pr.SifRadnika\n" +
                "  left outer join Referenti tr\n" +
                "    on ten.Referent=tr.SifRadnika\n" +
                "where  u.SifKomint=k.SifKomint\n" +
                "order by u.BrUgovora");
        return getObjects(rows);
    }
    public JSONArray findlAllKupcidobavljaci(){
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList("if EXISTS (select * from tempdb..sysobjects\n" +
                "where id= object_id('tempdb..#SintetikaKUDOB'))\n" +
                "drop table #SintetikaKUDOB\n" +
                "if EXISTS (select * from tempdb..sysobjects\n" +
                "where id= object_id('tempdb..#KrosTabKUDOB'))\n" +
                "drop table #KrostabKUDOB\n" +
                "CREATE TABLE [#KrosTabKuDob] (\n" +
                "    SifKomint char(6) NULL ,\n" +
                "    k14000 [Money] NULL default 0 ,\n" +
                "    k20100 Money   NULL default 0,\n" +
                "    k43000 money  NULL default 0,\n" +
                "    k432xx money  NULL default 0,\n" +
                "    )\n" +
                "\n" +
                "select f.konto,f.sifkomint,  sum(f.duguje) SUMDUG, sum(f.potrazuje) SUMPOT,sum(duguje-Potrazuje) SALDO-- ,k.NazivKonta, Kom.NazivKomint,kom.Mesto\n" +
                "into #SintetikaKUDOB\n" +
                "from  KontniPlan k, nalogzaknjizenjefin NF,finansijskaKartica f \n" +
                "left outer join V_Komintenti_ptt Kom \n" +
                "on f.SifKomint=Kom.SifKomint\n" +
                "where   f.Konto = k.Konto and NF.storno<>1 \n" +
                "and f.RbrNaloga=NF.RbrNaloga\n" +
                "and k.Komint=1\n" +
                "group by  f.konto,f.sifkomint\n" +
                "order by F.SifKomint\n" +
                "--select * from #SintetikaKUDOB\n" +
                "\n" +
                "insert into #KrosTabKuDob(\n" +
                "       SifKomint\n" +
                ")\n" +
                "select distinct SifKomint from  #SintetikaKuDOB\n" +
                "\n" +
                "update #KrosTabKuDob \n" +
                "set K14000=(select sum(sk.Saldo) \n" +
                "     from #SintetikaKuDob sk\n" +
                "    where #KrosTabKuDob.SifKomint=sk.SifKomint\n" +
                "        and sk.Konto in ('15000','15100','15200','15300','15400','15500')),\n" +
                "    K20100=(select sum(sk.Saldo) \n" +
                "     from #SintetikaKuDob sk\n" +
                "    where #KrosTabKuDob.SifKomint=sk.SifKomint\n" +
                "        and sk.Konto in ('20400',  '20000','20200','20500','20300') ),\n" +
                "    K43000=(select  sum(sk.Saldo)\n" +
                "     from #SintetikaKuDob sk\n" +
                "    where #KrosTabKuDob.SifKomint=sk.SifKomint\n" +
                "        and sk.Konto='43000' ),\n" +
                "    K432xx=(select sum(sk.Saldo) \n" +
                "     from #SintetikaKuDob sk\n" +
                "    where #KrosTabKuDob.SifKomint=sk.SifKomint\n" +
                "        and sk.Konto in ('43300', '43310', '43400','43100','43110','43500', '43510','43600','43610'))\n" +
                "        \n" +
                " update #KrosTabKuDob\n" +
                "   set K14000=0 \n" +
                "  where K14000 is null\n" +
                "\n" +
                " update #KrosTabKuDob\n" +
                "   set K43000=0 \n" +
                "  where K43000 is null\n" +
                "\n" +
                " update #KrosTabKuDob\n" +
                "   set K20100=0 \n" +
                "  where K20100 is null\n" +
                "\n" +
                " update #KrosTabKuDob\n" +
                "   set K432xx=0 \n" +
                "  where K432xx is null\n" +
                "              \n" +
                "select KTB.* ,Kom.Nazivkomint,Kom.Mesto,(K14000 +  K20100 +  k43000 +  k432xx) Saldo, \n" +
                "kom.Referent,kom.ReferentDob,kom.StatusKom,kom.SifZemlje, r.Ime, rr.Ime as ReferentDobavljaca,\n" +
                "kom.Komentar as Komentar, kom.KreditniLimit,r.Referada as Poslovnica,kom.PIB,\n" +
                "(select k.Supervizor \n" +
                "  from Komintenti k \n" +
                "where k.SifKomint=KTB.SifKomint) as Supervizor,\n" +
                "(select rrk.Ime \n" +
                "  from Komintenti k\n" +
                "  left outer join Referenti rrk\n" +
                "  on K.Supervizor=rrk.SifRadnika\n" +
                "  where k.SifKomint=KTB.SifKomint\n" +
                ") as ImeSupervizora\n" +
                "from #KrosTabKuDOB KTB, V_Komintenti_ptt Kom\n" +
                "left outer join Referenti r\n" +
                "on Kom.Referent=r.SifRadnika\n" +
                "left outer join Referenti rr\n" +
                "on Kom.ReferentDob=rr.SifRadnika\n" +
                "where KTB.SifKomint=kom.SifKomint\n" +
                "order by NazivKomint");
        return getObjects(rows);
    }

    private JSONArray getObjects(List<Map<String, Object>> rows) {
        JSONArray json_arr=new JSONArray();
        for (Map<String, Object> map : rows) {
            JSONObject json_obj=new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                try {
                    json_obj.put(key,value);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            json_arr.put(json_obj);
        }
        return json_arr;
    }
}