package controller;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import adapterdao.RuntimeTypeAdapterFactory;
import beans.Login;
import beans.Administrator;
import beans.Guest;
import beans.Host;
import beans.User;

import service.UserService;
import spark.Session;

public class UserController {
	
	private static Gson gs = new Gson();
	
	public UserController(final UserService userService) {
		
		post("/users/registration", (req, res) ->{ 		
			User u = gs.fromJson(req.body(), Host.class);		
			return userService.Register(u);		
		});

		
	get("/users/:username", (req,res) -> userService.getUser(req.params("username")));
	
	post("/users/login", (req, res) -> {
		res.type("application/json");
		User u = userService.Login(gs.fromJson(req.body(), Login.class));
		if(u != null) {
			if(!u.isBlocked()) {
				Session ss = req.session(true);
				User user = ss.attribute("user");
				if (user == null) {
					user = u;
					ss.attribute("user", user);
				}
				return gs.toJson(user);
			}
		}
		return "";
	});
	
	get("/users/log/test", (req, res) -> {
		res.type("application/json");
		Session ss = req.session(true);
		User user = ss.attribute("user");
		return gs.toJson(user);
	});
	
	}
}
