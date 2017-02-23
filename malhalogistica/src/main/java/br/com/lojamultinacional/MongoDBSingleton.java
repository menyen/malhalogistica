package br.com.lojamultinacional;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBSingleton {

	private static MongoDBSingleton mDbSingleton;

	private static MongoClient mongoClient;

	private static DB db ;


	private static final String dbHost = "localhost";
	private static final int dbPort = 27017;
	private static final String dbName = "mapas";

	private MongoDBSingleton(){};

	public static MongoDBSingleton getInstance(){
		if(mDbSingleton == null){
			mDbSingleton = new MongoDBSingleton();
		}
		return mDbSingleton;
	} 

	@SuppressWarnings("deprecation")
	public DB getTestdb(){
		if(mongoClient == null){
			mongoClient = new MongoClient(dbHost , dbPort);
		}
		if(db == null)
			db = mongoClient.getDB(dbName);
		return db;
	}
}
