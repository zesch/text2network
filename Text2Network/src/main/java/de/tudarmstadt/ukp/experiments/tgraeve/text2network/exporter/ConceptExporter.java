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

package de.tudarmstadt.ukp.experiments.tgraeve.text2network.exporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.experiments.tgraeve.text2network.components.Nounphrase;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class ConceptExporter extends JCasConsumer_ImplBase 
{
	
	protected String outputFile;
	protected List<Nounphrase> nounphrases;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);
		
		for (int i = 0; i< context.getConfigParameterNames().length; i = i+1)
		{
			System.out.println(context.getConfigParameterNames()[i]);
		}
		
		this.outputFile = context.getConfigParameterValue(context.getConfigParameterNames()[0]).toString();

	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{
		
		nounphrases = new ArrayList<Nounphrase>();
		
		for (Concept concept : JCasUtil.select(aJCas, Concept.class))
		{
			nounphrases.add(new Nounphrase(concept.getCoveredText()));
			System.out.println(concept.getCoveredText());
		}
		
		this.export(this.outputFile);
		
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
	}
	
	protected void export(String outputFile)
	{
		
		FileWriter writer = null;
		
		try
		{
			 writer = new FileWriter(outputFile);
			 
			 for(int i = 0; i <nounphrases.size(); i = i+1)
			 {
				 writer.write(nounphrases.get(i).getText());
				 writer.write(System.lineSeparator());
			 }
			 
			 
		}
		catch (IOException e)
		{
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (IOException e)
			{
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
		}
		
		
	}
	
//	protected List<Nounphrase> getNounphrase(Sentence sentence)
//	{
//		
//		List<Nounphrase> nounphrases = new ArrayList<Nounphrase>();
//		
//		for (Chunk chunk : JCasUtil.selectCovered(Chunk.class, sentence))
//		{
//			if (chunk.getChunkValue().equals("NP"))
//			{
//				System.out.println(chunk.getCoveredText());
//				nounphrases.add(new Nounphrase(chunk.getCoveredText()));
//			}
//		}
//		return nounphrases;	
//	}

}
