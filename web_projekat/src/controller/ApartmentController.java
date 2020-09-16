package controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import beans.Amenity;
import beans.Apartment;
import beans.Comment;
import beans.Guest;
import beans.Host;
import beans.Reservation;
import beans.ReStatus;
import beans.User;
import service.ApartmentService;
import spark.Session;

public class ApartmentController {
	private static Gson g = new Gson();

	public ApartmentController(final ApartmentService apartmentService) {
		
		post("/apartment/add", (req, res) -> {
			Session ss = req.session(true);
			Host user = ss.attribute("user");
			Apartment a = g.fromJson(req.body(), Apartment.class);
			//user.setAppartments(null)
			a.setHost(user);
			
			System.out.println("aaaaaaaaaaaaaaaaaAAAAAAA");
			return apartmentService.Create(a);
		});
		
		/*get("/apartments", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			
			return apartmentService.GetAll();
		});*/
		
		get("/apartment/:id", (req,res) -> apartmentService.getApartment(req.params("id")));

		get("/apartments", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int userType = -1;
			if(user instanceof Guest || user==null)
				userType = 0;
			else if(user instanceof Host)
				userType = 1;
			else 
				userType = 2;
			
			String username;
			if(user==null) 
				username="";
			else {
				username=user.getUsername();
			}
			
			
			
			return apartmentService.GetAllForUser(userType,username);
			
		});
	}
}



