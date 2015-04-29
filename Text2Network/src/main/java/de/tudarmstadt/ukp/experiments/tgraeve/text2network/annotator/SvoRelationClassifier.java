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
public class SvoRelationClassifier extends JCasAnnotator_ImplBase
{
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{	
		for(Relation relation :JCasUtil.select(aJCas, Relation.class))
		{
			List<VC> vcL = JCasUtil.selectBetween(aJCas, VC.class, relation.getSource(), relation.getTarget());
			
			if(!vcL.isEmpty())
			{
				
				RelationType relType = new RelationType(aJCas);
				relType.setLabel(vcL.get(0).getCoveredText());
				relType.setBegin(vcL.get(0).getBegin());
				relType.setEnd(vcL.get(0).getEnd());
				relation.setRelation(relType);
				
				if(vcL.size()>2)
				{
					System.out.println("Mehrere VCs aufgetreten!");
					for(int i=1; i<vcL.size();i++)
					{
						Relation relationCloned = new Relation(aJCas);
						relationCloned.setBegin(relation.getBegin());
						relationCloned.setEnd(relation.getEnd());
						relationCloned.setSource(relation.getSource());
						relationCloned.setTarget(relation.getTarget());
						
						RelationType relTypes = new RelationType(aJCas);
						relTypes.setLabel(vcL.get(i).getCoveredText());
						relTypes.setBegin(vcL.get(i).getBegin());
						relTypes.setEnd(vcL.get(i).getEnd());
						relationCloned.setRelation(relType);
						
						relationCloned.addToIndexes();

					}
				}
			}
		}       
    }
}
