package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {

	// stato del mondo
	private Model model;
	
	private Graph<Team,DefaultWeightedEdge> grafo;
	private Map<Team,Integer> reporterTeam;	/* ad ogni squadra e' associato un certo numero di reporter */
	
	// coda degli eventi
	private PriorityQueue<Evento> queue;
	
	// parametri in input
	private int nReporter;			// N
	private int sogliaCritica;		// X (numero minimo reporter per partita)
	
	private List<Team> allTeams;
	private List<Match> allMatches;
	
	private Map<Integer,Team> allTeamsMappa;
	
	// output
	private int partiteCritiche;
	
	public Simulatore(Graph<Team,DefaultWeightedEdge> grafo, 
			List<Team> allTeams, List<Match> allMatches) {
		this.model = model;
	
		this.grafo = grafo;
		this.allTeams = allTeams;
		
		for(Team t: this.allTeams)
			this.allTeamsMappa.put(t.getTeamID(), t);
		
		Collections.sort(allMatches);
		this.allMatches = allMatches;
	}
	
	public void init(int nReporter, int sogliaCritica) {
		// inizializzazione parametri in input
		this.nReporter = nReporter;
		this.sogliaCritica = sogliaCritica;
		
		// inizializzazione output
		this.partiteCritiche = 0;
		
		// inizializzazione stato del mondo
		this.reporterTeam = new HashMap<Team,Integer>();
		for(Team team: this.allTeams)
			reporterTeam.put(team, nReporter);
		
		// inizializzazione coda prioritaria e pre-caricamento primo evento
		this.queue = new PriorityQueue<Evento>();
		Match primoMatch = this.allMatches.get(0);
		this.queue.add(new Evento(primoMatch.getDate().toLocalDate(), primoMatch, 2*this.nReporter));
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			System.out.println(e);
			/* SQUADRA VINCENTE IN CASA */
			if(e.getMatch().getResultOfTeamHome() == 1)	{	// ha vinto la squadra in casa
				Team vincente = this.allTeamsMappa.get(e.getMatch().getTeamHomeID());
				Team perdente = this.allTeamsMappa.get(e.getMatch().getTeamAwayID());
				
				double casoVincita = Math.random();
				if(casoVincita < 0.50 && this.reporterTeam.get(vincente) >= 1) {		// nel 50% dei casi ed esiste almeno un reporter
					Team piuBlasonata = selezionaSquadraMigliore(vincente); 	// assegno un reporter a squadra piu' blasonata 
					if(piuBlasonata != null)
						this.reporterTeam.put(piuBlasonata, this.reporterTeam.get(piuBlasonata)+1);
				}
				
				double casoPerdita = Math.random();
				if(casoPerdita < 0.20 && this.reporterTeam.get(perdente) >= 1) {
					Team menoBlasonata = selezionaSquadraPeggiore(perdente);
					int reporterAggiunti = (int) ( Math.random()*(this.reporterTeam.get(perdente)-1+1) + 1);	// (int) ( Math.random()*(max-min+1) + min) )
					if(menoBlasonata != null)
						this.reporterTeam.put(menoBlasonata, this.reporterTeam.get(menoBlasonata)+reporterAggiunti);
				}
			/* SQUADRA VINCENTE IN TRASFERTA */
			} else if(e.getMatch().getResultOfTeamHome() == -1) {	// ha vinto la squadra in trasferta
				Team vincente = this.allTeamsMappa.get(e.getMatch().getTeamAwayID());
				Team perdente = this.allTeamsMappa.get(e.getMatch().getTeamHomeID());
				
				double casoVincita = Math.random();
				if(casoVincita < 0.50 && this.reporterTeam.get(vincente) >= 1) {		// nel 50% dei casi ed esiste almeno un reporter
					Team piuBlasonata = selezionaSquadraMigliore(vincente); 	// assegno un reporter a squadra piu' blasonata 
					if(piuBlasonata != null)
						this.reporterTeam.put(piuBlasonata, this.reporterTeam.get(piuBlasonata)+1);
				}
				
				double casoPerdita = Math.random();
				if(casoPerdita < 0.20 && this.reporterTeam.get(perdente) >= 1) {
					Team menoBlasonata = selezionaSquadraPeggiore(perdente);
					int reporterAggiunti = (int) ( Math.random()*(this.reporterTeam.get(perdente)-1+1) + 1);	// (int) ( Math.random()*(max-min+1) + min) )
					if(menoBlasonata != null)
						this.reporterTeam.put(menoBlasonata, this.reporterTeam.get(menoBlasonata)+reporterAggiunti);
				}
			/* PAREGGIO */
			} else {
				// nessun cambiamento -> reporter associati alle squadre precedentemente assegnate
			}
		}
	}	// chiude il run

	private Team selezionaSquadraPeggiore(Team perdente) {
		List<TeamDiff> peggiori = this.model.getPeggiori(perdente);
		
		int scelta = (int) (Math.random()*(peggiori.size()) );
		
		if(peggiori.size() != 0)
			return peggiori.get(scelta).getTeam();
		
		return null;
	}

	private Team selezionaSquadraMigliore(Team vincente) {
		List<TeamDiff> migliori = this.model.getMigliori(vincente);
		
		int scelta = (int) (Math.random()*(migliori.size()) );
		
		if(migliori.size() != 0)
			return migliori.get(scelta).getTeam();
		
		return null;
	}
}
