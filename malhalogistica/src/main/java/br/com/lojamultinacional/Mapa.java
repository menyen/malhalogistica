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

/**
 * Classe utilizada para salvar dados de matriz de adjacência e matriz de custo
 * 
 * @author ng
 *
 */
@XmlRootElement(name = "mapas")
@JsonDeserialize(using = MapaDeserializer.class)
public class Mapa {

    private String nome;
    private Map<Set<String>, Integer> matrizInicialDeDistancia;
    private List<String> listaVerticesOrdenados;
    private double[][] matrizCustoCompleta;
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
		createListaVerticesOrdenados();
	}
	
	/**
	 * Cria uma lista com apenas os nomes das cidades ordenados por ordem alfabética
	 * 
	 * @param listaVerticesOrdenados
	 */
	public void setListaVerticesOrdenados(List<String> listaVerticesOrdenados){
		this.listaVerticesOrdenados = listaVerticesOrdenados;
	}
	
	/**
	 * @return Lista ordenada de vertices (cidades)
	 */
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
	 * Set para a matriz de custo totalmente preenchida em formato de matriz quadrada
	 * 
	 * @param matrizCustoCompleta
	 */
	public void setMatrizCustoCompleta(double[][] matrizCustoCompleta) {
		this.matrizCustoCompleta = matrizCustoCompleta;
	}
	
	/**
	 * @return Matriz de custo totalmente preenchida
	 */
	public double[][] getMatrizCustoCompleta() {
		return matrizCustoCompleta;
	}

	/**
	 * @return Matriz com rota de menor caminho de um ponto a outro
	 */
	public int[][] getMatrizCaminho() {
		return matrizCaminho;
	}
	
	/**
	 * Salva a matriz de caminho mais curto
	 * 
	 * @param matrizCaminho
	 */
	public void setMatrizCaminho(int[][] matrizCaminho) {
		this.matrizCaminho = matrizCaminho;
	}
	
	/**
	 * Cria uma lista com os nomes das cidades encontradas no mapa inicial de distâncias
	 * e os ordena. 
	 */
	public void createListaVerticesOrdenados(){
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
	 * Método converte a matriz recebida na variável "matrizInicialDeDistancia" 
	 * e substitui os nomes das cidades por indices da variável "listVerticesOrdenados"
	 * 
	 * @return matriz inicial de custo
	 */
	@JsonIgnore
	public int[][] getMatrizInicialCusto(){
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
	
	/**
	 * Converte a matriz de adjacência retornada do banco de dados em formato 
	 * <List<List<Double>> e converte em matriz de tipo primitivo
	 * 
	 * @param matrizCustoCompleta
	 */
	public void setMatrizCustoCompleta(List<List<Double>> matrizCustoCompleta) {
		this.matrizCustoCompleta = new double[matrizCustoCompleta.size()][matrizCustoCompleta.size()];
		for (int i = 0; i < matrizCustoCompleta.size(); i++){
			List<Double> rowCustoCompleta= matrizCustoCompleta.get(i);
			for (int j = 0; j < matrizCustoCompleta.size(); j++){
				this.matrizCustoCompleta[i][j] = rowCustoCompleta.get(j);
			}
		}
	}
	
	/**
	 * Converte a matriz de caminho mais curto de List<List<Integer>> para tipo primitivo de matriz
	 * 
	 * @param matrizCaminho
	 */
	public void setMatrizCaminho(List<List<Integer>> matrizCaminho) {
		this.matrizCaminho = new int[matrizCaminho.size()][matrizCaminho.size()];
		for (int i = 0; i < matrizCaminho.size(); i++){
			List<Integer> rowCustoCompleta = matrizCaminho.get(i);
			for (int j = 0; j < matrizCaminho.size(); j++){
				this.matrizCaminho[i][j] = rowCustoCompleta.get(j);
			}
		}
	}
	
	/**
	 * Recupera matriz de adjacência e custo do banco de dados, calcula o menor caminho e seu custo
	 * 
	 * @param origem
	 * @param destino
	 * @param autonomia
	 * @param litro
	 * @return JSON vonvertido em String contendo rota e custo do caminho mais curto de um ponto a outro do mapa
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String getRotaECusto(String origem, String destino, Double autonomia, Double litro) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> resposta = new HashMap<String, Object>();
		int i = listaVerticesOrdenados.indexOf(origem);
		int j = listaVerticesOrdenados.indexOf(destino);
		if(matrizCustoCompleta[i][j] == Double.POSITIVE_INFINITY){
			resposta.put("rota", "Não há rota válida.");
			resposta.put("custo", "Custo não calculado.");
		} else{
			String rota = origem;
			Double custo = (matrizCustoCompleta[i][j]/autonomia)*litro;
			resposta.put("custo", custo);
			do {
				i = this.matrizCaminho[i][j];
				rota += "  " + listaVerticesOrdenados.get(i);
			} while (i != j );
			
			resposta.put("rota", rota);
		}
		return mapper.writeValueAsString(resposta);
	}

}