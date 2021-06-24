package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
	private List<Artist>soluzione;
	
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
	public boolean artistaPresente(int id) {
		Artist a= artisti.get(id);
		if(grafo!= null && a!=null && grafo.vertexSet().contains(a))
			return true;
		return false;
	}
	public List<Artist> doRicorsione(int idArtista){
		Artist partenza= artisti.get(idArtista);
		soluzione = new LinkedList<>();
		LinkedList<Artist> parziale= new LinkedList<>();
		parziale.add(partenza);
		cerca(1, parziale);
		return soluzione;
		
	}
	private void cerca(int pesoTotale, LinkedList<Artist> parziale) {
		

		//condizione di terminazione
		if(parziale.size()>soluzione.size()) {
			soluzione= new LinkedList<>(parziale);
			
		}
		Artist ultimo= parziale.get(parziale.size()-1);
		for(DefaultWeightedEdge edge: grafo.outgoingEdgesOf(ultimo)) {
			if(grafo.getEdgeWeight(edge)==pesoTotale) {
				Artist prossimo= Graphs.getOppositeVertex(grafo, edge, ultimo);
				if(!parziale.contains(prossimo)) {
					parziale.add(prossimo);
				cerca(pesoTotale,parziale);
				parziale.remove(parziale.size()-1);
				}
				
			}else {
				Artist prossimo= Graphs.getOppositeVertex(grafo, edge, ultimo);
				if(!parziale.contains(prossimo)) {
					parziale.add(prossimo);
				cerca((int) grafo.getEdgeWeight(edge),parziale);
				parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}
}
