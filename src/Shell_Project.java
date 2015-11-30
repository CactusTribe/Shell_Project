import java.util.*;

public class Shell_Project{

	private Scanner sc;
	private String argl; // Ligne de commande brute
	private ArrayList<String> argv; // Liste des arguments

	/*
	 * Constructeur
	 */
	public Shell_Project(){
		sc = new Scanner(System.in);
		argv = new ArrayList<String>(); 

		// Boucle infinie du shell (S'arrête grâce à Ctrl+D ou bien exit)
		while(true){
			display_prompt();
			read_command();
			tokenize_command();
			execute_command();
		}
	}

	/*
	 * Affiche une nouvelle ligne pour la prochaine commande
	 */
	public void display_prompt(){
		System.out.print("MyShell$ ");
	}

	/*
	 * Stock la commande complete dans argl
	 */
	public void read_command(){
		try{
			argl = sc.nextLine();

		}catch (NoSuchElementException e){ // Exception levé lors de Ctrl+D
			argl = "exit";
			System.out.println("exit");
		}
	}

	/*
	 * Découpe la commande en arguments distincts
	 */
	public void tokenize_command(){
		argv = new ArrayList<String>(Arrays.asList(argl.split("\\s*,\\s*")));
	}

	/*
	 * Execute la commande contenue dans argl
	 */
	public void execute_command(){
		if(argl.equals("exit"))
			Builtin.execute_commande_exit(argv);
		else if(argl.equals("ls")){
			Builtin.execute_commande_ls(argv);
		}
		else if(argl.equals("ps")){
			Builtin.execute_commande_ps(argv);
		}
		else if(argl.equals("pwd")){
			Builtin.execute_commande_pwd(argv);
		}
		else if(argl.equals("cd")){
			Builtin.execute_commande_cd(argv);
		}
		else if(argl.equals("date")){
			Builtin.execute_commande_date(argv);
		}
		else if(argl.equals("find")){
			Builtin.execute_commande_find(argv);
		}
		else{
			System.out.format("La commande %s n'existe pas.\n", argl);
		}
	}

	public static void main(String[] args){
		Shell_Project shell = new Shell_Project();
	}
}