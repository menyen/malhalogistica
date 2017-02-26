package br.com.lojamultinacional;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
 
@Path("/webservice")

public class RESTfulMalhaLogistica {
	@GET
	@Path("/mapa/{cidade}")
	@Produces(MediaType.APPLICATION_JSON)
	public Mapa getMatrizAdjacenciaCidade(@PathParam("cidade") String cidade)
	{
		Mapa mapa = new Mapa();
		mapa.setNome("SP");
		Map<Set<String>, Integer> matriz = new HashMap<>();
		matriz.put(new HashSet<String>(Arrays.asList("A", "B")), 10);
		matriz.put(new HashSet<String>(Arrays.asList("B", "D")), 15);
		matriz.put(new HashSet<String>(Arrays.asList("A", "C")), 20);
		matriz.put(new HashSet<String>(Arrays.asList("C", "D")), 30);
		matriz.put(new HashSet<String>(Arrays.asList("B", "E")), 50);
		matriz.put(new HashSet<String>(Arrays.asList("D", "E")), 30);
		mapa.setMatrizInicialDeDistancia(matriz);
		return mapa;
	}
	
	@POST
	@Path("/mapa")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response criarMapaEmJSON(Mapa mapa) {

		String mapaURI = "http://localhost:8080/malhalogistica/webservice/mapa/"+mapa.getNome();
		return Response.status(201).entity(mapaURI).build();

	}
	
	@GET
	@Path("/mapa/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMenorCaminho(
			@QueryParam("cidade") String cidade, 
			@QueryParam("origem") String origem, 
			@QueryParam("destino") String destino,
			@QueryParam("autonomia") Double autonomia,
			@QueryParam("litro") Double litro) 
					throws JsonGenerationException, JsonMappingException, IOException
	{
		Mapa mapa = new Mapa();
		mapa.setNome("SP");
		Map<Set<String>, Integer> matriz = new HashMap<>();
		matriz.put(new HashSet<String>(Arrays.asList("A", "B")), 10);
		matriz.put(new HashSet<String>(Arrays.asList("B", "D")), 15);
		matriz.put(new HashSet<String>(Arrays.asList("A", "C")), 20);
		matriz.put(new HashSet<String>(Arrays.asList("C", "D")), 30);
		matriz.put(new HashSet<String>(Arrays.asList("B", "E")), 50);
		matriz.put(new HashSet<String>(Arrays.asList("D", "E")), 30);
		mapa.setMatrizInicialDeDistancia(matriz);
		return mapa.getRotaECusto(origem, destino, autonomia, litro);
	}
}
