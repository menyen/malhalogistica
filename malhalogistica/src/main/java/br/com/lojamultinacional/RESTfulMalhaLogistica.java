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
		Map<Set<String>, Long> matriz = new HashMap<>();
		matriz.put(new HashSet<String>(Arrays.asList("A", "B")), (long) 10);
		matriz.put(new HashSet<String>(Arrays.asList("B", "D")), (long) 15);
		matriz.put(new HashSet<String>(Arrays.asList("A", "C")), (long) 20);
		matriz.put(new HashSet<String>(Arrays.asList("C", "D")), (long) 30);
		matriz.put(new HashSet<String>(Arrays.asList("B", "E")), (long) 50);
		matriz.put(new HashSet<String>(Arrays.asList("D", "E")), (long) 30);
		mapa.setMatrizDeDistancia(matriz);
		return mapa;
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(Object mapa) {

		String result = "Mapa salvo : " + mapa;
		System.out.println(mapa);
		return Response.status(201).entity(result).build();

	}
}
