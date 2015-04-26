package de.tudarmstadt.ukp.experiments.tgraeve.text2network.interfaces;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

public interface I_Extractor {

	public void startPipeline() throws UIMAException, IOException;
	public void startPipeline(AnalysisEngineDescription... components) throws UIMAException, IOException;
	
}
