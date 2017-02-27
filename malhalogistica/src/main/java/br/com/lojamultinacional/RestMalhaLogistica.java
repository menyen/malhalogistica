package br.com.lojamultinacional;


import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
 
@Path("/webservice")

public class RestMalhaLogistica {
	@SuppressWarnings("unchecked")
	@GET
	@Path("/mapa/{nome_mapa}")
	@Produces(MediaType.APPLICATION_JSON)
	public Mapa getMatrizCustoCidade(@PathParam("nome_mapa") String nome_mapa)
	{
		Mapa mapa = new Mapa();
		MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
		DB db = dbSingleton.getTestdb();
		DBCollection coll = db.getCollection("mapas"); 
		DBCursor cursor = coll.find(new BasicDBObject("nome", nome_mapa));
		if (cursor.hasNext()) {
			DBObject o = cursor.next();
			mapa.setNome((String) o.get("nome"));
			mapa.setListaVerticesOrdenados((List<String>) o.get("cidades"));
			mapa.setMatrizCustoCompleta((List<List<Double>>) o.get("matriz_custo"));
			mapa.setMatrizCaminho((List<List<Integer>>) o.get("matriz_caminho"));
		}
		return mapa;
	}
	
	@POST
	@Path("/mapa")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response criarMapa(Mapa mapa) {
		FloydWarshall.floydWarshall(mapa);
		MongoDBSingleton dbSingleton = MongoDBSingleton.getInstance();
		DB db = dbSingleton.getTestdb();
		DBCollection coll = db.getCollection("mapas"); 
		DBCursor cursor = coll.find(new BasicDBObject("nome", mapa.getNome()));
		BasicDBObject doc = new BasicDBObject("nome", mapa.getNome()).
				append("cidades", mapa.getListaVerticesOrdenados()).
				append("matriz_custo", mapa.getMatrizCustoCompleta()).
				append("matriz_caminho", mapa.getMatrizCaminho());
		if (cursor.hasNext()) {
			coll.update(new BasicDBObject("nome", mapa.getNome()), doc);
		} else {
			coll.insert(doc);
		}

		String mapaURI = "http://localhost:8080/malhalogistica/webservice/mapa/"+mapa.getNome();
		return Response.status(201).entity(mapaURI).build();

	}
	
	@GET
	@Path("/mapa/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMenorCaminho(
			@QueryParam("nome_mapa") String nome_mapa, 
			@QueryParam("origem") String origem, 
			@QueryParam("destino") String destino,
			@QueryParam("autonomia") Double autonomia,
			@QueryParam("litro") Double litro) 
					throws JsonGenerationException, JsonMappingException, IOException
	{
		Mapa mapa = getMatrizCustoCidade(nome_mapa);
		return mapa.getRotaECusto(origem, destino, autonomia, litro);
	}
	
	
}
