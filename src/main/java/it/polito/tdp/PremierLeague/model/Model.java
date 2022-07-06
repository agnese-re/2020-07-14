package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Team,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.listAllTeams());
		
		for(Team t1: this.grafo.vertexSet())
			for(Team t2: this.grafo.vertexSet())
				if(!t1.equals(t2)) {
					int punteggioT1 = this.calcolaPunteggio(t1);
					int punteggioT2 = this.calcolaPunteggio(t2);
					if(punteggioT1 > punteggioT2)
						Graphs.addEdgeWithVertices(this.grafo, t1, t2, punteggioT1-punteggioT2);
					else {
						if(punteggioT1 < punteggioT2)
							Graphs.addEdgeWithVertices(this.grafo, t2, t1, punteggioT2-punteggioT1);
					}
				}
	}
	
	/* sono squadre peggiori quelle raggiunte da t tramite un arco. Arco da t ad altra squadra */
	public List<Classifica> squadrePeggiori(Team t) {
		List<Classifica> peggiori = new ArrayList<Classifica>();
		for(DefaultWeightedEdge edge: this.grafo.outgoingEdgesOf(t))
			peggiori.add(new Classifica(Graphs.getOppositeVertex(this.grafo, edge, t),
							(int)this.grafo.getEdgeWeight(edge)));
		Collections.sort(peggiori);
		return peggiori;
	}
	
	public List<Classifica> squadreMigliori(Team t) {
		List<Classifica> migliori = new ArrayList<Classifica>();
		for(DefaultWeightedEdge edge: this.grafo.incomingEdgesOf(t))	// archi entranti in t
			migliori.add(new Classifica(Graphs.getOppositeVertex(this.grafo, edge, t),
							(int)this.grafo.getEdgeWeight(edge)));
		Collections.sort(migliori);
		return migliori;
	}
	
	public List<Team> getVertici() {
		List<Team> teams = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(teams);
		return teams;
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	private int calcolaPunteggio(Team t) {
		int punteggioCasa = 0;
		int punteggioTrasferta = 0;
		// 1. punti fatti in casa
		List<Match> matchesCasa = this.dao.matchesTeamHome(t);
		for(Match m: matchesCasa)
			if(m.getResultOfTeamHome() == 1)
				punteggioCasa += 3;
			else {
				if(m.getResultOfTeamHome() == 0)
					punteggioCasa += 1;
			}
		// 2. punti fatti in trasferta
		List<Match> matchesTrasferta = this.dao.matchesTeamAway(t);
		for(Match m: matchesTrasferta)
			if(m.getResultOfTeamHome() == -1)	// ha perso la squadra in casa
				punteggioCasa += 3;
			else {
				if(m.getResultOfTeamHome() == 0)
					punteggioCasa += 1;
			}
		return punteggioCasa+punteggioTrasferta;
	}
}
