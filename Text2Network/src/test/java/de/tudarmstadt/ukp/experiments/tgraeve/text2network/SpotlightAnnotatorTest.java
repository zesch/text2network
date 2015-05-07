package de.tudarmstadt.ukp.experiments.tgraeve.text2network;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.*;

import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class SpotlightAnnotatorTest {
	
	protected String text = "Germany was xxx reunified in 1990 - Thanks to David Hasselhoff!";
	protected static double confidence = 0.2;
	
	@Test
	public void testConceptAnnotationNC() throws UIMAException
	{
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentText(text);
		jcas.setDocumentLanguage("en");
		
		//Segmenter
		AnalysisEngineDescription segmenter = createEngineDescription(BreakIteratorSegmenter.class);
		AnalysisEngine segEngine = createEngine(segmenter);
		segEngine.process(jcas);
		
		//SpotlightAnnotator
		AnalysisEngineDescription spotAnn = createEngineDescription(SpotlightAnnotator.class);
		AnalysisEngine spotEngine = createEngine(spotAnn);
		spotEngine.process(jcas);
		
		//Test
		Concept concept1 = JCasUtil.selectByIndex(jcas, Concept.class, 0);
		Concept concept2 = JCasUtil.selectByIndex(jcas, Concept.class, 1);
		assertEquals("Germany", concept1.getCoveredText());
		assertEquals("David_Hasselhoff", concept2.getCoveredText());
	}
}
