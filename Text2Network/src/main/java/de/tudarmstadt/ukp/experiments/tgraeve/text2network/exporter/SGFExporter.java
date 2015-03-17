
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;
import eu.sisob.api.parser.sisob.SGFParser;
import eu.sisob.api.visualization.format.graph.fields.EdgeSet;
import eu.sisob.api.visualization.format.graph.fields.Node;
import eu.sisob.api.visualization.format.graph.fields.NodeSet;

public class SGFExporter extends JCasConsumer_ImplBase
{
	protected Map<String, String> vertexKeyTypes;
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);
		
		NodeSet nodeset = new NodeSet();
		EdgeSet edgeset = new EdgeSet();
		SGFParser parser = new SGFParser();
		Node node;
		Edge edge;
		

	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException
	{
		
		for(Relation relation : JCasUtil.select(jCas, Relation.class))
		{
			
		}
		
		
	}

}
