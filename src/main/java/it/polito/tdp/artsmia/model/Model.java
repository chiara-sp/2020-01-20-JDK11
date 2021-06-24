package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private Map<Integer,Artist> artisti;
	private SimpleWeightedGraph<Artist, DefaultWeightedEdge>grafo;
	
	public Model() {
		dao= new ArtsmiaDAO();
		artisti= new HashMap<Integer,Artist>();
		dao.getAllArtists(artisti);
	}
	public List<String> getRuoli(){
		return dao.getRuoli();
	}
	public void creaGrafo(String ruolo) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getArtistByRole(ruolo, artisti));
		
		for(Adiacenza a: dao.getAdiacenze(artisti, ruolo)) {
			Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<Adiacenza> getAdiacenze(String ruolo){
		List<Adiacenza> lista= dao.getAdiacenze(artisti, ruolo);
		Collections.sort(lista);
		return lista;
	}
	public boolean grafoCreato() {
		if(grafo==null)
			return false;
		return true;
	}
}
