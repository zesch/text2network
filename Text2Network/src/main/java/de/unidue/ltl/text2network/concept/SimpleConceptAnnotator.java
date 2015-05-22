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

package de.unidue.ltl.text2network.concept;

import java.util.Map.Entry;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.unidue.ltl.text2network.type.Concept;


public class SimpleConceptAnnotator
	extends JCasAnnotator_ImplBase
{

	public static final String PARAM_CONCEPT_FEATURE_PATH = "conceptFeaturePath";
	@ConfigurationParameter(name = PARAM_CONCEPT_FEATURE_PATH, mandatory = true)
	protected String conceptFeaturePath;
	
	public static final String PARAM_CONCEPT_VALUE = "conceptValue";
	@ConfigurationParameter(name = PARAM_CONCEPT_VALUE, mandatory = true)
	protected String conceptValue;
	
	public void process(JCas aJCas) 
			throws AnalysisEngineProcessException
	{

		try {

			for (Entry<AnnotationFS, String> entry : FeaturePathFactory.select(aJCas.getCas(), conceptFeaturePath))
			{
				AnnotationFS annotation = entry.getKey();

				if (entry.getValue().equals(conceptValue)) {
					Concept concept = new Concept(aJCas);
					concept.setBegin(annotation.getBegin());
					concept.setEnd(annotation.getEnd());
					concept.setLabel(annotation.getCoveredText());
					concept.addToIndexes();
				}
			}
			
		} catch (FeaturePathException e) {
			throw new AnalysisEngineProcessException (e);
		}
	}
}
