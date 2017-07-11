/**
 * 
 */
package fr.epita.iam.launcher;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DaoSaveException;
import fr.epita.iam.exceptions.DaoSearchException;
import fr.epita.iam.services.Authenticator;
import fr.epita.iam.services.FileIdentityDAO;
import fr.epita.iam.services.IdentityDAO;
import fr.epita.iam.services.JDBCIdentityDAO;
import fr.epita.logging.LogConfiguration;
import fr.epita.logging.Logger;

/**
 * @author tbrou
 *
 */
public class Launcher {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 * @throws DaoSearchException 
	 */
	public static void main(String[] args) throws FileNotFoundException, SQLException, DaoSearchException, DaoSaveException {

		JDBCIdentityDAO dao = new JDBCIdentityDAO();
		
		
		LogConfiguration conf = new LogConfiguration("./tmp/application.log");
		Logger logger = new Logger(conf);
		
	    logger.log("beginning of the program");
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("User name :"); // setting username and passord for the admin to access the system
		String userName = scanner.nextLine();
		System.out.println("Password :");
		String password = scanner.nextLine();
		
		if (!Authenticator.authenticate(userName, password)) { //check weather uname and pwd are correct or not
			logger.log("unable to authenticate "  + userName);
			return;
		} else {
			System.out.println("Successfully authenticated");//succesfull authentication after correct uname and pwd
			// We are authenticated
			String answer = "";
			while (!"3".equals(answer)) {
			
				System.out.println("1. Create Identity"); // creating new data of a person
				System.out.println("2. Search and ->>update or delete :");// searching the hole and updating the values to it
				System.out.println("3. Quit");// exiting the system
				System.out.println("your choice : ");//enter your choice
				
				logger.log("User chose the " + answer + " choice");// taking choice number as input

				answer = scanner.nextLine();

				switch (answer) {//if 1 is enterned ne data is created
				case "1":
					System.out.println("Identity Creation");
					logger.log("selected the identity creation");
					System.out.println("input the identity display name :");
					String displayName  = scanner.nextLine();
					System.out.println("identity email :");
					String email = scanner.nextLine();
					System.out.println("birthdate :");
					String birthdate = scanner.nextLine();
					
					Identity identity = new Identity( null,displayName, email, birthdate);
					try {
						dao.save(identity);
						System.out.println("the save operation completed successfully");
					} catch (DaoSaveException e) {
						System.out.println("the save operation is not able to complete, details :" + e.getMessage());
					}
					
					break;
				case "2":// if 2 is selected it will take you into search and update and delete option

					// Update Identity
					
					System.out.println("Identity Update");
					Identity criteria = new Identity( null,null, null, null);
					List<Identity> identities = dao.search(criteria);
					System.out.println("Availabe Identities");
					for(Identity idens : identities)
					{
						
						System.out.print("Name : "+idens.getDisplayName()+"\t");
						System.out.print("Date of Birth : "+idens.getBirthdate()+"\t");
						System.out.println("Email : "+idens.getEmail());
						
					}
					System.out.println("1.update");// nested values of update and delete
					System.out.println("2.delete");//nested values of search ->delete
					System.out.println("Exit.");//exiting the search system
					System.out.println("your choice : ");//enetering the choice
					Scanner sc = new Scanner(System.in);
					String ch = sc.nextLine();
					switch(ch)// updating the values of a person selecting update
					{
					case "1":
						System.out.println("input the identity display name to update identity :");
						String name  = scanner.nextLine();// taking iput of a display name
					System.out.println("please input the identity display name :");
					String displayName1  = scanner.nextLine();//taking input of a email of a person
					System.out.println("identity email :");
					String email1 = scanner.nextLine();//taking input of a of a person
					System.out.println("birthdate :");
					String birthdate1 = scanner.nextLine();
					
					Identity identity1 = new Identity( null,displayName1, email1, birthdate1);// setting the valuesof a person uid,bname,emailand bithdate
						dao.update(identity1,name);
						System.out.println("Update successfull");
						break;
					case "2":System.out.println("input the identity display name to update :");
							String name1  = scanner.nextLine();
							dao.delete(name1);
							
						break;
					case "3" : break;
					}
					break;

				case "3":

					System.out.println("quitting Thankyou :)");
					break;
				default:

					System.out.println("input is not recognized use 1,2,3 else use 4 to quit");
					break;
				}

			}

		}

	}


}
