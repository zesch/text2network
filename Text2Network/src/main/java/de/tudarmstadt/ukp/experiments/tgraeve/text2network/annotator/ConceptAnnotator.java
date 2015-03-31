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

package de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator;

import java.util.Map.Entry;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;
/**
 * Diese Komponente annotiert {@link Concept}s nach Parametervorgabe.
 * 
 * @author Tobias Graeve
 *
 */
public class ConceptAnnotator extends JCasAnnotator_ImplBase
{
	/**
	 * Bestimmt den Typ der Chunks, die als Konzepte dienen sollen.
	 */
	public static final String PARAM_CONCEPT_TYPE = "conceptType";
	@ConfigurationParameter(name = PARAM_CONCEPT_TYPE, mandatory = true)
	protected Class conceptType;
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{

		try {

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
			
		} catch (FeaturePathException e) {
			throw new AnalysisEngineProcessException (e);
		}
	}
}
