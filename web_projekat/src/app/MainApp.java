package app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import beans.Administrator;
import beans.Gender;
import beans.User;
import controller.AmenityController;
import controller.ApartmentController;
import controller.UserController;
import dao.ApartmentDao;
import dao.UserDao;
import service.AmenityService;
import service.ApartmentService;
import service.UserService;


public class MainApp {

	public static void main(String[] args) throws Exception{
				port(41);
				staticFiles.externalLocation(new File("./static").getCanonicalPath());

				
						
						 // UserDao dao = new UserDao();
						//  List<User> users=dao.GetAll();
						 // User user = dao.Login("admin","admin");
						 // for(User u : users)
						 // System.out.println(u);
			/*	User a = new Administrator("admin", "admin", "Aleksandar", "Mijatovic",
						  Gender.male);
						
						//  UserDao dao = new UserDao();
						//  dao.AddUser(a);
						  dao.GetAll();*/
				get("/test", (req, res) -> {
					return "Works";
				});
				
				UserDao userDao = new UserDao();
				ApartmentDao apartmentDAO = new ApartmentDao(userDao);

				
				new UserController(new UserService(userDao));
				new AmenityController(new AmenityService());
				new ApartmentController(new ApartmentService(apartmentDAO));

				System.out.println("asdad");
			}

		}

	


