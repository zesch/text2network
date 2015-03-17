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

package de.tudarmstadt.ukp.experiments.tgraeve.text2network.components;

public class Edge {

	private String end1;
	private String end2;
	private String relation;
	
	public Edge(String end1, String end2)
	{
		this.end1 = end1;
		this.end2 = end2;
	}
	
	public Edge(String end1, String relation, String end2)
	{
		this.end1 = end1;
		this.end2 = end2;
		this.relation = relation;
	}

	public String getEnd1() {
		return end1;
	}

	public void setEnd1(String end1) {
		this.end1 = end1;
	}

	public String getEnd2() {
		return end2;
	}

	public void setEnd2(String end2) {
		this.end2 = end2;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public String toString()
	{
		return "Edge: " + end1 + " - " + relation + " - " + end2;
	}
	
	
}
