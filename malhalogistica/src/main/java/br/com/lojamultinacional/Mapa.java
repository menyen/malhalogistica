package br.com.lojamultinacional;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Books")
public class Mapa {

    private String nome;
    private Map<Set<String>, Long> matrizDeDistancia;
    
    @XmlElement
	public String getNome() {
		return nome;
	}
	public void setNome(String name) {
		this.nome = name;
	}
	
	@XmlElement  
	public Map<Set<String>, Long> getMatrizDeDistancia() {
		return matrizDeDistancia;
	}
	public void setMatrizDeDistancia(Map<Set<String>, Long> matrizDeDistancia) {
		this.matrizDeDistancia = matrizDeDistancia;
	}

}