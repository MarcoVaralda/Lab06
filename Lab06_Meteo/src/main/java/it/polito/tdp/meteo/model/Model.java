package it.polito.tdp.meteo.model;

import java.time.LocalDate;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO dao = new MeteoDAO();
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		
		List<Rilevamento> rilevamentiTorino = dao.getAllRilevamentiLocalitaMese(mese, "Torino");
		List<Rilevamento> rilevamentiMilano = dao.getAllRilevamentiLocalitaMese(mese, "Milano");
		List<Rilevamento> rilevamentiGenova = dao.getAllRilevamentiLocalitaMese(mese, "Genova");
		
		String risultato = "Citta Torino: umidita = " +calcolaMediaUmidita(rilevamentiTorino) +"\n" +"Citta Milano: umidita = " +calcolaMediaUmidita(rilevamentiMilano) +"\n" +"Citta Genova: umidita = " +calcolaMediaUmidita(rilevamentiGenova);
		
		return risultato;
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		return "TODO!";
	}
	
	public double calcolaMediaUmidita(List<Rilevamento> rilevamenti) {
		double somma = 0.0;
		for(Rilevamento r : rilevamenti)
			somma = somma + r.getUmidita();
		return (somma/(rilevamenti.size()));
	}
	

}
