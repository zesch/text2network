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

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.annotator.SpotlightAnnotator;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class SpotlightAnnotatorTest {
	
	protected String text = "Germany was reunified in 1990 - Thanks to David Hasselhoff!";
	protected double confidence = 0.2;
	
	@Test
	public void testSpotlightAnnotator() throws UIMAException
	{
		
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentText(text);
		jcas.setDocumentLanguage("en");
		
		AnalysisEngineDescription pipeline = createEngineDescription(
				createEngineDescription(BreakIteratorSegmenter.class),
				createEngineDescription(SpotlightAnnotator.class)
		);
		AnalysisEngine engine = createEngine(pipeline);
		engine.process(jcas);
		
		Collection<Concept> concepts = JCasUtil.select(jcas, Concept.class);
		Iterator<Concept> iterator = concepts.iterator();
		assertEquals(2, concepts.size());
		assertEquals("Germany", iterator.next().getLabel());
		assertEquals("David_Hasselhoff", iterator.next().getLabel());
	}
}
