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
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.util.StanfordAnnotator;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.*;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpParser;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.ConceptExporter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.GraphMLExporter;

import org.apache.uima.fit.component.CasDumpWriter;
import org.apache.uima.resource.ResourceInitializationException;

import static org.apache.uima.fit.pipeline.SimplePipeline.*;

public class ExtractionPipeline {

	public void startPipeline(String input, String output) throws UIMAException, IOException {
		
		CollectionReaderDescription reader = createReaderDescription(
		         TextReader.class,
		         TextReader.PARAM_SOURCE_LOCATION, input,
		         TextReader.PARAM_PATTERNS, new String[] {"[+]*.txt"},
		         TextReader.PARAM_LANGUAGE, "en");
		
		//Segmenter
		AnalysisEngineDescription stSegmenter = createEngineDescription(StanfordSegmenter.class);
		AnalysisEngineDescription seg = createEngineDescription(BreakIteratorSegmenter.class);	
		
		//POS
		AnalysisEngineDescription stPos = createEngineDescription(StanfordPosTagger.class);
		AnalysisEngineDescription openPos = createEngineDescription(OpenNlpPosTagger.class);
		
		//Parser
		AnalysisEngineDescription stParser = createEngineDescription(StanfordParser.class);
		AnalysisEngineDescription openParser = createEngineDescription(OpenNlpParser.class);

		
		//Chunker
		AnalysisEngineDescription openChunker = createEngineDescription(OpenNlpChunker.class); //Der OpenNLPChunker nutzt das Penn Treebank Tagset
		
		//Nounphrase
		AnalysisEngineDescription npAnn = createEngineDescription(NounphraseAnnotator.class);
		AnalysisEngineDescription npexp = createEngineDescription(ConceptExporter.class, "outputFile", output);
			
		//Ausgabe
		AnalysisEngineDescription wr = createEngineDescription(TextWriter.class, TextWriter.PARAM_TARGET_LOCATION, "target/output");
//		AnalysisEngineDescription conw = createEngineDescription(Conll2012Writer.class, Conll2012Writer.PARAM_TARGET_LOCATION, "target/output");
		AnalysisEngineDescription cas = createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_OUTPUT_FILE, "target/output.txt");
		AnalysisEngineDescription gmlexp = createEngineDescription(GraphMLExporter.class);
		
		AnalysisEngineDescription netBuild = createEngineDescription(NetworkBuilder.class);



		
		
		
		runPipeline(reader, seg, openPos, openChunker, netBuild, gmlexp, cas);
	}

}
