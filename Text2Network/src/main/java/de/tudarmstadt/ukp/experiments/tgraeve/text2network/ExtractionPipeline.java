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
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.component.CasDumpWriter;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.VC;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpChunker;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpParser;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunker;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.ChunkTagChanger;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SimpleConceptAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SlidingWindowRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SyntaxRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.GraphMLExporter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.SGFExporter;

/**
 * In dieser Pipeline können die unterschiedlichen Komponenten ineinander verzahnt werden.
 * 
 * @author Tobias Graeve
 *
 */
public class ExtractionPipeline {
	
	protected String[] input;
	protected String output;
	protected String concept;
	protected String relation;
	protected String export;
	
	public ExtractionPipeline(String... input) throws UIMAException, IOException
	{
		
		ArrayList<AnalysisEngineDescription> config = new ArrayList<AnalysisEngineDescription>();
		int i = 0;
		boolean fail = false;
		this.input = input;
		
		CollectionReaderDescription reader = createReaderDescription(
		         TextReader.class,
		         TextReader.PARAM_SOURCE_LOCATION, "input/",
		         TextReader.PARAM_PATTERNS, new String[] {"[+]*.txt"},
		         TextReader.PARAM_LANGUAGE, "en");
		AnalysisEngineDescription segmenter = createEngineDescription(BreakIteratorSegmenter.class);
		
		AnalysisEngineDescription pos = createEngineDescription(OpenNlpPosTagger.class);
		AnalysisEngineDescription chunker = createEngineDescription(OpenNlpChunker.class);
		AnalysisEngineDescription changeChunker = createEngineDescription(ChunkTagChanger.class);
		AnalysisEngineDescription cas = createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_OUTPUT_FILE, "output/+|.*");
		
		config.add(segmenter);
		config.add(pos);
		config.add(chunker);
		config.add(changeChunker);
		
		
		if(input.length>0)
		{
			
			AnalysisEngineDescription concAnn = null;
			switch (input[i]) { //Konzeptextraktion
			case "Concept":
				i++;
				switch (input[i]) {
				case "NC":
					concAnn = createEngineDescription(SimpleConceptAnnotator.class, SimpleConceptAnnotator.PARAM_CONCEPT_TYPE, NC.class);
					config.add(concAnn);
					break;
				case "VC":
					concAnn = createEngineDescription(SimpleConceptAnnotator.class, SimpleConceptAnnotator.PARAM_CONCEPT_TYPE, VC.class);
					config.add(concAnn);
					break;
				default:
					System.out.println("Keine gültige Annotation angegeben!");
					fail = true;
					break;
				}
				i++;
				break;
			case "Spotlight":
				i++;
				if(0<Float.parseFloat(input[i]) && Float.parseFloat(input[i])<1)
				{
					AnalysisEngineDescription spotAnn = createEngineDescription(SpotlightAnnotator.class, SpotlightAnnotator.PARAM_CONFIDENCE, Float.parseFloat(input[i]));
					config.add(spotAnn);
				}
				else{
					System.out.println("Kein gültiges Konfidenzintervall angegeben!");
					fail = true;
				}
				i++;
				break;
			default:
				System.out.println("Keine gültige Konzeptextraktion angegeben!");
				fail = true;
				break;
			}
			
			
			AnalysisEngineDescription relAnnotator = null;
			switch (input[i]) { //Relationsextraktion
			case "CoOccurrence":
				i++;
				if(Integer.parseInt(input[i])>0)
				{
					relAnnotator = createEngineDescription(SlidingWindowRelationAnnotator.class, SlidingWindowRelationAnnotator.PARAM_WINDOW_SIZE, Integer.parseInt(input[i]));
					config.add(relAnnotator);
				}
				else {
					System.out.println("Ungültige Fenstergröße angegeben!");
				}
				i++;
				break;
			case "Syntax":
				relAnnotator = createEngineDescription(SyntaxRelationAnnotator.class);
				config.add(relAnnotator);
				i++;
				break;
			default:
				System.out.println("Ungültige Relationsextraktion angegeben!");
				break;
			}
			
			AnalysisEngineDescription exporter = null;
			switch (input[i]) {
			case "SGF":
				exporter = createEngineDescription(SGFExporter.class);
				config.add(exporter);
				break;
			case "GraphML":
				exporter = createEngineDescription(GraphMLExporter.class);
				config.add(exporter);
				break;
			default:
				break;
			}
			
			config.add(cas);
			AnalysisEngineDescription[] pipe = new AnalysisEngineDescription[config.size()];
			config.toArray(pipe);

			try {
				runPipeline(reader, pipe);
			} catch (Exception e) {
				System.out.println("Falsche Konfiguration - bitte Eingaben überprüfen!");
			}
				
		} else{
			System.out.println("Keine Parameter angegeben!");
		}
	}

	
	public void startPipeline(CollectionReaderDescription reader,
								AnalysisEngineDescription segmenter,
								AnalysisEngineDescription pos,
								AnalysisEngineDescription chunker,
								AnalysisEngineDescription conAnnotator,
								AnalysisEngineDescription relAnnotator,
								AnalysisEngineDescription exporter) throws UIMAException, IOException
	{
			
		AnalysisEngineDescription changeChunker = createEngineDescription(ChunkTagChanger.class);
		AnalysisEngineDescription cas = createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_OUTPUT_FILE, "output/+|.*");


		
		runPipeline(reader, segmenter, pos, chunker, changeChunker, conAnnotator, relAnnotator, exporter, cas);
		
	}

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
		AnalysisEngineDescription openChunker = createEngineDescription(OpenNlpChunker.class); //Der OpenNLPChunker nutzt das Penn Treebank Tagset
		AnalysisEngineDescription treeChunker = createEngineDescription(TreeTaggerChunker.class);
		AnalysisEngineDescription changeChunker = createEngineDescription(ChunkTagChanger.class);
		
		//Annotator
		AnalysisEngineDescription concAnn = createEngineDescription(SimpleConceptAnnotator.class, SimpleConceptAnnotator.PARAM_CONCEPT_TYPE, NC.class);
		AnalysisEngineDescription spotAnn = createEngineDescription(SpotlightAnnotator.class, SpotlightAnnotator.PARAM_CONFIDENCE, new Float(0.1));
		AnalysisEngineDescription relAnn = createEngineDescription(SlidingWindowRelationAnnotator.class);
			
		//Ausgabe
		AnalysisEngineDescription cas = createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_OUTPUT_FILE, "output/output.txt");
		AnalysisEngineDescription gmlexp = createEngineDescription(GraphMLExporter.class);
		AnalysisEngineDescription sgfexp = createEngineDescription(SGFExporter.class);
	
		
		runPipeline(reader, seg, openPos, openChunker, changeChunker, concAnn, relAnn, gmlexp, sgfexp, cas);
//		runPipeline(reader, seg, openPos, openChunker, changeChunker, spotAnn, relAnn, gmlexp, sgfexp, cas);
	}

}
