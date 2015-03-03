package de.tudarmstadt.ukp.experiments.tgraeve.text2network.components;

public class Nounphrase {
	
	private String text;
	private int position;
	
	public Nounphrase(String text)
	{
		this.text = text;
	}
	
	public Nounphrase(String text, int position)
	{
		
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
