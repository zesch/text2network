package de.tudarmstadt.ukp.experiments.tgraeve.text2network.components;

public class Edge {

	private String end1;
	private String end2;
	private String relation;
	
	public Edge(String end1, String end2)
	{
		this.end1 = end1;
		this.end2 = end2;
	}
	
	public Edge(String end1, String relation, String end2)
	{
		this.end1 = end1;
		this.end2 = end2;
		this.relation = relation;
	}

	public String getEnd1() {
		return end1;
	}

	public void setEnd1(String end1) {
		this.end1 = end1;
	}

	public String getEnd2() {
		return end2;
	}

	public void setEnd2(String end2) {
		this.end2 = end2;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public String toString()
	{
		return "Edge: " + end1 + " - " + relation + " - " + end2;
	}
	
	
}
