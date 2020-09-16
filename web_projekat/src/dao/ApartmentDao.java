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
import beans.Host;
import beans.ApartStatus;


import beans.Period;




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
	


}
