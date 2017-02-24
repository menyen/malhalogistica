package br.com.lojamultinacional;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;

public class MapaDeserializer extends StdDeserializer<Mapa> {

	public MapaDeserializer() { 
        this(null); 
    } 
 
    public MapaDeserializer(Class<?> vc) { 
        super(vc); 
    }
 
    @Override
    public Mapa deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
    	Mapa mapa = new Mapa();
        JsonNode node = jp.getCodec().readTree(jp);
        
        String nome =  node.get("nome").getValueAsText();
        mapa.setNome(nome);
        
        Map<Set<String>, Integer> edges = new HashMap<>();
        Iterator<Entry<String, JsonNode>> matrizDeDistancia = node.get("matrizDeDistancia").getFields();
        while (matrizDeDistancia.hasNext()){
        	Entry<String, JsonNode> edge = matrizDeDistancia.next();
        	Set<String> vertices = new HashSet<String>(Arrays.asList(edge.getKey().split(", ")));
        	edges.put(vertices, edge.getValue().getValueAsInt());
        }
        mapa.setMatrizInicialDeDistancia(edges);
 
        return mapa;
    }
}
