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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.VC;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.RelationType;
/**
 * Diese Komponente sucht Verbindungen zwischen Konzepten anhand zwischenliegender Verbphrasen und verbindet diese zu einer {@link Relation}.
 * 
 * @author Tobias Graeve
 *
 */
public class SyntaxRelationAnnotator extends JCasAnnotator_ImplBase
{
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{	
		
		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) // Selektiert Satz aus Gesamtkonstrukt.
		{
			
			HashMap<Concept, ArrayList> relHelper = new HashMap<Concept, ArrayList>();
			List<Token> tokenSentence = new ArrayList<Token>();
			
			for (Token token : JCasUtil.selectCovered(Token.class, sentence)) // Selektiert einzelne Token aus Satz.
			{
				tokenSentence.add(token);
			}
			
			List<Concept> conceptsSentence = new ArrayList<Concept>();
				
			for (Concept concept : JCasUtil.selectCovered(Concept.class, sentence)) // Durchsucht das aktuelle Fenster nach Konzepten.
			{
				conceptsSentence.add(concept);
			}
				
			if(conceptsSentence.size()>=2) //Mehr als 2 Konzepte innerhalb des Satzes gefunden
			{	
				int iterator = 0;
				
				while(iterator < conceptsSentence.size()-1)
				{
					Concept con1 = conceptsSentence.get(iterator);
					Concept con2 = conceptsSentence.get(iterator+1);
				
					if(!JCasUtil.selectBetween(aJCas, VC.class, con1, con2).isEmpty())
					{
					
						if(!relHelper.containsKey(con1))
						{
							Relation relation = new Relation(aJCas);
							relation.setBegin(con1.getBegin());
							relation.setEnd(con2.getEnd());
							relation.setSource(con1);
							relation.setTarget(con2);
							
							RelationType relType = new RelationType(aJCas);
							relType.setText(JCasUtil.selectBetween(aJCas, VC.class, con1, con2).get(0).getCoveredText());
							relType.setBegin(JCasUtil.selectBetween(aJCas, VC.class, con1, con2).get(0).getBegin());
							relType.setEnd(JCasUtil.selectBetween(aJCas, VC.class, con1, con2).get(0).getEnd());
							relation.setRelation(relType);
							relType.addToIndexes();
							
							relation.addToIndexes();
							
							ArrayList array = new ArrayList<Concept>();
							array.add(con2);
							relHelper.put(con1, array);
						}
						else if (!relHelper.get(con1).contains(con2))
						{
							Relation relation = new Relation(aJCas);
							relation.setBegin(con1.getBegin());
							relation.setEnd(con2.getEnd());
							relation.setSource(con1);
							relation.setTarget(con2);
							
							RelationType relType = new RelationType(aJCas);
							relType.setText(JCasUtil.selectBetween(VC.class, con1, con2).get(0).getCoveredText());
							relType.addToIndexes();
							relation.setRelation(relType);
							
							relation.addToIndexes();
							
							ArrayList array = relHelper.get(con1);
							array.add(con2);
							relHelper.put(con1, array);
						}	
					}
					iterator++;
				}
			}
		}       
    }
}
