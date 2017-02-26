package br.com.lojamultinacional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

@XmlRootElement(name = "Books")
@JsonDeserialize(using = MapaDeserializer.class)
public class Mapa {

    private String nome;
    private Map<Set<String>, Integer> matrizInicialDeDistancia;
    private List<String> listaVerticesOrdenados;
    private double[][] matrizAdjacenciaCompleta;
    private int[][] matrizCaminho;
    
	/**
	 * @return Nome do mapa
	 */
	public String getNome() {
		return nome;
	}
    
	/**
	 * Configura o nome do mapa
	 * 
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * @return Matriz fornecida como entrada 
	 */
	@JsonIgnore 
	public Map<Set<String>, Integer> getMatrizInicialDeDistancia() {
		return matrizInicialDeDistancia;
	}
	
	/**
	 * Configura matriz de entrada como um dos attributos da classe
	 * 
	 * @param matrizDeDistancia
	 */
	public void setMatrizInicialDeDistancia(Map<Set<String>, Integer> matrizDeDistancia) {
		this.matrizInicialDeDistancia = matrizDeDistancia;
		setListaVerticesOrdenados();
	}
	
	/**
	 * Cria uma lista com os nomes das cidades encontradas no mapa inicial de distâncias
	 * e os ordena. 
	 */
	public void setListaVerticesOrdenados(){
		Set<String> vertices = new HashSet<String>();
		for (Set<String> edge : matrizInicialDeDistancia.keySet()) {
			Iterator<String> verticesIterator = edge.iterator();
			while (verticesIterator.hasNext()){
				vertices.add(verticesIterator.next());
			}
		}
		List<String> sortedVertices = new ArrayList<String>(vertices);
		Collections.sort(sortedVertices);
		this.listaVerticesOrdenados = sortedVertices;
	}
	
	/**
	 * @return Lista ordenada de vertices (cidades)
	 */
	@JsonIgnore
	public List<String> getListaVerticesOrdenados(){
		return this.listaVerticesOrdenados;
	}
	
	
	/**
	 * @return quantidade de vértices existentes no mapa
	 */
	@JsonIgnore
	public int getNumberOfVertices(){
		return this.listaVerticesOrdenados.size();
	}
	
	/**
	 * Método converte a matriz recebida na variável "matrizInicialDeDistancia" 
	 * e substitui os nomes das cidades por indices da variável "listVerticesOrdenados"
	 * 
	 * @return matriz inicial de adjacência
	 */
	@JsonIgnore
	public int[][] getMatrizInicialAdjacencia(){
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
				edges[i][j] = listaVerticesOrdenados.indexOf(vertex);
				j++;
			}
			edges[i][2] = edgeEntry.getValue();
			i++;
		}
		return edges;
	}

	@JsonIgnore 
	public double[][] getMatrizAdjacenciaCompleta() {
		return matrizAdjacenciaCompleta;
	}
	

	public void setMatrizAdjacenciaCompleta(double[][] matrizAdjacenciaCompleta) {
		this.matrizAdjacenciaCompleta = matrizAdjacenciaCompleta;
	}

	@JsonIgnore
	public int[][] getMatrizCaminho() {
		return matrizCaminho;
	}

	public void setMatrizCaminho(int[][] matrizCaminho) {
		this.matrizCaminho = matrizCaminho;
	}
	
	public String getRotaECusto(String origem, String destino, Double autonomia, Double litro) throws JsonGenerationException, JsonMappingException, IOException{
		FloydWarshall.floydWarshall(this);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> resposta = new HashMap<String, Object>();
		String rota = origem;
		int i = listaVerticesOrdenados.indexOf(origem);
		int j = listaVerticesOrdenados.indexOf(destino);
		Double custo = (matrizAdjacenciaCompleta[i][j]/autonomia)*litro;
		resposta.put("custo", custo);
		do {
			i = this.matrizCaminho[i][j];
			rota += "  " + listaVerticesOrdenados.get(i);
		} while (i != j );
		
		resposta.put("rota", rota);
		return mapper.writeValueAsString(resposta);
	}

}