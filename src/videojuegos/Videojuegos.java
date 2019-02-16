package videojuegos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoCredential;
import com.mongodb.MongoSecurityException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class Videojuegos {
	private final static String SERVER = "localhost";
	private final static int PORT = 27017;
	private final static String USER = "gamer"; 
	private final static String PASS = "mygamer"; 
	private final static String BD = "videojuegos"; 
	private final static MongoCredential CREDENCIALES = MongoCredential.createCredential(USER, BD, PASS.toCharArray());
	private static MongoClient dbClient = null;
	private static MongoDatabase db = null;
	private static MongoCollection<Document> videogames = null;
	private static MongoCollection<Document> companies = null;
	
	public static void main(String[] args) {	
		try {
			
			// Deactivate logs MongoDB
			Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
			mongoLogger.setLevel(Level.SEVERE);
			
			// Create and save a MongoDB client
			dbClient = createClientMongoDB();
			
			if( dbClient != null ) {
				// Get DB and its collection to use
				db = dbClient.getDatabase( "videojuegos" );
				videogames = db.getCollection( "videojuegos" );
				companies = db.getCollection( "companyias" );
				
				// Show menu and process option for the first time.
				processMenuOption( showMenu() );
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}catch (MongoSecurityException e) {
			e.printStackTrace();
		}catch(MongoClientException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a new client for MongoDB with credentials
	 * @return MongoClient
	 */
	private static MongoClient createClientMongoDB() throws MongoSecurityException {
		return new MongoClient(
			new ServerAddress( SERVER, PORT ),
			Arrays.asList( CREDENCIALES )	
		);
	}

	/**
	 * Show menu
	 */
	private static void menu() {
		System.out.flush();
		System.out.println( "VIDEOJUEGOS MONGODB" );
		System.out.println( "-------------------" );
		System.out.println( "" );
		System.out.println( "1 - Listado de videojuegos ordenado por a�o" );
		System.out.println( "2 - Listado de compa��as" );
		System.out.println( "3 - Listado de videojuegos por compa��a (ordenado por compa��a)" );
		System.out.println( "4 - B�squeda de videojuegos por nombre (b�squeda parcial)" );
		System.out.println( "0 - Salir" );
		System.out.println( "" );
		System.out.print( "Elige una opci�n:" );
	}
	
	/**
	 * Show menu options and get the option menu as a number
	 */
	private static String showMenu() throws IOException {
		BufferedReader _reader = new BufferedReader( new InputStreamReader( System.in ) );
		String _optionSelected = null;
		
		// Shows menu
		menu();
		
		// Get the option
		_optionSelected = _reader.readLine();
		System.out.print( _optionSelected );
		System.out.println( "" );
		System.out.println( "Procesando solicitud..." );
		
		return _optionSelected.trim();
	}
	
	/**
	 * Process the menu option and run the correct function
	 * @param _menuOption
	 */
	private static void processMenuOption( String _menuOption ) {
		try {
			BufferedReader _reader = new BufferedReader( new InputStreamReader( System.in ) );
			switch ( _menuOption ) {
				case "1":
					getVideogamesOrderByYear();
					System.out.println( "" );
					System.out.println( "Presiona cualquier tecla para volver al men�." );
					if( _reader.readLine() != null ) {
						processMenuOption( showMenu() );
					}
					break;
				case "2":
					break;
				case "3":
					break;
				case "4":
					break;
				case "0":
					//Finish program
					System.out.println( "Finaliza el programa" );
					break;
				default:
					// Bad request. Restart program.
					System.out.println( "Opci�n incorrecta!" );
					processMenuOption( showMenu() );
					break;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}catch(MongoCommandException e) {
			e.printStackTrace();
		}catch (MongoTimeoutException e) {
			e.printStackTrace();
		}
	}
	
	private static void getVideogamesOrderByYear() throws MongoCommandException {
		System.out.println( "" );
		System.out.println( "- Videojuegos -" );
		System.out.println( "" );
		
		// Get all the videogames sort by "anyo" field ascending
		// and show its info
		System.out.println( "Date\tTitle" );
		for(Document infoVideogame: videogames.find().sort( Sorts.ascending( "anyo" ) ) ) {
			System.out.println( 
				String.format( 
					"%s\t%s", 
					infoVideogame.getInteger( "anyo" ), 
					infoVideogame.getString( "titulo" ) 
				) 
			);
		}
		
	}
}
