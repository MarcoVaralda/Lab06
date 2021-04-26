package it.polito.tdp.meteo.model;

import java.time.LocalDate;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	MeteoDAO dao = new MeteoDAO();
	List<Citta> soluzioneMigliore;
	int costoMinimoCorrente=500000;
	
	List<Citta> listaCitta;
	int giorniStessaCitta=0;
	
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
		
		List<Citta> parziale = new LinkedList<Citta>();
		
		listaCitta = new LinkedList<Citta>();
		Citta Torino = new Citta("Torino",dao.getAllRilevamentiLocalitaMese(mese, "Torino"));
		Citta Milano = new Citta("Milano",dao.getAllRilevamentiLocalitaMese(mese, "Milano"));
		Citta Genova = new Citta("Genova",dao.getAllRilevamentiLocalitaMese(mese, "Genova"));
		listaCitta.add(Torino);
		listaCitta.add(Milano);
		listaCitta.add(Genova);
		
		soluzioneMigliore = new LinkedList<Citta>();
		
		int livello=0;
		int costo=0;
					
		recursive(parziale,livello,costo, giorniStessaCitta);
		
		String risultato="";
		for(Citta s: soluzioneMigliore)
			risultato = risultato +s.getNome()+" ";
		
		return risultato;
	}
	
	public void recursive(List<Citta> parziale, int livello, int costo,int giorniConsecutiviCitta) {
		
		// Caso terminale
		if(livello==15) {
			if(costo<costoMinimoCorrente) {
				costoMinimoCorrente = costo;
				soluzioneMigliore = new LinkedList<Citta>(parziale);
			}
			return;
		}
		else if(giorniConsecutiviCitta>0 && giorniConsecutiviCitta<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
			Citta c = parziale.get(livello-1);
			c.increaseCounter();
			
			int nuovoCosto = costo + c.getRilevamenti().get(livello).getUmidita();
			parziale.add(c);
			recursive(parziale, livello+1,nuovoCosto, giorniConsecutiviCitta+1);
			c.setCounter(c.getCounter()-1);;
			parziale.remove(livello);
		}
		else {
			
			for(Citta c : listaCitta) {
				
				// Se la città è valida provo a inserirla
				if(c.getCounter()<NUMERO_GIORNI_CITTA_MAX) {
					int nuovoCosto=costo;
					// Se la città che sto inserendo è diversa dall'ultima inserita --> azzero il contatore giorniStessaCitta
					if(livello>0 && !parziale.get(livello-1).equals(c)) {
						giorniConsecutiviCitta=0;
						nuovoCosto = nuovoCosto + COST;
					}
					c.increaseCounter();
					nuovoCosto = nuovoCosto + c.getRilevamenti().get(livello).getUmidita();
					parziale.add(c);
					recursive(parziale,livello+1,nuovoCosto,giorniConsecutiviCitta+1);
					parziale.remove(livello);
					c.setCounter(c.getCounter()-1);
				}
				
		    }
			
		}
			
	}

}
