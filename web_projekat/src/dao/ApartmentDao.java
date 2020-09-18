package dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import Decoder.BASE64Decoder;
import beans.Amenity;
import beans.Apartment;
import beans.Guest;
import beans.Host;
import beans.ApartStatus;
import beans.ApartType;
import beans.Period;
import beans.Reservation;




public class ApartmentDao {
	private final String path = "./data/apartments.json";
	private static Gson g = new Gson();
	private UserDao userDao;
	
	public ApartmentDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public List<Apartment> GetAllFromFile() throws JsonSyntaxException, IOException{		
		return g.fromJson((Files.readAllLines(Paths.get(path),Charset.defaultCharset()).size() == 0) ? "" : Files.readAllLines(Paths.get(path),Charset.defaultCharset()).get(0), new TypeToken<List<Apartment>>(){}.getType());
	}
	
	public List<Apartment> GetAll() throws JsonSyntaxException, IOException {
		List<Apartment> lista = GetAllFromFile();
		List<Apartment> retList = new ArrayList<Apartment>();
		
		
		if(lista!=null) {
			for(Apartment item : lista) {
				if(!item.isDeleted())
					retList.add(item);
			}
		}

		
		return retList;
	}
	
	
	
	
	public Apartment Create(Apartment apartment) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		apartment.setId(GetMaxID());
		if(apartments == null) {
			apartments = new ArrayList<Apartment>();
		}
		if(apartment.getDateForRenting().size() > 0)
			apartment.setFreeDateForRenting(setFreeDateFromPeriod(apartment.getDateForRenting().get(0)));
		
		apartment.setStatus(ApartStatus.inactive);
		
		List<String> lista = new ArrayList<String>();
		 
        int numberOfImages=0;
        for(String item : apartment.getPictures()) {
            numberOfImages++;
            String imageString = item.split(",")[1];
 
            BufferedImage image = null;
            byte[] imageByte;
 
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
 
            String imageName= apartment.getId() + "-" + numberOfImages + ".png";
                       
            lista.add("pictures\\" + imageName);
           
            File outputfile = new File(System.getProperty("user.dir")+ "\\static\\pictures\\" + imageName);
            ImageIO.write(image, "png", outputfile);
        }
       
        apartment.setPictures(lista);
		
		apartments.add(apartment);
		SaveAll(apartments);
		return apartment;
	}
	
	private List<Long> setFreeDateFromPeriod(Period period) {
		List<Long> retVal = new ArrayList<Long>();
			
		long temp = period.getDateFrom();
		long endDate = period.getDateTo();

		while(temp <= endDate) {
			retVal.add(temp);
			temp += 24*60*60*1000;
		}
		return retVal;
	}

	public Apartment get(String id) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAll();
		if(apartments != null && id != null) {
			for(Apartment a : apartments) {
				if(a.getId() == Integer.parseInt(id)) {
					return a;
				}
			}
		}
		return null;
	}
	
	public List<Apartment> GetAllApartmentForUser(int typeUser, String username) throws JsonSyntaxException, IOException{		
		List<Apartment> retVal = new ArrayList<Apartment>();
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAll();
		
		if(apartments!=null) {
			for(Apartment a : apartments) {
				Host h = (Host) userDao.get(a.getHost().getUsername());
					if(typeUser == 0) {
						if(a.getStatus()==ApartStatus.active) {
							if(!h.isBlocked())
								retVal.add(a);
						}
					}else if(typeUser == 1) {
						if(a.getHost().getUsername().equals(username)) {
							retVal.add(a);
						}
					}
					else {
						retVal.add(a);
					}
				}
		}
		
		return retVal;
	}
	
	
	
	
	
	
	

	private int GetMaxID() throws JsonSyntaxException, IOException {
		int maxId = 0;
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		if(apartments != null) {
			for(Apartment a : apartments) {
				if(a.getId() > maxId)
					maxId = a.getId();
			}
		}
		return ++maxId;
	}
	
	public void SaveAll(Collection<Apartment> apartments) throws JsonIOException, IOException{
	    Writer writer = new FileWriter(path);
		g.toJson(apartments, writer);
	    writer.close();
	}
	
	public void deleteAllAmenities(int id) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		for(Apartment a : apartments) {
			for(Amenity am : a.getAmenities()) {
				if(am.getId() == id) {
					List<Amenity> amenities = a.getAmenities();
					amenities.remove(am);
					a.setAmenities(amenities);
					break;
				}
			}
		}
		SaveAll(apartments);
	}
	
	public void updateAllAmenities(Amenity amenity) throws JsonSyntaxException, IOException {
		ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
		for(Apartment a : apartments) {
			for(Amenity am : a.getAmenities()) {
				if(am.getId() == amenity.getId()) {
					List<Amenity> amenities = a.getAmenities();
					amenities.remove(am);
					amenities.add(amenity);
					a.setAmenities(amenities);
					break;
				}
			}
		}
		SaveAll(apartments);
	}
	
public List<Apartment> searchApartments(String location, String dateFrom, String dateTo, String numberOfGuest,String minRoom, String maxRoom, String minPrice, String maxPrice, String sortValue, String type, String apartmentStatus,List<Amenity> amenities,int userType , String username) throws JsonSyntaxException, IOException{
		
		
		
		ArrayList<Apartment> list = (ArrayList<Apartment>) GetAllApartmentForUser(userType, username);
		List<Apartment> retVal = new ArrayList<Apartment>();
					
		ApartType tip;
		if(type.equals("soba"))
			tip = ApartType.room;
		else
			tip = ApartType.apartment;
		
		ApartStatus status;
		if(apartmentStatus.equals("aktivan"))
			status = ApartStatus.active;
		else
			status = ApartStatus.inactive;

		//datefrom//dateto
		for(Apartment item : list) {
			if((!location.isEmpty() ? item.getLocation().getAdress().getCity().toLowerCase().contains(location.toLowerCase()) : true) 
					&& (!numberOfGuest.isEmpty()? item.getNumberOfGuest()>=Integer.parseInt(numberOfGuest):true)
					&& ((!minRoom.isEmpty())? (item.getNumberOfRoom()>=Integer.parseInt(minRoom)) :true)
					&&((!maxRoom.isEmpty())? (item.getNumberOfRoom()<=Integer.parseInt(maxRoom)): true)
					&& ((!minPrice.isEmpty())? (item.getPriceForNight()>=Integer.parseInt(minPrice)) :true)
					&&((!maxPrice.isEmpty())? (item.getPriceForNight()<=Integer.parseInt(maxPrice)): true)
					&&((!apartmentStatus.isEmpty())? (item.getStatus()==status): true)
					&&((!type.isEmpty())? (item.getType()==tip): true)) {
				if(!dateFrom.isEmpty() || !dateTo.isEmpty()){
					for(long datum : item.getFreeDateForRenting()) {
						if(((!dateFrom.isEmpty())? datum >= Long.parseLong(dateFrom) : true) && ((!dateTo.isEmpty()) ? datum <= Long.parseLong(dateTo) : true)) {
								if(amenities!=null) {
									if(userType==1) {
										if(item.getStatus()==ApartStatus.active)
											if(uporediListe(item.getAmenities(), amenities))
												retVal.add(item);
									}
									else {
										if(uporediListe(item.getAmenities(), amenities))
											retVal.add(item);
									}
								}else {
									if(userType==1) {
										if(item.getStatus()==ApartStatus.active)
												retVal.add(item);
									}else {
										retVal.add(item);
									}
								}
								
								break;
						}
					}
				}else {
					if(amenities!=null) {
						if(userType==1) {
							if(item.getStatus()==ApartStatus.active)
								if(uporediListe(item.getAmenities(), amenities))
									retVal.add(item);
						}
						else {
							if(uporediListe(item.getAmenities(), amenities))
								retVal.add(item);
						}
					}else {
						if(userType==1) {
							if(item.getStatus()==ApartStatus.active)
									retVal.add(item);
						}else {
							retVal.add(item);
						}
					}
				}
				
			}
					

		}	
		
		

					
		if(sortValue.equals("rastuca")) {
			Collections.sort(retVal, new Comparator<Apartment>() {
				@Override
				public int compare(Apartment o1, Apartment o2) {
					// TODO Auto-generated method stub
					return (int)(o1.getPriceForNight() - o2.getPriceForNight());
				}
			});	
		}else if(sortValue.equals("opadajuca")) {
			Collections.sort(retVal, new Comparator<Apartment>() {
				@Override
				public int compare(Apartment o1, Apartment o2) {
					// TODO Auto-generated method stub
					return (int)(o2.getPriceForNight() - o1.getPriceForNight());
				}
			});	
		}
		
		return retVal;

	}

private boolean uporediListe(List<Amenity> listaApartmana,List<Amenity> listaPretrage){
	
	
	for(Amenity itemPretrage : listaPretrage) {
		boolean postoji=false;
		for(Amenity itemApartmana : listaApartmana) {
			if(itemApartmana.getId()==itemPretrage.getId()) {
				postoji=true;
			}
		}
		if(!postoji) {
			return false;
		}
		
	}
	
	return true;
}

public Apartment Update(Apartment apartment) throws JsonSyntaxException, IOException {
	ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
	
	List<String> lista = new ArrayList<String>();
	 
	
	for(String item : apartment.getPictures()) {
		if(!item.startsWith("data:image")) {
			lista.add(item);
		}
	}
	
    int numberOfImages=0;
    for(String item : apartment.getPictures()) {
        numberOfImages++;
        
        if(item.startsWith("data:image")) {
        	String imageString = item.split(",")[1];
        	BufferedImage image = null;
            byte[] imageByte;
 
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
 
            
            String imageName= apartment.getId() + "-" + numberOfImages + ".png";
            while(lista.contains("pictures\\" + imageName)) {
            	numberOfImages++;
            	imageName= apartment.getId() + "-" + numberOfImages + ".png";
            }
            
            lista.add("pictures\\" + imageName);
           
            File outputfile = new File(System.getProperty("user.dir")+ "\\static\\pictures\\" + imageName);
            ImageIO.write(image, "png", outputfile);
    
        }
    }
    apartment.setPictures(lista);
	
	for(Apartment a : apartments) {
		if(a.getId() == apartment.getId()) {
			apartments.remove(a);
			apartments.add(apartment);
			break;
		}
	}
	SaveAll(apartments);
	return apartment;
}


public Apartment Delete(String id) throws JsonSyntaxException, IOException {
	ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
	Apartment retVal = null;
	for(Apartment a : apartments) {
		if(a.getId() == Integer.parseInt(id)) {
			a.setDeleted(true);
			retVal = a;
			break;
		}
	}
	SaveAll(apartments);
	return retVal;
}


private List<Period> sortApartmentPeriods(List<Period> forSort){
	List<Period> retVal = new ArrayList<Period>();
	Date t = new Date();
    @SuppressWarnings("deprecation")
	Date today = new Date(t.getYear(),t.getMonth(), t.getDate());
	for(Period p : forSort) {
		if(p.getDateFrom() >= today.getTime()) {
			int i = 0;
			boolean added = false;
			for(Period po : retVal) {
				if(po.getDateFrom() < p.getDateFrom()) {
					retVal.add(i, p);
					added = true;
					break;
				}
				
				i++;
			}		
			if(!added)
				retVal.add(p);
		}
	}	
	return retVal;
}

public List<Period> intervalZauzetosti(String id) throws JsonSyntaxException, IOException{
	Apartment apartment = get(id);
	List<Period> retVal = new ArrayList<Period>();

	List<Period> sortedPeriods = sortApartmentPeriods(apartment.getDateForRenting());
	//int max=sortedPeriods.size();
	//retVal.add(new Period( 0,sortedPeriods.get(max).getDateTo()+24*60*60*1000));
	
	Period p1=new Period();
	Period p2=new Period();
	
	for(int i=0;i<sortedPeriods.size()-1;i++) {
		p1=sortedPeriods.get(i);
		p2=sortedPeriods.get(i+1);
		Date temp1 = new Date(p1.getDateFrom());
		Date dateTo1 = new Date(p1.getDateTo());
		//System.out.println("OOOOOODDDDDDDDDD:    "+ temp1+"    Dooooo: "+dateTo1);
		retVal.add(new Period(p2.getDateTo()+24*60*60*1000,p1.getDateFrom()));
		
		
	}
	
	
	
	retVal.add(new Period(sortedPeriods.get(0).getDateTo()+24*60*60*1000, 0));
	
	return retVal;
}

public List<Long> zauzetiDatumi(String id) throws JsonSyntaxException, IOException{
	List<Long> retVal = new ArrayList<Long>();
	if(id != null) {
		Apartment apartment = get(id);
		for(Period p : apartment.getDateForRenting()) {
			Date temp = new Date(p.getDateFrom());
			Date dateTo = new Date(p.getDateTo());
			
			
			Calendar c = Calendar.getInstance(); 
			while(temp.compareTo(dateTo) <= 0) {
				if(!apartment.getFreeDateForRenting().contains(temp.getTime())) {
					retVal.add(temp.getTime());
				}
				c.setTime(temp); 
				c.add(Calendar.DAY_OF_YEAR, 1);
				temp = c.getTime();
			}
		}
	}
	return retVal;
}

public List<Reservation> getAllReservations(int whatToGet, String username) throws JsonSyntaxException, IOException{
	List<Reservation> retVal = new ArrayList<Reservation>();
	ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
	
	for(Apartment a : apartments) {
		for(Reservation r : a.getReservations()) {
			if(whatToGet == 0) {
				if(r.getGuest().getUsername().equals(username)) {
					r.setAppartment(new Apartment(a.getId(),a.getType(),a.getNumberOfRoom(),a.getNumberOfGuest(),a.getLocation(), null, null, null, null, null,0,"", "", null, null, null));
					retVal.add(r);
				}
			}else if(whatToGet == 1) {
				if(a.getHost().getUsername().equals(username)) {
					r.setAppartment(new Apartment(a.getId(),a.getType(),a.getNumberOfRoom(),a.getNumberOfGuest(),a.getLocation(), null, null, null, null, null,0,"", "", null, null, null));
					retVal.add(r);
				}
			}
			else {
				r.setAppartment(new Apartment(a.getId(),a.getType(),a.getNumberOfRoom(),a.getNumberOfGuest(),a.getLocation(), null, null, null, null, null,0,"", "", null, null, null));
				retVal.add(r);
			}
		}
	}
		
	return retVal;
}
private int GetMaxIDForReservation() throws JsonSyntaxException, IOException  {
	int maxId = 0;
	ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
	for(Apartment a : apartments) {
		for(Reservation r : a.getReservations()) {
			if(r.getId() > maxId)
				maxId = r.getId();
		}
	}
	return ++maxId;
}


public boolean reserve(Reservation reservation) throws JsonSyntaxException, IOException {
	ArrayList<Apartment> apartments = (ArrayList<Apartment>) GetAllFromFile();
	boolean retVal = false;
	Guest g = (Guest) userDao.get(reservation.getGuest().getUsername());
	
	
	List<Reservation> guestReservations = g.getReservations();
	Apartment ap = null;
	for(Apartment a : apartments) {
		if(a.getId() == reservation.getAppartment().getId()) {
			List<Reservation> temp = a.getReservations();
			
			ap = a;
			if(temp == null)
				temp = new ArrayList<Reservation>();
			
			reservation.setId(GetMaxIDForReservation());				
			reservation.setAppartment(null);
			temp.add(reservation);
			a.setReservations(temp);
			a.setFreeDateForRenting(setFreeDaysForRenting(a.getFreeDateForRenting(),reservation));
			retVal = true;
			break;
		}
	}
	SaveAll(apartments);
	
	ap.setReservations(null);
	
	reservation.setAppartment(ap);
	reservation.setGuest(null);
	guestReservations.add(reservation);
	System.out.println(reservation.toString());
	userDao.Update(g);
	
	return retVal;
}

private List<Long> setFreeDaysForRenting(List<Long> freeDateForRenting, Reservation reservation) {
	
	List<Long> retVal = new ArrayList<Long>();
	List<Long> reservationDates = new ArrayList<Long>();
	long temp = reservation.getStartDate();
	long endDate = reservation.getStartDate() + reservation.getDaysForStay()*24*60*60*1000;

	while(temp <= endDate) {
		reservationDates.add(temp);
		temp += 24*60*60*1000;
	}
			
	for(long date : freeDateForRenting) {
		if(!reservationDates.contains(date)) {
			retVal.add(date);
		}
	}
	
	return retVal;
}



}
