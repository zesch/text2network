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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

/**
 * 
 * Der SpotlightAnnotator annotiert {@link Concept}s mithilfe der DBPedia-Wissensdatenbank über die Spotlight-Webschnittstelle.
 * @see dbpedia.org
 * 
 * @author Tobias Graeve
 *
 */
public class SpotlightAnnotator extends JCasAnnotator_ImplBase
{
	
	/**
	 * Vertrauensintervall von 0-1.
	 */
	public static final String PARAM_CONFIDENCE = "confidence";
	@ConfigurationParameter(name = PARAM_CONFIDENCE, mandatory = true, defaultValue = "0.2")
	protected double confidence;
	
	/**
	 * Prominenz auf Wikipedia (Anzahl der internen Verlinkungen) > 0.
	 */
	public static final String PARAM_SUPPORT = "support";
	@ConfigurationParameter(name = PARAM_SUPPORT, mandatory = false)
	protected int support;
	
	@Override
	public void process(JCas aJCas) 
			throws AnalysisEngineProcessException
	{
		
		String text = aJCas.getDocumentText();		
		try {
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AnalysisEngineProcessException(e);
		}
		
		String request = "http://spotlight.dbpedia.org/rest/annotate?text=" + text 
				+"&confidence="+ confidence; //alternativ http://spotlight.sztaki.hu:2222/rest
		
		if (support != 0)
		{
			request = request + "&support="+ support;
		}

		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpRequest = new HttpGet(request);
		httpRequest.addHeader("Accept", "text/xml");
		
		try {
			HttpResponse response = client.execute(httpRequest);
			
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

			}
			} catch (IOException | ParserConfigurationException | IllegalStateException | SAXException e) {
				throw new AnalysisEngineProcessException(e);
			} finally {
				try {
					client.close();
				} catch (IOException e) {
					throw new AnalysisEngineProcessException(e);
				}
			}
	}
}