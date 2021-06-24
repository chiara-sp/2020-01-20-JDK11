package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<String> getRuoli(){
		String sql="select distinct role "
				+ "from authorship "
				+ "order by role";
		List<String> result= new LinkedList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				
				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
	public void getAllArtists(Map<Integer,Artist> mappa) {
		String sql="select * from artists";
		//List<Artist> result= new LinkedList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Artist a=new Artist(res.getInt("artist_id"),res.getString("name"));
				mappa.put(a.getId(),a);
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}	
	}
	public List<Artist> getArtistByRole(String ruolo, Map<Integer,Artist>mappa){
		String sql="select distinct artist_id as id "
				+ "from authorship "
				+ "where role=?";
		List<Artist> result= new LinkedList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Artist a= mappa.get(res.getInt("id"));
				if(a!=null) {
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	public List<Adiacenza> getAdiacenze(Map<Integer,Artist>mappa, String ruolo){
		String sql="select a1.`artist_id`as id1, a2.`artist_id` as id2, count(distinct eo1.`exhibition_id`) as peso "
				+ "from exhibition_objects eo1, exhibition_objects eo2, `authorship` a1, authorship a2, objects o1, objects o2 "
				+ "where eo1.`exhibition_id`=eo2.`exhibition_id` and a1.`artist_id`<a2.`artist_id` and a1.`role`=a2.`role` and o1.`object_id`=a1.`object_id` "
				+ "and o2.`object_id`=a2.`object_id` and o1.`object_id`=eo1.`object_id`and o2.`object_id`=eo2.`object_id` and a1.role=? and a1.`role`=a2.`role` "
				+ "group by a1.`artist_id`, a2.`artist_id`";
		List<Adiacenza>result= new LinkedList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Artist a1= mappa.get(res.getInt("id1"));
				Artist a2= mappa.get(res.getInt("id2"));
				if(a1!=null && a2!=null) {
					result.add(new Adiacenza(a1,a2,res.getInt("peso")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
}
