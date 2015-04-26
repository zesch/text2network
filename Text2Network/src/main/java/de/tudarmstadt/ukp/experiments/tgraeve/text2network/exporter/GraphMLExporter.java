
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

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLTokens;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Relation;
/**
 * Diese Komponente liest Relationen aus und exportiert diese als GraphML-Format.
 * 
 * @author Tobias Graeve
 *
 */
public class GraphMLExporter extends JCasConsumer_ImplBase
{
	protected Map<String, String> vertexKeyTypes;

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException
	{
		vertexKeyTypes = new HashMap<String, String>();
		vertexKeyTypes.put("concept", GraphMLTokens.STRING);
		Graph graph = new TinkerGraph();
		
		for(Concept concept : JCasUtil.select(jCas, Concept.class))
		{	
			if(graph.getVertices("concept", concept.getLabel()) == null)
			{
				Vertex vert = graph.addVertex(null);
				vert.setProperty("concept", concept.getLabel());
			}
		}
		
		for (Relation relation : JCasUtil.select(jCas, Relation.class))
		{
			Vertex a = graph.addVertex(null);
			Vertex b = graph.addVertex(null);
			a.setProperty("concept", relation.getSource().getLabel());
			b.setProperty("concept", relation.getTarget().getLabel());
			
			if(relation.getRelation() != null)
			{
				Edge e = graph.addEdge(null, a, b, relation.getRelation().getLabel());
			}
			else 
			{
				Edge e = graph.addEdge(null, a, b, "");
			}
		}
		
		GraphMLWriter writer = new GraphMLWriter(graph);
		writer.setVertexKeyTypes(vertexKeyTypes);
		
		try
		{
			OutputStream out = new FileOutputStream("output/network.graphml");
			writer.outputGraph(out);
		} 
		catch (IOException e)
		{
			throw new AnalysisEngineProcessException(e);
		}	
	}
}
