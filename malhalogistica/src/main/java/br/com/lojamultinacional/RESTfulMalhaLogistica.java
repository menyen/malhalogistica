package br.com.lojamultinacional;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/webservice")

public class RESTfulMalhaLogistica {
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Mapa getStartingPage()
	{
		Mapa mapa = new Mapa();
		mapa.setNome("Sao Paulo");
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
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(Mapa mapa) {

		FloydWarshall.floydWarshall(mapa.getMatrizInicialArestas(), mapa.getNumberOfVertices());
		return Response.status(200).build();

	}
}
