/*******************************************************************************
 * Copyright 2015
 * Language Technlogy Lab
 * University of Duisburg-Essen
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

package de.unidue.ltl.text2network.concept;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unidue.ltl.text2network.type.Concept;


public class SpotlightAnnotator
	extends JCasAnnotator_ImplBase
{
	
	/**
	 * Confidence for the service [0.0 - 1.0]
	 */
	public static final String PARAM_CONFIDENCE = "confidence";
	@ConfigurationParameter(name = PARAM_CONFIDENCE, mandatory = true, defaultValue = "0.5")
	protected double confidence;
	
	/**
	 * TODO ??
	 */
	public static final String PARAM_SUPPORT = "support";
	@ConfigurationParameter(name = PARAM_SUPPORT, mandatory = false)
	protected Integer support;
	
	/**
	 * Rename concept with linked URI
	 */
	public static final String PARAM_RENAME_TO_URI = "renameToUri";
	@ConfigurationParameter(name = PARAM_RENAME_TO_URI, mandatory = true, defaultValue = "true")
	protected boolean renameToUri;
	
	/**
	 * Only use certain entity types
	 * @see http://wiki.dbpedia.org/Ontology
	 */
	public static final String PARAM_TYPES = "types";
	@ConfigurationParameter(name = PARAM_TYPES, mandatory = false)
	protected String[] types;
	
	public void process(JCas aJCas) 
			throws AnalysisEngineProcessException
	{
		
		// FIXME service call will not work if document is too big
		String text = aJCas.getDocumentText();
		
		try {
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AnalysisEngineProcessException(e);
		}
		
		String request = "http://spotlight.sztaki.hu:2222/rest/annotate?text=" + text 
				+"&confidence="+ confidence; 
		// or http://spotlight.dbpedia.org/rest - but different API
		
		
		if (support != null)
		{
			request = request + "&support="+ support;
		}
		
		if (types != null && types.length>0)
		{
			request = request + "&types=" + types[0];
			for(int i=1; i<types.length; i++)
			{
				request = request + "," +types[i];
			}
		}
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpRequest = new HttpGet(request);
		httpRequest.addHeader("Accept", "text/xml");
		
		try {			
			CloseableHttpResponse response = client.execute(httpRequest);
			
			if (response.getStatusLine().getStatusCode() == 414) {
				throw new AnalysisEngineProcessException(new Throwable("Document too long for current implementation of wrapper."));
			}
			
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
										
				Concept concept = new Concept(aJCas);
				concept.setBegin(begin);
				concept.setEnd(end);
				if (renameToUri)
				{
					concept.setLabel(element.getAttribute("URI").substring(28));
				}
				else
				{
					concept.setLabel(element.getAttribute("surfaceForm"));
				}
				concept.addToIndexes();	
				
				response.close();
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