package it.polito.tdp.PremierLeague.model;

public class Classifica implements Comparable<Classifica> {

	private Team team;
	private int punteggio;
	
	public Classifica(Team team, int punteggio) {
		super();
		this.team = team;
		this.punteggio = punteggio;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}

	@Override
	public String toString() {
		return team.getName() + "(" + punteggio + ")";
	}

	@Override
	public int compareTo(Classifica o) {
		return this.getPunteggio()-o.getPunteggio();
	}
	
	
}
