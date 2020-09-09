package app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import java.io.File;
public class MainApp {

	public static void main(String[] args) throws Exception{
				port(41);
				staticFiles.externalLocation(new File("./static").getCanonicalPath());

			
				
				System.out.println("asdad");
			}

		}

	


