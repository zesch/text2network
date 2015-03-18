/*******************************************************************************
 * Copyright 2010
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tudarmstadt.ukp.experiments.tgraeve.text2network;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.component.CasDumpWriter;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextWriter;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpParser;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunker;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.ConceptExporter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.GraphMLExporter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.SGFExporter;

public class ExtractionPipeline {

	public void startPipeline(String input, String output) throws UIMAException, IOException {
		
		CollectionReaderDescription reader = createReaderDescription(
		         TextReader.class,
		         TextReader.PARAM_SOURCE_LOCATION, input,
		         TextReader.PARAM_PATTERNS, new String[] {"[+]*.txt"},
		         TextReader.PARAM_LANGUAGE, "en");
		
		//Segmenter
		AnalysisEngineDescription stSeg = createEngineDescription(StanfordSegmenter.class);
		AnalysisEngineDescription seg = createEngineDescription(BreakIteratorSegmenter.class);	
		
		//POS
		AnalysisEngineDescription stPos = createEngineDescription(StanfordPosTagger.class);
		AnalysisEngineDescription openPos = createEngineDescription(OpenNlpPosTagger.class);
		
		//Parser
		AnalysisEngineDescription stParser = createEngineDescription(StanfordParser.class);
		AnalysisEngineDescription openParser = createEngineDescription(OpenNlpParser.class);

		
		//Chunker
		AnalysisEngineDescription openChunker = createEngineDescription(OpenNlpChunker.class, OpenNlpChunker.PARAM_PRINT_TAGSET, true); //Der OpenNLPChunker nutzt das Penn Treebank Tagset
		AnalysisEngineDescription treeChunker = createEngineDescription(TreeTaggerChunker.class, TreeTaggerChunker.PARAM_PRINT_TAGSET, true);
		
		//Annotator
		AnalysisEngineDescription concAnn = createEngineDescription(ConceptAnnotator.class, ConceptAnnotator.PARAM_CONCEPT_TYPE, Chunk.class, ConceptAnnotator.PARAM_CONCEPT_VALUE, "NP");
		AnalysisEngineDescription npexp = createEngineDescription(ConceptExporter.class, "outputFile", output);
		AnalysisEngineDescription relAnn = createEngineDescription(RelationAnnotator.class);
			
		//Ausgabe
		AnalysisEngineDescription wr = createEngineDescription(TextWriter.class, TextWriter.PARAM_TARGET_LOCATION, "target/output");
//		AnalysisEngineDescription conw = createEngineDescription(Conll2012Writer.class, Conll2012Writer.PARAM_TARGET_LOCATION, "target/output");
		AnalysisEngineDescription cas = createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_OUTPUT_FILE, "target/output.txt");
		AnalysisEngineDescription gmlexp = createEngineDescription(GraphMLExporter.class);
		AnalysisEngineDescription sgfexp = createEngineDescription(SGFExporter.class);
		



		
		
		
		runPipeline(reader, seg, openPos, openChunker, concAnn, relAnn, sgfexp, cas);
	}

}
