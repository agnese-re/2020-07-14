package it.polito.tdp.PremierLeague.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m = new Model();
		
		m.creaGrafo();
		System.out.println(m.getNVertici() + " " + m.getNArchi());
	}

}
