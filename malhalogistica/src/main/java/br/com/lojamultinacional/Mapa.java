package br.com.lojamultinacional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

@XmlRootElement(name = "Books")
@JsonDeserialize(using = MapaDeserializer.class)
public class Mapa {

    private String nome;
    private Map<Set<String>, Integer> matrizInicialDeDistancia;
    private List<String> sortedVerticesList;
    
    @XmlElement
	public String getNome() {
		return nome;
	}
    
	public void setNome(String name) {
		this.nome = name;
	}
	
	@XmlElement  
	public Map<Set<String>, Integer> getMatrizInicialDeDistancia() {
		return matrizInicialDeDistancia;
	}
	
	public void setMatrizInicialDeDistancia(Map<Set<String>, Integer> matrizDeDistancia) {
		this.matrizInicialDeDistancia = matrizDeDistancia;
		setSortedVerticesList();
	}
	
	public void setSortedVerticesList(){
		Set<String> vertices = new HashSet<String>();
		for (Set<String> edge : matrizInicialDeDistancia.keySet()) {
			Iterator<String> verticesIterator = edge.iterator();
			while (verticesIterator.hasNext()){
				vertices.add(verticesIterator.next());
			}
		}
		List<String> sortedVertices = new ArrayList<String>(vertices);
		Collections.sort(sortedVertices);
		this.sortedVerticesList = sortedVertices;
	}
	
	/**
	 * @return Lista ordenada de vertices (cidades)
	 */
	public List<String> getSortedVerticesList(){
		return this.sortedVerticesList;
	}
	
	public int getNumberOfVertices(){
		return this.sortedVerticesList.size();
	}
	
	public int[][] getMatrizInicialArestas(){
		int numberOfEdges = matrizInicialDeDistancia.keySet().size();
		int i = 0;
		int[][] edges = new int[numberOfEdges][3];
		Iterator<Entry<Set<String>, Integer>> edgesIterator = matrizInicialDeDistancia.entrySet().iterator();
		while (edgesIterator.hasNext()) {
			Entry<Set<String>, Integer> edgeEntry = edgesIterator.next();
			Iterator<String> vertices = edgeEntry.getKey().iterator();
			int j = 0;
			while (vertices.hasNext()){
				String vertex = vertices.next();
				edges[i][j] = sortedVerticesList.indexOf(vertex);
				j++;
			}
			edges[i][2] = edgeEntry.getValue();
			i++;
		}
		return edges;
	}

}