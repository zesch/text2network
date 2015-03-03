package de.tudarmstadt.ukp.experiments.tgraeve.text2network.components;

import java.util.Set;

public class Network
{
	private Set<Edge> edges;
	
	public Network(Set<Edge> edges)
	{
		this.edges = edges;
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	public void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}
	
	public void addEdges(Set<Edge> edges)
	{
		this.edges.addAll(edges);
	}
	
}
