package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MaquinaMongoDB {

    private MongoCollection<Document> collection;

    public MaquinaMongoDB() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("expendedoras");
        collection = db.getCollection("maquinas");
    }

    public void insertarMaquina(MaquinaExpendedora m) {
        Document doc = Mapper.maquinaToDocument(m);
        collection.insertOne(doc);
    }

    public List<MaquinaExpendedora> obtenerTodasLasMaquinas() {
        List<MaquinaExpendedora> lista = new ArrayList<>();

        FindIterable<Document> docs = collection.find();

        for (Document d : docs) {
            MaquinaExpendedora m = Mapper.documentToMaquina(d);
            lista.add(m);
        }

        return lista;
    }
}