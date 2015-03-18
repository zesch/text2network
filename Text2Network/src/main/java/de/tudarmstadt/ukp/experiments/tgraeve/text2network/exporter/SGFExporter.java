
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
import java.io.FileWriter;
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
import eu.sisob.api.visualization.format.graph.fields.Edge;
import eu.sisob.api.visualization.format.graph.fields.EdgeSet;
import eu.sisob.api.visualization.format.graph.fields.Node;
import eu.sisob.api.visualization.format.graph.fields.NodeSet;
import eu.sisob.api.visualization.format.metadata.Metadata;




public class SGFExporter extends JCasConsumer_ImplBase
{
	protected NodeSet nodeset;
	protected EdgeSet edgeset;
	protected SGFParser parser;
	
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);
		
		nodeset = new NodeSet();
		edgeset = new EdgeSet();
		parser = new SGFParser();


	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException
	{
		int eId = 1;
		
		for(Relation relation : JCasUtil.select(jCas, Relation.class))
		{
			Node node1 = new Node(relation.getSource().getCoveredText(), relation.getSource().getCoveredText());
			if(!nodeset.contains(node1))
			{
				nodeset.addNode(node1);
			}
			Node node2 = new Node(relation.getTarget().getCoveredText(), relation.getTarget().getCoveredText());
			if(!nodeset.contains(node2))
			{
				nodeset.addNode(node2);
			}
			
			Edge edge = new Edge(Integer.toString(eId), relation.getSource().getCoveredText(), relation.getTarget().getCoveredText());
			edgeset.add(edge);
			
			eId++;
		}
		
		parser.setMetadata(new Metadata("Text2Network", "1 mode network", "false"));
		parser.setParsingNodeSet(nodeset);
		parser.setParsingEdgeSet(edgeset);
		
		System.out.println(parser.encode());
		
		FileWriter writer = null;
		
		try {
			writer = new FileWriter("output/output.sgf");
			writer.write(parser.encode());
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
		}
		
		
	}

}
