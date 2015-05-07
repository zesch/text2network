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

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.ChunkTagChanger;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.interfaces.I_Extractor;

/**
 * Dieser Extractor kann vollständig konfigueriert werden.
 * Er beinhaltet nur das Einlesen von Text.
 * 
 * 
 * 
 * Quelle: input/
 * Ziel: output/
 * 
 * @author Tobias Graeve
 *
 */

public class NetworkExtractor implements I_Extractor {
	
	protected AnalysisEngineDescription[] components;

	public void startPipeline(AnalysisEngineDescription... components) throws UIMAException, IOException
	{
		this.components = components;	
		
		CollectionReaderDescription reader = createReaderDescription(
		         TextReader.class,
		         TextReader.PARAM_SOURCE_LOCATION, "input/",
		         TextReader.PARAM_PATTERNS, new String[] {"[+]*.txt"},
		         TextReader.PARAM_LANGUAGE, "en");
		
		
		
		AnalysisEngineDescription[] pipe = new AnalysisEngineDescription[components.length];
		
		int i = 0;
		while(i<components.length)
		{
			pipe[i] = components[i];
			i++;
		}
		
		runPipeline(reader, pipe);
	}

	public void startPipeline() {
		// TODO Automatisch generierter Methodenstub
		
	}
}