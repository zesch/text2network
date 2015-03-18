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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.components.Edge;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;

public class NetworkBuilder extends JCasAnnotator_ImplBase
{
	
	protected List<Chunk> chunksSentence;
	protected int windowSize = 4;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		super.initialize(context);
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{	
        Set<Edge> edges = new HashSet();
        boolean inWindow;
        
        for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class))
        {
        	List<Chunk> chunksSentence = new ArrayList<>();
        	
	        for (Chunk chunk : JCasUtil.selectCovered(Chunk.class, sentence))
			{
				if (chunk.getChunkValue().equals("NP"))
				{
					chunksSentence.add(chunk);
					System.out.println(chunk.getCoveredText());
				}
				if (chunk.getChunkValue().equals("VP"))
				{
					chunksSentence.add(chunk);
					System.out.println(chunk.getCoveredText());
				}
			}
	        
	        int i = 0;
	        while(i<chunksSentence.size()-windowSize-1)
	        {
        		if (chunksSentence.get(i).getChunkValue().equals("NP")
        				&& chunksSentence.get(i+1).getChunkValue().equals("VP")
        					&& chunksSentence.get(i+2).getChunkValue().equals("NP"))
        		{
//        			System.out.println("NP:VP:NP = " + 
//        								chunksSentence.get(i).getCoveredText()+" -> "+
//        									chunksSentence.get(i+1).getCoveredText()+" -> "+
//        										chunksSentence.get(i+2).getCoveredText());
        			edges.add(new Edge(chunksSentence.get(i).getCoveredText(),
        									chunksSentence.get(i+1).getCoveredText(),
        										chunksSentence.get(i+2).getCoveredText()));
        			
        			Relation relation = new Relation(aJCas);
        			relation.setSource(chunksSentence.get(i));
        			relation.setRelation(chunksSentence.get(i+1));
        			relation.setTarget(chunksSentence.get(i+2));
    				relation.addToIndexes();
    				System.out.println("+++"+relation.getSource().getCoveredText());

        		} 
//        		else if (chunksSentence.get(i).getChunkValue().equals("NP")
//        						&& chunksSentence.get(i+1).getChunkValue().equals("NP")
//        							&& chunksSentence.get(i+2).getChunkValue().equals("VP"))
//        		{
//        			System.out.println("NP:NP:VP = " + 
//        								chunksSentence.get(i+1).getCoveredText()+" -> "+
//    										chunksSentence.get(i+2).getCoveredText()+" -> "+
//    											chunksSentence.get(i).getCoveredText());
//        			edges.add(new Edge(chunksSentence.get(i+1).getCoveredText(),
//    									chunksSentence.get(i+2).getCoveredText(),
//    										chunksSentence.get(i).getCoveredText()));
//        		}
        		
        		
        		
	        	i++;
	        }
	        
        }
        
        

       
    }

}
