import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String separator = System.getProperty("file.separator");        
        File currentDir = new File(System.getProperty("user.dir"));
        File newDir;
        if(argv.size() < 2)
            System.out.println("Pas assez d'arguments -> cd <path>");
        else if(argv.size() > 2)
            System.out.println("Trop d'arguments -> cd <path>");
        else{
            Pattern p = Pattern.compile(String.format("(\\.\\.)(%s\\.\\.)*", separator));
            Matcher m = p.matcher(argv.get(1));
            if(m.matches()){
                System.out.println("Match");
                Pattern motif = Pattern.compile("(\\.\\.)");
                Matcher recherche = motif.matcher(argv.get(1));
                while(recherche.find()){
                    try{
                        currentDir = currentDir.getAbsoluteFile().getParentFile();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                System.setProperty("user.dir", currentDir.toString());
            }
            else{
                newDir = new File(argv.get(1));

                if(newDir.getAbsoluteFile().isDirectory()){
                    System.setProperty("user.dir", newDir.getAbsoluteFile().toString());
                }
                else{
                   System.out.println(newDir.toString() + " n'est pas un dossier."); 
                }
            }
        }
    }
    
    public static void execute_commande_date(ArrayList<String> argv){
        SimpleDateFormat formater = null;
        Date now = new Date();

        if(argv.size() == 1)
            formater = new SimpleDateFormat("yy-MM-dd");

        System.out.println(formater.format(now));
    }
    
    public static void execute_commande_find(ArrayList<String> argv){
        System.out.println("Execution de find");
    }
    
}