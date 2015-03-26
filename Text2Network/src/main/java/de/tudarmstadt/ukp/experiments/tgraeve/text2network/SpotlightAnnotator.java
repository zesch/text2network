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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.VC;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.components.Nounphrase;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class SpotlightAnnotator extends JCasAnnotator_ImplBase
{
	
	public static final String PARAM_CONFIDENCE = "confidence";
	@ConfigurationParameter(name = PARAM_CONFIDENCE, mandatory = false) //TODO auf true setzen? Spotlight Standard 0.1
	protected double confidence;
	
	public static final String PARAM_SUPPORT = "support";
	@ConfigurationParameter(name = PARAM_SUPPORT, mandatory = false)
	protected int support;
	
	protected String text;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);

	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{
		
		text = aJCas.getDocumentText();
		text = text.replaceAll(" ", "%20");
		System.out.println(text);
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"http://spotlight.sztaki.hu:2222/rest/annotate?text=" + text 
				+"&confidence="+ confidence);
//				+ "&support="+ support);
		request.addHeader("Accept", "text/xml");
		
		try {
			HttpResponse response = client.execute(request);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(response.getEntity().getContent());
			
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName("Resource");
			
			for(int i = 0; i < list.getLength(); i++)
			{
				Node node = list.item(i);
				Element element = (Element) node;
				int begin = Integer.parseInt(element.getAttribute("offset"));
				int end = begin + element.getAttribute("surfaceForm").length();
				
				System.out.println(element.getAttribute("surfaceForm")+" von " + begin + " bis " + end); //TODO löschen
				
				Concept concept = new Concept(aJCas);
				concept.setBegin(begin);
				concept.setEnd(end);
				concept.setText(element.getAttribute("surfaceForm"));
				concept.addToIndexes();
				
//				
//				for(Chunk chunk : JCasUtil.select(aJCas, Chunk.class)) // TODO Spotlight-NE überspannen häufig mehr als nur ein Token!
//				{
//					if (chunk.getCoveredText().contains(element.getAttribute("surfaceForm")))
//						{
//							Concept concept = new Concept(aJCas);
//							concept.setBegin(chunk.getBegin());
//							concept.setEnd(chunk.getEnd());
//							concept.setText(element.getAttribute("surfaceForm")); //TODO Sinn überdenken. Verfälscht begin und end.
//							concept.addToIndexes();
//							
//							System.out.println(concept.getCoveredText());
//						}
//				}
			}
			
			
			//einfacher Stringreader
//			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//			String empty = "";
//			while ( (empty = rd.readLine()) != null)
//			{
//			System.out.println(rd.readLine());
//			}
		} catch (IOException | ParserConfigurationException | IllegalStateException | SAXException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		
		
			
	}

}