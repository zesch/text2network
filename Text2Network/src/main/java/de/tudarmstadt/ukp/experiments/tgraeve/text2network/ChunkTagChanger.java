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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.VC;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.components.Nounphrase;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class ChunkTagChanger extends JCasAnnotator_ImplBase
{

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);

	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{
		for(Chunk chunk: JCasUtil.select(aJCas, Chunk.class))
		{
			if(chunk.getChunkValue().equals("NP"))
			{
				NC nc = new NC(aJCas);
				nc.setBegin(chunk.getBegin());
				nc.setEnd(chunk.getEnd());
				nc.addToIndexes();
			}
			else if(chunk.getChunkValue().equals("VP"))
			{
				VC vc = new VC(aJCas);
				vc.setBegin(chunk.getBegin());
				vc.setEnd(chunk.getEnd());
				vc.addToIndexes();
			}
		}

			
	}

}