/*******************************************************************************
 * Copyright 2015
 * Language Technlogy Lab
 * University of Duisburg-Essen
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

import org.apache.uima.fit.component.NoOpAnnotator;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SimpleConceptAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SlidingWindowRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.GraphMLExporter;

public class Example {

	public static void main(String[] args)
			throws Exception
	{
		Text2Network simpleT2N = getSimpleExample();
		simpleT2N.runBatch();
		simpleT2N.runStreaming();
		
		Text2Network spotlightT2N = getSpotlightExample();
		spotlightT2N.runStreaming();
	}
	
	public static Text2Network getSimpleExample()
		throws Exception
	{
		return new Text2Network(
				createReaderDescription(
				         TextReader.class,
				         TextReader.PARAM_SOURCE_LOCATION, "src/test/resources/text",
				         TextReader.PARAM_PATTERNS, new String[] {"[+]simpsons.txt"},
				         TextReader.PARAM_LANGUAGE, "en"
				 ), 
				createEngineDescription(
						createEngineDescription(BreakIteratorSegmenter.class),
						createEngineDescription(OpenNlpPosTagger.class),
						createEngineDescription(OpenNlpChunker.class)
				),
				createEngineDescription(
						SimpleConceptAnnotator.class,
						SimpleConceptAnnotator.PARAM_CONCEPT_FEATURE_PATH, Chunk.class.getName() + "/chunkValue",
						SimpleConceptAnnotator.PARAM_CONCEPT_VALUE, "NP"
				),
				createEngineDescription(
						SlidingWindowRelationAnnotator.class,
						SlidingWindowRelationAnnotator.PARAM_WINDOW_SIZE, 7
				),
				createEngineDescription(
						GraphMLExporter.class,
						GraphMLExporter.PARAM_OUTPUT_FILE, "target/simple_graph.ml"
				)
		);
		
	}
	
	public static Text2Network getSpotlightExample()
		throws Exception
	{
		return new Text2Network(
				createReaderDescription(
				         TextReader.class,
				         TextReader.PARAM_SOURCE_LOCATION, "src/test/resources/text",
				         TextReader.PARAM_PATTERNS, new String[] {"[+]*.txt"},
				         TextReader.PARAM_LANGUAGE, "en"
				 ), 
				createEngineDescription(
						createEngineDescription(NoOpAnnotator.class)
				),
				createEngineDescription(
						SpotlightAnnotator.class,
						SpotlightAnnotator.PARAM_CONFIDENCE, new Float(0.5),
						SpotlightAnnotator.PARAM_RENAME_TO_URI, true
				),
				createEngineDescription(
						SlidingWindowRelationAnnotator.class,
						SlidingWindowRelationAnnotator.PARAM_WINDOW_SIZE, 7
				),
				createEngineDescription(
						GraphMLExporter.class,
						GraphMLExporter.PARAM_OUTPUT_FILE, "target/spotlight_graph.ml"
				)
		);
	}
}
