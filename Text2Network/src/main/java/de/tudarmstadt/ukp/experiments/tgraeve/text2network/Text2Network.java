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

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasWriter;


public class Text2Network {

	private CollectionReaderDescription reader;
	private AnalysisEngineDescription preprocessing;
	private AnalysisEngineDescription conceptExtractor;
	private AnalysisEngineDescription relationClassifier;
	private AnalysisEngineDescription graphExporter;
	
	public Text2Network(
			CollectionReaderDescription reader,
			AnalysisEngineDescription preprocessing,
			AnalysisEngineDescription conceptExtractor,
			AnalysisEngineDescription relationClassifier,
			AnalysisEngineDescription graphExporter)
	{
		this.reader = reader;
		this.preprocessing = preprocessing;
		this.conceptExtractor = conceptExtractor;
		this.relationClassifier = relationClassifier;
		this.graphExporter = graphExporter;
	}
	
	public void runStreaming() 
			throws UIMAException, IOException
	{
		AnalysisEngineDescription pipeline = createEngineDescription(
				preprocessing,
				conceptExtractor,
				relationClassifier,
				graphExporter
		);
		SimplePipeline.runPipeline(reader, pipeline);
	}
	
	public void runBatch() 
			throws UIMAException, IOException
	{		
		String preprocessingFolder = UUID.randomUUID().toString();
		String conceptFolder = UUID.randomUUID().toString();
		String relationFolder = UUID.randomUUID().toString();
		
		SimplePipeline.runPipeline(reader, preprocessing, getBinCasWriter(preprocessingFolder));
		SimplePipeline.runPipeline(getBinCasReader(preprocessingFolder), conceptExtractor, getBinCasWriter(conceptFolder));
		SimplePipeline.runPipeline(getBinCasReader(conceptFolder), relationClassifier, getBinCasWriter(relationFolder));
		SimplePipeline.runPipeline(getBinCasReader(relationFolder), graphExporter);
	}
	
	private AnalysisEngineDescription getBinCasWriter(String name) 
			throws ResourceInitializationException
	{
		File outFile = new File("target/" + name);
		return createEngineDescription(
				BinaryCasWriter.class,
				BinaryCasWriter.PARAM_TARGET_LOCATION, outFile
		);
	}
	
	private CollectionReaderDescription getBinCasReader(String name) 
			throws ResourceInitializationException
	{
		File inFile = new File("target/" + name);
		return createReaderDescription(
				BinaryCasReader.class,
				BinaryCasReader.PARAM_SOURCE_LOCATION, inFile,
                BinaryCasReader.PARAM_PATTERNS, "*.bin"
		);
	}
}
