package controller;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

import beans.Amenity;
import service.AmenityService;

public class AmenityController {
	private static Gson g = new Gson();

	public AmenityController(final AmenityService amenityService) {

		post("/amenity/add", (req, res) -> 
					amenityService.Create(g.fromJson(req.body(), Amenity.class)));


		get("/amenity", (req,res) -> amenityService.GetAll());

		post("/amenity", (req, res) -> 
						amenityService.Update(g.fromJson(req.body(), Amenity.class)));
		
		delete("/amenity/:id", (req,res) -> amenityService.Delete(req.params("id")));

	}
}