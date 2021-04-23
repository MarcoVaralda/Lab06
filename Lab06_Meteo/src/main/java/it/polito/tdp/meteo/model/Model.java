package it.polito.tdp.meteo.model;

import java.time.LocalDate;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO dao = new MeteoDAO();
	List<String> soluzioneMigliore;
	int costoMinimoCorrente=500000;
	
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
	
	public double calcolaMediaUmidita(List<Rilevamento> rilevamenti) {
		double somma = 0.0;
		for(Rilevamento r : rilevamenti)
			somma = somma + r.getUmidita();
		return (somma/(rilevamenti.size()));
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		
		List<String> parziale = new LinkedList<String>();
		
		List<Citta> listaCitta = new LinkedList<Citta>();
		Citta Torino = new Citta("Torino",dao.getAllRilevamentiLocalitaMese(mese, "Torino"));
		Citta Milano = new Citta("Milano",dao.getAllRilevamentiLocalitaMese(mese, "Milano"));
		Citta Genova = new Citta("Genova",dao.getAllRilevamentiLocalitaMese(mese, "Genova"));
		listaCitta.add(Torino);
		listaCitta.add(Milano);
		listaCitta.add(Genova);
		
		soluzioneMigliore = new LinkedList<String>();
		
		int livello=0;
		int costo=0;
		
		recursive(parziale,dao.getRilevamentiMese(mese),listaCitta,livello,costo);
		
		String risultato="";
		for(String s: soluzioneMigliore)
			risultato = risultato +s+" ";
		
		return risultato;
	}
	
	public void recursive(List<String> parziale,List<Rilevamento> rilevamenti, List<Citta> listaCitta, int livello, int costo) {
		
		// Caso terminale
		if(livello==15) {
			if(costo<costoMinimoCorrente) {
				costoMinimoCorrente = costo;
				System.out.println(costoMinimoCorrente);
				soluzioneMigliore = new LinkedList<String>(parziale);
			}
			return;
		}
		
		for(Citta c : listaCitta) {
			// Provo a inserire una citta (se valida)
			if(c.getCounter()<=3) {
				
				// Calcolo il costo aggiornato
				int nuovoCosto = costo;
				
				if(parziale.size()==0 || parziale.get(livello-1)!=c.getNome()) 
					nuovoCosto=nuovoCosto+COST;
				
				nuovoCosto = nuovoCosto + c.getRilevamenti().get(livello).getUmidita() +c.getRilevamenti().get(livello+1).getUmidita() +c.getRilevamenti().get(livello+2).getUmidita();
			
				// Aggiungo la città alla soluzione parziale
				List<String> nuovaParziale = new LinkedList<String>(parziale);
				nuovaParziale.add(c.getNome());
				nuovaParziale.add(c.getNome());
				nuovaParziale.add(c.getNome());
				
				c.setCounter(c.getCounter()+3);
				
				recursive(nuovaParziale,rilevamenti,listaCitta, livello+3, nuovoCosto);
				
				// Riporto indietro il contatore
				c.setCounter(c.getCounter()-3);
			}
		}
		return;
	}	
	
	/*public boolean isValida(List<String> parziale, Citta citta) {
		
	}*/

}
