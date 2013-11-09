package util;

public class Consts {
	
	public enum Layer {
	    REGIONE,PROVINCIA,COMUNE,CAP
	}
	
	
	public static String user = "root";
	public static String passwd = "labcad";
	
	//public static final String queryBasePv="SELECT IF( rpp.tc_clpot_id =1, rpp.valore,  NULL ) AS potenziale,cod_cliente, pv.pv_id, cod_mmas, nome1 AS ragione_sociale, indirizzo, cap, comune, provincia, tel1 AS telefono, cf_pi AS codice_fiscale,nome2 as titolare,fax,cliente FROM pv JOIN rel_pv_pot rpp ON rpp.pv_id = pv.pv_id JOIN tc_clpot tc ON tc.tc_clpot_id = rpp.tc_clpot_id where ";
	//public static final String queryBasePv_id="SELECT   pv.pv_id as id FROM pv JOIN rel_pv_pot rpp ON rpp.pv_id = pv.pv_id JOIN tc_clpot tc ON tc.tc_clpot_id = rpp.tc_clpot_id where ";
	//public static final String queryExpandRegion="SELECT sigla FROM provincia JOIN regione_provincia rel ON provincia.id = rel.provincia_id JOIN regione r ON r.regione_id = rel.regione_id WHERE r.regione = '";
	//public static final String queryCountPv="SELECT pv.pv_id as id  FROM pv JOIN rel_pv_pot rpp ON rpp.pv_id = pv.pv_id JOIN tc_clpot tc ON tc.tc_clpot_id = rpp.tc_clpot_id where ";
	
	public static final String RefactoredQueryBasePv = "SELECT DISTINCT  IF(rel_pv_pot.tc_clpot_id=1, rel_pv_pot.valore, NULL) AS potenziale,cod_cliente, pv.pv_id,codice as cod_mmas, nome1 AS ragione_sociale, indirizzo, cap, comune, provincia, tel1 AS telefono, cf_pi AS codice_fiscale, nome2 AS titolare, fax, cliente FROM pv left JOIN rel_pv_pot ON pv.pv_id = rel_pv_pot.pv_id WHERE";
	public static final String QueryFilteredPv = "SELECT DISTINCT IF(rel_pv_pot.tc_clpot_id=1, rel_pv_pot.valore, NULL) AS potenziale,cod_cliente, pv.pv_id,codice as cod_mmas, nome1 AS ragione_sociale, indirizzo, cap, comune, provincia, tel1 AS telefono, cf_pi AS codice_fiscale, nome2 AS titolare, fax, cliente FROM pv left JOIN rel_pv_pot ON pv.pv_id = rel_pv_pot.pv_id left JOIN rel_pv_par ON pv.pv_id = rel_pv_par.pv_id left JOIN rel_pv_mar ON pv.pv_id = rel_pv_mar.pv_id WHERE";
	public static final String queryIstatRegioni = "SELECT tc_istat_id FROM mmasgisDB.tc_istat JOIN mmasgisDB.regioni ON tc_istat.tc_regione_id = regioni.tc_regione_id WHERE regioni.codice = ";
	public static final String queryIstatProvince = "SELECT tc_istat_id FROM mmasgisDB.tc_istat JOIN mmasgisDB.province ON tc_istat.tc_provincia_id = province.tc_provincia_id WHERE province.codice = ";
	public static final String queryIstatComuni = "SELECT tc_istat_id FROM mmasgisDB.tc_istat JOIN mmasgisDB.comuni ON tc_istat.tc_comune_id = comuni.tc_comune_id WHERE comuni.codice = ";
	
	public static final String queryBrandsAnalysisView = "CREATE OR REPLACE VIEW selezione_geografica AS SELECT DISTINCT pv.pv_id, rpp.valore FROM pv JOIN rel_pv_pot rpp ON pv.pv_id = rpp.pv_id JOIN rel_pv_mar rpm ON pv.pv_id = rpm.pv_id WHERE tc_clpot_id = 1 AND ";
	public static final String queryBrandsAnalysisFirst = "SELECT count(pv_id) AS base_num, round(sum(valore), 3) AS base_pot FROM selezione_geografica";
	public static final String queryBrandsAnalysisList = "SELECT tc_mar.testo AS testo, tc_mar.tc_mar_id AS tc_mar_id FROM tc_mar JOIN tc_rel_clmar_mar rel ON rel.tc_mar_id = tc_mar.tc_mar_id WHERE tc_clmar_id = ";
	public static final String queryBrandsAnalysisSingle = "SELECT count(sel.pv_id) AS num_pv, IF(round(sum(valore), 3) IS NOT NULL, round(sum(valore), 3), 0) AS pot_pv FROM selezione_geografica sel JOIN rel_pv_mar rpm ON sel.pv_id = rpm.pv_id WHERE rpm.tc_clmar_id = %s AND rpm.tc_mar_id = %s";
	public static final String queryBrandsAnalysisPot = "SELECT IF(sum(sel.valore/100*rpm.uso) IS NOT NULL, round(sum(sel.valore/100*rpm.uso),3), 0) AS pot_marca FROM rel_pv_mar rpm JOIN selezione_geografica sel ON sel.pv_id = rpm.pv_id WHERE tc_clmar_id = %s AND tc_mar_id = %s";
	
	public static String queryClassParameters = "SELECT tc_clpar_id AS id_parametro, testo AS parametro FROM tc_clpar ORDER BY tc_clpar.ordine ASC";
	public static String queryParameters = "SELECT testo AS valore FROM tc_par t JOIN rel_pv_par rpp ON t.tc_par_id = rpp.tc_par_id WHERE rpp.tc_clpar_id=%s and rpp.pv_id=%s";
	public static String queryPotentials = "SELECT tccl.testo as potenziale, tcm.testo as fascia, rpm.valore FROM pv p, tc_pot tcm, rel_pv_pot rpm, tc_clpot tccl WHERE rpm.tc_pot_id=tcm.tc_pot_id AND rpm.tc_clpot_id=tccl.tc_clpot_id AND p.pv_id=rpm.pv_id AND p.pv_id=%d and tccl.tc_stato_id=1";
	public static String queryBrands = "SELECT DISTINCT tcl.testo AS classificazione, tcl.tc_clmar_id AS id FROM tc_clmar tcl WHERE tcl.tc_stato_id=1 ORDER BY tcl.ordine";
	public static String queryBrandsValues = "SELECT distinct  tccl.testo as classificazione,tcm.testo as valore  FROM rel_pv_mar rpm inner join tc_mar tcm on rpm.tc_mar_id=tcm.tc_mar_id inner join tc_clmar tccl on rpm.tc_clmar_id=tccl.tc_clmar_id inner join pv p on p.pv_id=rpm.pv_id  inner join tc_clmar tcl where p.pv_id=%d  and tcl.tc_stato_id=1 and rpm.tc_clmar_id=%d order by tccl.ordine";
	public static String queryValuesClassPar = "SELECT tc_%s_id as id,tc_%s.testo as text,tc_%s.tc_cl%s_id as class_id, tc_cl%s.testo as class_text from tc_%s join tc_cl%s on tc_%s.tc_cl%s_id=tc_cl%s.tc_cl%s_id where tc_%s.tc_cl%s_id=%s  order by tc_%s.ordine asc"	;
	public static String queryValueClassMar = "SELECT tcrcm.tc_clmar_id as class_id,tc_mar.tc_mar_id as id,tc_clmar.testo as class_text, tc_mar.testo as text from tc_rel_clmar_mar tcrcm join tc_mar on tc_mar.tc_mar_id=tcrcm.tc_mar_id join tc_clmar on tc_clmar.tc_clmar_id=tcrcm.tc_clmar_id where tcrcm.tc_clmar_id=%s";
	public static String queryClass = "SELECT tc_cl%s_id as id, testo as text from tc_cl%s order by ordine asc";
	

}