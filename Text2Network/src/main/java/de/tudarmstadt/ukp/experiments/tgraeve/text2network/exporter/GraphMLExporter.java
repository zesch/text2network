
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

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLTokens;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;

public class GraphMLExporter extends JCasConsumer_ImplBase
{
	protected Map<String, String> vertexKeyTypes;
	
	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		// TODO Automatisch generierter Methodenstub
		super.initialize(context);
		
		vertexKeyTypes = new HashMap<String, String>();
		vertexKeyTypes.put("concept", GraphMLTokens.STRING);
		

	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException
	{
		Graph graph = new TinkerGraph();
		
//		for (Concept concept : JCasUtil.select(jCas, Concept.class))
//		{
//			Vertex a = graph.addVertex(null);
//			a.setProperty("concept", concept.getCoveredText());
//			System.out.println(a.getProperty("concept"));
//		}
		
		for (Relation relation : JCasUtil.select(jCas, Relation.class))
		{
			Vertex a = graph.addVertex(null);
			Vertex b = graph.addVertex(null);
			a.setProperty("concept", relation.getSource().getCoveredText());
			b.setProperty("concept", relation.getTarget().getCoveredText());
			Edge e = graph.addEdge(null, a, b, relation.getRelation().getCoveredText());
			System.out.println(e);
		}
		
		
		
		
		GraphMLWriter writer = new GraphMLWriter(graph);
		writer.setVertexKeyTypes(vertexKeyTypes);
		
		try
		{
			OutputStream out = new FileOutputStream("network.graphml");
			writer.outputGraph(out);
		} 
		catch (IOException e)
		{
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		
		
	}

}
