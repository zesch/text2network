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

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.VC;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.ChunkTagChanger;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SimpleConceptAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SlidingWindowRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;

public class ChunkTagChangerTest {
	
	protected String text = "This is a test.";
	
	@Test
	public void testChunkTagChanger() throws UIMAException
	{
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentText(text);
		jcas.setDocumentLanguage("en");
		
		//Segmenter
		AnalysisEngineDescription segmenter = createEngineDescription(BreakIteratorSegmenter.class);
		AnalysisEngine segEngine = createEngine(segmenter);
		segEngine.process(jcas);
		
		//POS
		AnalysisEngineDescription openPos = createEngineDescription(OpenNlpPosTagger.class);
		AnalysisEngine posEngine = createEngine(openPos);
		posEngine.process(jcas);
		
		//Chunker
		AnalysisEngineDescription openChunk = createEngineDescription(OpenNlpChunker.class);
		AnalysisEngine chunkEngine = createEngine(openChunk);
		chunkEngine.process(jcas);
		
		//ChangeChunker
		AnalysisEngineDescription changeChunker = createEngineDescription(ChunkTagChanger.class);
		AnalysisEngine changeEngine = createEngine(changeChunker);
		changeEngine.process(jcas);
		
		
		//Test
		NC nc1 = JCasUtil.selectByIndex(jcas, NC.class, 0);
		NC nc2 = JCasUtil.selectByIndex(jcas, NC.class, 1);
		assertEquals("This", nc1.getCoveredText());
		assertEquals("a test", nc2.getCoveredText());
		
		VC vc1 = JCasUtil.selectByIndex(jcas, VC.class, 0);
		assertEquals("is", vc1.getCoveredText());
			
	}
}
