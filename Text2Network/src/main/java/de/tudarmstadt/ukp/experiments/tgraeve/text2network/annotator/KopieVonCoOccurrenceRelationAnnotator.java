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
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;
/**
 * Diese Komponente sucht Verbindungen zwischen Konzepten und verbindet diese zu einer {@link Relation}.
 * 
 * @author Tobias Graeve
 *
 */
public class KopieVonCoOccurrenceRelationAnnotator extends JCasAnnotator_ImplBase
{
	/**
	 * Größe des Fensters über dem Text, in dem nach Verbindungen gesucht wird.
	 */
	public static final String PARAM_WINDOW_SIZE = "PARAM_WINDOW_SIZE";
	@ConfigurationParameter(name = PARAM_WINDOW_SIZE, mandatory = true, defaultValue = "4")
	protected int windowSize;
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{	
		
		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) // Selektiert Satz aus Gesamtkonstrukt.
		{
			
			HashMap<Concept, ArrayList> relHelper = new HashMap<Concept, ArrayList>(); //Speichert Konzepte zwischen, um doppelte zu vermeiden
																					   // Konzepte -> {Konzepte}
			List<Token> tokenSentence = new ArrayList<Token>();
			int iterator = 0;
			
			for (Token token : JCasUtil.selectCovered(Token.class, sentence)) // Selektiert einzelne Token aus Satz.
			{
				tokenSentence.add(token);
			}
			
			while (iterator <= tokenSentence.size()-windowSize) // Betrachtet Fenster innerhalb dieses Satzes.
			{
				Annotation window = new Annotation(aJCas);
				window.setBegin(tokenSentence.get(iterator).getBegin());
				window.setEnd(tokenSentence.get(iterator+windowSize-1).getEnd());
				
				List<Concept> conceptsWindow = new ArrayList<Concept>();
				
				for (Concept concept : JCasUtil.selectCovered(Concept.class, window)) // Durchsucht das aktuelle Fenster nach Konzepten.
				{
					conceptsWindow.add(concept);
				}
				
				if(conceptsWindow.size()>=2)
				{	
					Concept con1 = conceptsWindow.get(0);
					Concept con2 = conceptsWindow.get(1);
					
					if(!relHelper.containsKey(con1))
					{
						Relation relation = new Relation(aJCas);
						relation.setBegin(con1.getBegin());
						relation.setEnd(con2.getEnd());
						relation.setSource(con1);
						relation.setTarget(con2);
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
