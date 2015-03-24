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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.components.Nounphrase;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class ConceptAnnotator extends JCasAnnotator_ImplBase
{
	public static final String PARAM_CONCEPT_TYPE = "conceptType";
	@ConfigurationParameter(name = PARAM_CONCEPT_TYPE, mandatory = true)
	protected Class conceptType;
	
	public static final String PARAM_CONCEPT_VALUE = "conceptValue";
	@ConfigurationParameter(name = PARAM_CONCEPT_VALUE, mandatory = false)
	protected String conceptValue;
	
	protected String outputFile;
	protected List<Nounphrase> nounphrases;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);

	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{

			try {
				
				if(conceptValue == null)
				{
					for (Entry<AnnotationFS, String> entry : FeaturePathFactory.select(aJCas.getCas(), conceptType.getName()))
					{
						System.out.println(conceptType.getName());
						System.out.println(entry.getValue());
						
						Concept concept = new Concept(aJCas);
						concept.setBegin(entry.getKey().getBegin());
						concept.setEnd(entry.getKey().getEnd());
						concept.setText(entry.getKey().getCoveredText());
						concept.addToIndexes();
					}
				}
				else
				{
					for (Entry<AnnotationFS, String> entry : FeaturePathFactory.select(aJCas.getCas(), conceptType.getName()))
					{
						
						System.out.println(conceptType.getName());
						System.out.println(entry.getKey().getFeatureValueAsString(entry.getKey().getType().getFeatureByBaseName("chunkValue")));
						
						if(entry.getKey().getFeatureValueAsString(entry.getKey().getType().getFeatureByBaseName("chunkValue")).equals(conceptValue))
						{
							Concept concept = new Concept(aJCas);
							concept.setBegin(entry.getKey().getBegin());
							concept.setEnd(entry.getKey().getEnd());
							concept.setText(entry.getKey().getCoveredText());
							concept.addToIndexes();
						}
					}
				}
				
				
			} catch (FeaturePathException e) {
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
		
		
//			for (Chunk chunk : JCasUtil.select(aJCas, Chunk.class ))
//			{
//				if(chunk.getChunkValue().equals(conceptType)) {
//					Concept concept = new Concept(aJCas);
//					concept.setBegin(chunk.getBegin());
//					concept.setEnd(chunk.getEnd());
//					concept.setText(chunk.getCoveredText());
//					concept.addToIndexes();
//					System.out.println(chunk.getCoveredText());
//				}
//			}
//		
	}
}
