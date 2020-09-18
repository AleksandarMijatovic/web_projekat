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
			
			//System.out.println("aaaaaaaaaaaaaaaaaAAAAAAA");
			return apartmentService.Create(a);
		});
		
		/*get("/apartments", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			
			return apartmentService.GetAll();
		});*/
		
		get("/apartment/datesForDisable/:id", (req,res) ->  {
		   //  System.out.println("aaaaa"+apartmentService.getOccupiedDates(req.params("id")));
			return apartmentService.zauzetiDatumi(req.params("id"));
		});
		
		get("/apartment/rangesForDisable/:id", (req,res) ->  {
			//System.out.println("Periodi moguci:    "+apartmentService.getOccupiedRanges(req.params("id")));
			return apartmentService.intervalZauzetosti(req.params("id"));
			});
		
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
		
		post("/apartment/reserve", (req,res) -> {
			System.out.println("Ovo je: "+ req.body());
			Reservation r = g.fromJson(req.body(), Reservation.class);
			
			Session ss = req.session(true);
			Guest user = ss.attribute("user");
			
			user.setRentedAppartments(null);
			//user.setReservations(null);
			
			r.setGuest(user);
			
			return apartmentService.reserve(r);
		});
		
		get("/apartments/search/parameters", (req,res) -> {
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
			

			return apartmentService.searchApartments(req.queryParams("location"), req.queryParams("dateFrom"), req.queryParams("dateTo"), req.queryParams("numberOfGuest"), req.queryParams("minRoom"), req.queryParams("maxRoom"), req.queryParams("minPrice"), req.queryParams("maxPrice"), req.queryParams("sortValue"), req.queryParams("type"), req.queryParams("apartmentStatus"),g.fromJson(req.queryParams("amenities"), new TypeToken<List<Amenity>>(){}.getType()),userType,username);
		});
		
		post("/apartment/edit", (req, res) -> 
		apartmentService.Update(g.fromJson(req.body(), Apartment.class)));
		
		delete("/apartment/:id", (req,res) -> apartmentService.Delete(req.params("id")));
	
		get("/apartment/get/reservations", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int whatToGet = -1;
			if(user instanceof Guest)
				whatToGet = 0;
			else if(user instanceof Host)
				whatToGet = 1;
			else 
				whatToGet = 2;
			
			return apartmentService.getAllReservations(whatToGet, user.getUsername());
		});
		
		
		put("/apartment/accept/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReStatus.accepted)));
		
		put("/apartment/reject/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReStatus.rejected)));
		
		put("/apartment/canceled/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReStatus.canceled)));
		
		put("/apartment/finished/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReStatus.done)));
	}
}



