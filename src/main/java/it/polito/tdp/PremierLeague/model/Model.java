package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Team,DefaultWeightedEdge> grafo;
	
	private List<Team> allTeamsLista;
	private List<Match> allMatchesLista;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		
		this.allTeamsLista = this.dao.listAllTeams();
		this.allMatchesLista = this.dao.listAllMatches();
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.allTeamsLista);
		
		for(Team t1: this.grafo.vertexSet())
			for(Team t2: this.grafo.vertexSet())
				if(!t1.equals(t2)) {
					int punteggio1 = this.calcolaPunteggio(this.dao.getAllMatchesTeam(t1), t1);
					int punteggio2 = this.calcolaPunteggio(this.dao.getAllMatchesTeam(t2), t2);
					
					if(punteggio1 > punteggio2)
						Graphs.addEdgeWithVertices(this.grafo, t1, t2, punteggio1-punteggio2);
					else if(punteggio1 < punteggio2)
						Graphs.addEdgeWithVertices(this.grafo, t2, t1, punteggio2-punteggio1);
				}
		
		String msg = "Grafo creato (" + this.grafo.vertexSet().size() + " vertici e " +
				this.grafo.edgeSet().size() + " archi)";
		return msg;
	}
	
	public List<Team> getAllTeams() {
		Collections.sort(this.allTeamsLista);
		return this.allTeamsLista;
	}
	
	/* ******************** */
	public List<TeamDiff> getMigliori(Team teamScelto) {	// archi entranti (vertici predecessori)
		List<TeamDiff> result = new ArrayList<TeamDiff>();
		
		for(DefaultWeightedEdge edge: this.grafo.incomingEdgesOf(teamScelto)) {
			Team opposto = Graphs.getOppositeVertex(this.grafo, edge, teamScelto);
			result.add(new TeamDiff(opposto,(int)this.grafo.getEdgeWeight(edge)));
		}
		
		Collections.sort(result, new ComparatorTeamDiffByDiff());
		return result;
	}
	
	public List<TeamDiff> getPeggiori(Team teamScelto) {	// archi uscenti (vertici successori)
		List<TeamDiff> result = new ArrayList<TeamDiff>();
		
		for(Team successore: Graphs.successorListOf(this.grafo, teamScelto)) {
			DefaultWeightedEdge edge = this.grafo.getEdge(teamScelto, successore);		// sono archi orientati	
			result.add(new TeamDiff(successore,(int)this.grafo.getEdgeWeight(edge)));
		}
		
		Collections.sort(result, new ComparatorTeamDiffByDiff());
		return result;
	}
	/* ******************** */
	
	public int getNComponentiConnesse() {
		ConnectivityInspector<Team,DefaultWeightedEdge> ci = 
				new ConnectivityInspector<>(this.grafo);
		return ci.connectedSets().size();
	}
	
	private int calcolaPunteggio(List<Match> matches, Team team) {
		int punteggio = 0;
		
		for(Match match: matches)
			if(match.getTeamHomeID() == team.getTeamID()) {	// partite in casa
				if(match.getResultOfTeamHome() == 1)	// VITTORIA
					punteggio += 3;
				else if(match.getResultOfTeamHome() == 0)	// PAREGGIO
					punteggio += 1;
			} else {	// in trasferta
				if(match.getResultOfTeamHome() == -1)	// VITTORIA
					punteggio += 3;
				else if(match.getResultOfTeamHome() == 0)	// PAREGGIO
					punteggio += 1;
			}
		
		return punteggio;
	}
}
