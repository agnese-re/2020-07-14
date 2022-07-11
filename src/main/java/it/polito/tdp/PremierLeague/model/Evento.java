package it.polito.tdp.PremierLeague.model;

import java.time.LocalDate;

public class Evento implements Comparable<Evento> {

	private LocalDate data;
	private Match match;
	private int nReporter;
	
	public Evento(LocalDate data, Match match, int nReporter) {
		super();
		this.data = data;
		this.match = match;
		this.nReporter = nReporter;
	}

	public LocalDate getData() {
		return data;
	}

	public Match getMatch() {
		return match;
	}

	public int getnReporter() {
		return nReporter;
	}

	@Override
	public String toString() {
		return "Evento [data=" + data + ", match=" + match + ", nReporter=" + nReporter + "]";
	}

	@Override
	public int compareTo(Evento o) {
		return this.getData().compareTo(o.getData());
	}
	
}
