package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class ComparatorTeamDiffByDiff implements Comparator<TeamDiff> {

	@Override
	public int compare(TeamDiff o1, TeamDiff o2) {
		return o1.getDiff()-o2.getDiff();
	}
	
}
