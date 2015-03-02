package de.tudarmstadt.ukp.experiments.tgraeve.text2network;

import java.io.IOException;

import org.apache.uima.UIMAException;

public class Initialize {

	public static void main(String[] args) throws UIMAException, IOException {
		
		String input = "input/";
		String output = "output/output.txt";
		
		
		ExtractionPipeline extractor = new ExtractionPipeline();
		
		extractor.startPipeline(input, output);
		
		
	}

}
