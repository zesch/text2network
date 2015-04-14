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

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SyntaxRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.SGFExporter;

/**
 * 
 * @author Tobias Graeve
 * 
 * Lässt Texte von Spotlight annotieren und verbindet gefundene Konzepte durch zugehörige Verbphrasen.
 * 
 * @param input
 * @param output
 *
 */
public class InitializeSpotlightSyntactic {

	public static void main(String[] args) throws UIMAException, IOException {
		
		String input = "input/";
		String output = "output/";
		
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
		AnalysisEngineDescription dRelAnnotator = createEngineDescription(SyntaxRelationAnnotator.class);
		AnalysisEngineDescription dExporter = createEngineDescription(SGFExporter.class);

		extractor.startPipeline(reader, dSegmenter, dPosTagger, dChunker, dConAnnotator, dRelAnnotator, dExporter);
	}

}
