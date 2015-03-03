package de.tudarmstadt.ukp.experiments.tgraeve.text2network;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.components.Edge;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class NetworkBuilder extends JCasAnnotator_ImplBase
{
	
	protected String outputFile;
	protected List<Chunk> chunksSentence;
	protected int windowSize = 4;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		super.initialize(context);
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{	
        Set<Edge> edges = new HashSet();
        boolean inWindow;
        
        for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class))
        {
        	List<Chunk> chunksSentence = new ArrayList<>();
        	
	        for (Chunk chunk : JCasUtil.selectCovered(Chunk.class, sentence))
			{
				if (chunk.getChunkValue().equals("NP"))
				{
					chunksSentence.add(chunk);
					System.out.println(chunk.getCoveredText());
				}
				if (chunk.getChunkValue().equals("VP"))
				{
					chunksSentence.add(chunk);
					System.out.println(chunk.getCoveredText());
				}
			}
	        
	        int i = 0;
	        while(i<chunksSentence.size()-windowSize-1)
	        {
        		if (chunksSentence.get(i).getChunkValue().equals("NP")
        				&& chunksSentence.get(i+1).getChunkValue().equals("VP")
        					&& chunksSentence.get(i+2).getChunkValue().equals("NP"))
        		{
        			System.out.println(chunksSentence.get(i).getCoveredText()+" "+
        									chunksSentence.get(i+1).getCoveredText()+" "+
        										chunksSentence.get(i+2).getCoveredText());
        			edges.add(new Edge(chunksSentence.get(i).getCoveredText(),
        									chunksSentence.get(i+1).getCoveredText(),
        										chunksSentence.get(i+2).getCoveredText()));
        			edges.toString();
        		}
	        	i++;
	        }
	        
        }
        
        

       
    }

}
