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
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;

public class RelationAnnotator extends JCasAnnotator_ImplBase
{
	
	public static final String PARAM_WINDOW_SIZE = "PARAM_WINDOW_SIZE";
	@ConfigurationParameter(name = PARAM_WINDOW_SIZE, mandatory = true, defaultValue = "4")
	protected int windowSize;


	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		super.initialize(context);
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{	
		
		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) // Selektiert Satz aus Gesamtkonstrukt.
		{
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
					Relation relation = new Relation(aJCas);
					relation.setBegin(conceptsWindow.get(0).getBegin());
					relation.setEnd(conceptsWindow.get(1).getEnd());
					relation.setSource(conceptsWindow.get(0));
					relation.setTarget(conceptsWindow.get(1));
					relation.addToIndexes();
				}
				
				iterator++;
			}
			
			
		}
		
		
		
		
		
		
		
//        Set<Edge> edges = new HashSet();
//        boolean inWindow;
//        
//        for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class))
//        {
//        	List<Chunk> chunksSentence = new ArrayList<>();
//        	List<Token> tokenSentence = new ArrayList<>();
//        	
//	        for (Chunk chunk : JCasUtil.selectCovered(Chunk.class, sentence))
//			{
//				if (chunk.getChunkValue().equals("NP"))
//				{
//					chunksSentence.add(chunk);
//					System.out.println(chunk.getCoveredText());
//				}
//				if (chunk.getChunkValue().equals("VP"))
//				{
//					chunksSentence.add(chunk);
//					System.out.println(chunk.getCoveredText());
//				}
//			}
//	        
//	        int i = 0;
//	        while(i<chunksSentence.size()-windowSize-1)
//	        {
//        		if (chunksSentence.get(i).getChunkValue().equals("NP")
//        				&& chunksSentence.get(i+1).getChunkValue().equals("VP")
//        					&& chunksSentence.get(i+2).getChunkValue().equals("NP"))
//        		{
////        			System.out.println("NP:VP:NP = " + 
////        								chunksSentence.get(i).getCoveredText()+" -> "+
////        									chunksSentence.get(i+1).getCoveredText()+" -> "+
////        										chunksSentence.get(i+2).getCoveredText());
//        			edges.add(new Edge(chunksSentence.get(i).getCoveredText(),
//        									chunksSentence.get(i+1).getCoveredText(),
//        										chunksSentence.get(i+2).getCoveredText()));
//        			
//        			Relation relation = new Relation(aJCas);
//        			relation.setSource(chunksSentence.get(i));
//        			relation.setRelation(chunksSentence.get(i+1));
//        			relation.setTarget(chunksSentence.get(i+2));
//    				relation.addToIndexes();
//    				System.out.println("+++"+relation.getSource().getCoveredText());
//
//        		} 
////        		else if (chunksSentence.get(i).getChunkValue().equals("NP")
////        						&& chunksSentence.get(i+1).getChunkValue().equals("NP")
////        							&& chunksSentence.get(i+2).getChunkValue().equals("VP"))
////        		{
////        			System.out.println("NP:NP:VP = " + 
////        								chunksSentence.get(i+1).getCoveredText()+" -> "+
////    										chunksSentence.get(i+2).getCoveredText()+" -> "+
////    											chunksSentence.get(i).getCoveredText());
////        			edges.add(new Edge(chunksSentence.get(i+1).getCoveredText(),
////    									chunksSentence.get(i+2).getCoveredText(),
////    										chunksSentence.get(i).getCoveredText()));
////        		}
//        		
//        		
//        		
//	        	i++;
//	        }
//	        
//        }
        
        

       
    }

}
