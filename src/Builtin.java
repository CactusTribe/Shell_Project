import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/* Classe Builtin
 *
 * Contient les commandes internes du shell (ls, ps, pwd, cd)
 */
public class Builtin{

	public static void execute_commande_exit(ArrayList<String> argv){
		System.exit(0);
	}

	public static void execute_commande_ls(ArrayList<String> argv){
		File dossier = new File(System.getProperty("user.dir"));
		String enfants[] = dossier.list();
		
		for(String fils: enfants){
			System.out.println(fils);
		}	
	} 

	public static void execute_commande_ps(ArrayList<String> argv){
		System.out.println("Execution de ps");
	} 

	public static void execute_commande_pwd(ArrayList<String> argv){
		String pwd = System.getProperty("user.dir");
		System.out.println(pwd);
	} 

	public static void execute_commande_cd(ArrayList<String> argv){
		System.out.println("Execution de cd");
	} 

	public static void execute_commande_date(ArrayList<String> argv){
		System.out.println("Execution de date");
	} 

	public static void execute_commande_find(ArrayList<String> argv){
		System.out.println("Execution de find");
	} 

}