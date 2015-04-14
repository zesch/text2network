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

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.component.CasDumpWriter;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.ChunkTagChanger;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.ConceptAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.CoOccurrenceRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SyntaxRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.SGFExporter;

public class InitializeNCCoOccurrence {

	public static void main(String[] args) throws UIMAException, IOException {
		
		String input = "input/";
		String output = "output/CASout.txt";
		
		ExtractionPipeline extractor = new ExtractionPipeline();
		
		CollectionReaderDescription reader = createReaderDescription(
		         TextReader.class,
		         TextReader.PARAM_SOURCE_LOCATION, input,
		         TextReader.PARAM_PATTERNS, new String[] {"[+]*.txt"},
		         TextReader.PARAM_LANGUAGE, "en");
		
		AnalysisEngineDescription dSegmenter = createEngineDescription(BreakIteratorSegmenter.class);	
		AnalysisEngineDescription dPosTagger = createEngineDescription(OpenNlpPosTagger.class);	
		AnalysisEngineDescription dChunker = createEngineDescription(OpenNlpChunker.class);
		AnalysisEngineDescription dConAnnotator = createEngineDescription(SpotlightAnnotator.class, SpotlightAnnotator.PARAM_CONFIDENCE, new Float(0.5));
//		AnalysisEngineDescription dConAnnotator = createEngineDescription(ConceptAnnotator.class, ConceptAnnotator.PARAM_CONCEPT_TYPE, NC.class);
//		AnalysisEngineDescription dRelAnnotator = createEngineDescription(CoOccurrenceRelationAnnotator.class, CoOccurrenceRelationAnnotator.PARAM_WINDOW_SIZE, 7);
		AnalysisEngineDescription dRelAnnotator = createEngineDescription(SyntaxRelationAnnotator.class);
//		AnalysisEngineDescription dExporter = createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_OUTPUT_FILE, output);
		AnalysisEngineDescription dExporter = createEngineDescription(SGFExporter.class);

		extractor.startPipeline(reader, dSegmenter, dPosTagger, dChunker, dConAnnotator, dRelAnnotator, dExporter);
	}

}
