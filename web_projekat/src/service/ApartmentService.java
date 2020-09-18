package service;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.Apartment;
import beans.ReStatus;
import beans.Reservation;
import beans.Amenity;

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
	
	public String getApartment(String id) {
		try {
			return g.toJson(apartmentDao.get(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return g.toJson(null);
	}
	
	public String GetAllForUser(int userType, String username) {
		try {
			return g.toJson(apartmentDao.GetAllApartmentForUser(userType, username));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String searchApartments(String location, String datFrom, String dateTo, String numberOfGuest,String minRoom, String maxRoom, String minPrice, String maxPrice, String sortValue, String type, String apartmentStatus,List<Amenity> amenities, int userType , String username) {
		try {
			return g.toJson(apartmentDao.searchApartments(location, datFrom, dateTo, numberOfGuest, minRoom, maxRoom, minPrice, maxPrice,sortValue,type,apartmentStatus,amenities,userType,username));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String Update(Apartment apartment) {
		try {
			//TODO
			apartmentDao.Update(apartment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g.toJson(apartment);		
	}
	
	public String Delete(String id) {
		try {
			return g.toJson(apartmentDao.Delete(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public String zauzetiDatumi(String id) {
		try {
			return g.toJson(apartmentDao.zauzetiDatumi(id));
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String intervalZauzetosti(String id) {
		try {
			return g.toJson(apartmentDao.intervalZauzetosti(id));
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean reserve(Reservation reservation) {
		try {
			return apartmentDao.reserve(reservation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String getAllReservations(int whatToGet, String username) {
		try {
			return g.toJson(apartmentDao.getAllReservations(whatToGet,username));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g.toJson(null);
	}
	
	public boolean changeReservationStatus(String id, ReStatus status) {
		try {
			return apartmentDao.changeReservationStatus(id, status);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
