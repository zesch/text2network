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

import java.io.IOException;

import org.apache.uima.UIMAException;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.ConceptAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.CoOccurrenceRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SyntaxRelationAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter.SGFExporter;

/**
 * Dient dem initialisieren von Pipelines zur Netzwerkextraktion.
 * 
 * @author Tobias Graeve
 *
 */
public class Initialize {

	public static void main(String[] args) throws UIMAException, IOException {
		
		Text2NetworkPipeline t2npipe = new Text2NetworkPipeline();
		
		t2npipe.startPipeline(createEngineDescription(SpotlightAnnotator.class, SpotlightAnnotator.PARAM_CONFIDENCE, new Float(0.5), SpotlightAnnotator.PARAM_RENAME_CONCEPTS, true, SpotlightAnnotator.PARAM_TYPES, new String[] {"Person"}),
								createEngineDescription(CoOccurrenceRelationAnnotator.class),
									createEngineDescription(SGFExporter.class));
		
//		t2npipe.startPipeline(createEngineDescription(ConceptAnnotator.class, ConceptAnnotator.PARAM_CONCEPT_TYPE, NC.class),
//				createEngineDescription(CoOccurrenceRelationAnnotator.class),
//					createEngineDescription(SGFExporter.class));

	}

}
