package de.tudarmstadt.ukp.experiments.tgraeve.text2network;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReaderDescription;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextWriter;
import de.tudarmstadt.ukp.dkpro.core.io.conll.Conll2012Writer;
import static org.apache.uima.fit.factory.CollectionReaderFactory.*;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.util.StanfordAnnotator;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.*;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;

import org.apache.uima.fit.component.CasDumpWriter;
import org.apache.uima.resource.ResourceInitializationException;

import static org.apache.uima.fit.pipeline.SimplePipeline.*;

public class ExtractionPipeline {

	public void startPipeline(String input, String output, String stopwords) throws UIMAException, IOException {
		
		CollectionReaderDescription cr = createReaderDescription(
		         TextReader.class,
		         TextReader.PARAM_SOURCE_LOCATION, input,
		         TextReader.PARAM_PATTERNS, new String[] {"[+]*.txt"},
		         TextReader.PARAM_LANGUAGE, "en");
		
		AnalysisEngineDescription seg = createEngineDescription(StanfordSegmenter.class);
		
		AnalysisEngineDescription pos = createEngineDescription(StanfordPosTagger.class);
		
//		AnalysisEngineDescription seg = createEngineDescription(BreakIteratorSegmenter.class);
//		
//		AnalysisEngineDescription tagger = createEngineDescription(OpenNlpPosTagger.class);
		
//		AnalysisEngineDescription ner = createEngineDescription(StanfordNamedEntityRecognizer.class);
		
		AnalysisEngineDescription chu = createEngineDescription(OpenNlpChunker.class);
		
		AnalysisEngineDescription stop = createEngineDescription(StopWordRemover.class,
																StopWordRemover.PARAM_MODEL_LOCATION, stopwords);
//		
//		AnalysisEngineDescription cc = createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_OUTPUT_FILE, output);
		
		AnalysisEngineDescription npexp = createEngineDescription(NounphraseExporter.class, "outputFile", output);
		
		
		
//		AnalysisEngineDescription wr = createEngineDescription(TextWriter.class, TextWriter.PARAM_TARGET_LOCATION, "target/output");
		
//		AnalysisEngineDescription conw = createEngineDescription(Conll2012Writer.class, Conll2012Writer.PARAM_TARGET_LOCATION, "target/output");

		
		runPipeline(cr, seg, pos, chu, stop, npexp);
	}

}
