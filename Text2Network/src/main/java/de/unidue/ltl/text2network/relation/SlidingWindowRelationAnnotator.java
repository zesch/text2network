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

package de.unidue.ltl.text2network.relation;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.unidue.ltl.text2network.type.Concept;
import de.unidue.ltl.text2network.type.Relation;


public class SlidingWindowRelationAnnotator 
	extends JCasAnnotator_ImplBase
{
	/**
	 * Size of the sliding window in which concepts can be found
	 */
	public static final String PARAM_WINDOW_SIZE = "PARAM_WINDOW_SIZE";
	@ConfigurationParameter(name = PARAM_WINDOW_SIZE, mandatory = true, defaultValue = "4")
	protected int windowSize;
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{	
		
		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class))
		{			
			// get tokens in that sentence
			List<Token> tokens = new ArrayList<Token>(JCasUtil.selectCovered(Token.class, sentence));
			
			int maxOffset = tokens.size() - windowSize + 1;
			if (tokens.size() < windowSize) {
				maxOffset = tokens.size();
			}

			for (int offset=0; offset<maxOffset; offset++) {

				// construct dummy annotation that is later used to select concepts in its span
				Annotation window = new Annotation(aJCas);
				window.setBegin(tokens.get(offset).getBegin());
				window.setEnd(tokens.get(offset+windowSize-1).getEnd());
				
				
				List<Concept> concepts = new ArrayList<>(JCasUtil.selectCovered(Concept.class, window));
				
				if (concepts.size() > 1)
				{	
					Concept con1 = concepts.get(0);
					Concept con2 = concepts.get(1);
					
					String relationType = "untyped";
					for (Chunk chunk : JCasUtil.selectBetween(aJCas, Chunk.class, con1, con2)) {
						if (chunk.getChunkValue().equals("VP")) {
							relationType = chunk.getCoveredText();
						}
					}
					
					Relation relation = new Relation(aJCas);
					relation.setBegin(con1.getBegin());
					relation.setEnd(con2.getEnd());
					relation.setSource(con1);
					relation.setTarget(con2);
					relation.setRelationType(relationType);
					relation.addToIndexes();					
				}
				
				offset++;
			}
		}
	}
}