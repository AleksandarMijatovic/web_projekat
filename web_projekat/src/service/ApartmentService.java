package service;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.Apartment;

import dao.ApartmentDao;

public class ApartmentService {
	private static Gson g = new Gson();
	private static ApartmentDao apartmentDao;
	
	public ApartmentService(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}
	
	public String Create(Apartment apartment) throws JsonSyntaxException, IOException {
		try {
			apartmentDao.Create(apartment);
		}  catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return g.toJson(apartment);		
	}
	
	public String GetAll() {
		try {
			return g.toJson(apartmentDao.GetAllFromFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
