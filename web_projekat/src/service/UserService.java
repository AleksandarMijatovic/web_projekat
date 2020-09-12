package service;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import beans.Login;
import beans.Administrator;
import beans.Guest;
import beans.Host;
import beans.User;
import dao.UserDao;

public class UserService {
	
	private UserDao userdao;
	
	public UserService(UserDao userDao) {
		this.userdao = userDao;
		
	}
	public User Login(Login data) {		
		try {
			return userdao.Login(data.getUsername(),data.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
