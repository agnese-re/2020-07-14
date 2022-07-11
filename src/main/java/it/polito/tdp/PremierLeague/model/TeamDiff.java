package it.polito.tdp.PremierLeague.model;

public class TeamDiff {

	private Team team;
	private int diff;
	
	public TeamDiff(Team team, int diff) {
		super();
		this.team = team;
		this.diff = diff;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	@Override
	public String toString() {
		return this.team + "(" + this.diff + ")";
	}
	
	
	
}
