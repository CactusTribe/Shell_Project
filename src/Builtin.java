import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Classe Builtin
 *
 * Contient les commandes internes du shell (ls, ps, pwd, cd)
 */
public class Builtin{
    
    /**
     * Quitte le Shell
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_exit(ArrayList<String> argv){
        System.exit(0);
    }
    
    /**
     * Liste les fichiers du répertoire courant
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_ls(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread ls = new Thread(){
                @Override
                public void run() {
                    execute_commande_ls(argv);
                }
            };
            ls.setName("ls");
            ls.start();
            Shell_Project.procList.add(ls);
        }
        else{
            File dossier = new File(System.getProperty("user.dir"));
            String enfants[] = dossier.list();

            for(String fils: enfants){
                System.out.println(fils);
            }
        }
    }
    
    /**
     * Affiche les processus en cours d'exécution
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_ps(ArrayList<String> argv, LinkedList<Thread> procList){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread ps = new Thread(){
                @Override
                public void run() {
                    execute_commande_ps(argv, procList);
                }
            };
            ps.setName("ps");
            ps.start();
            Shell_Project.procList.add(ps);
        }
        else{
            boolean empty = true;
            for(Thread t : procList){
                if(t.isAlive()){
                    System.out.println("Name : "+t.getName()+" - Pid : "+t.getId());
                    empty = false;
                }
            }
            if(empty){
                System.out.println("Aucune commande en cours d'execution");
            }
        }
    }
    
    /**
     * Affiche le répertoire courant
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_pwd(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread pwd = new Thread(){
                @Override
                public void run() {
                    execute_commande_pwd(argv);
                }
            };
            pwd.setName("pwd");
            pwd.start();
            Shell_Project.procList.add(pwd);
        }
        else{
            String pwd = System.getProperty("user.dir");
            System.out.println(pwd);
        }
    }
    
    /**
     * Change le dossier courant
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_cd(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread cd = new Thread(){
                @Override
                public void run() {
                    execute_commande_cd(argv);
                }
            };
            cd.setName("cd");
            cd.start();
            Shell_Project.procList.add(cd);
        }
        else{
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
    }
    
    /**
     * Affiche la date en fonction du format donné
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_date(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread date = new Thread(){
                @Override
                public void run() {
                    execute_commande_date(argv);
                }
            };
            date.setName("date");
            date.start();
            Shell_Project.procList.add(date);
        }
        else{
            Date now = new Date();
            SimpleDateFormat formater;
            String dateFormat;

            // Gestion du format par defaut
            if(argv.size() > 1)
                dateFormat = argv.get(1);
            else
                dateFormat = "+%Y-%m-%d";

            // Test de l'argument
            Pattern motif = Pattern.compile("[+](%[dHmMY]-?)*");
            Matcher m = motif.matcher(dateFormat);

            if(m.matches()){
                // Suppression des caractères inutiles
                dateFormat = dateFormat.substring(1);
                dateFormat.replace("-","");

                motif = Pattern.compile("%[dHmMY]");
                m = motif.matcher(dateFormat);

                StringBuffer sb = new StringBuffer();
                while(m.find()){
                    String token = m.group();
                    switch(token){
                        case "%Y":
                            m.appendReplacement(sb, "yy");
                            break;
                        case "%m":
                            m.appendReplacement(sb, "MM");
                            break;
                        case "%d":
                            m.appendReplacement(sb, "dd");
                            break;
                        case "%H":
                            m.appendReplacement(sb, "H");
                            break;
                        case "%M":
                            m.appendReplacement(sb, "m");
                            break;
                        default:
                            break;
                    }
                }
                m.appendTail(sb);

                dateFormat = sb.toString();
                formater = new SimpleDateFormat(dateFormat);
                System.out.println(formater.format(now));
            }
            else{
                System.out.println("Usage: date [<format>=+%Y-%m-%d-%H-%M]");
            }
        }
    }
    
    /**
     * Cherche et affiche tout les fichiers dont le nom est matché avec la regex
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_find(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread find = new Thread(){
                @Override
                public void run() {
                    execute_commande_find(argv);
                }
            };
            find.setName("find");
            find.start();
            Shell_Project.procList.add(find);
        }
        else{
            if((argv.size() == 4) && (argv.get(2).equals("-name") || argv.get(2).equals("-iname"))){

                File currentDir = new File(System.getProperty("user.dir"));
                File newDir = new File(argv.get(1));

                Matcher m;
                Pattern regex;

                if(argv.get(2).equals("-iname"))
                    regex = Pattern.compile(argv.get(3), Pattern.CASE_INSENSITIVE);
                else
                    regex = Pattern.compile(argv.get(3));

                if(newDir.getAbsoluteFile().isDirectory()){
                    // On se déplace dans le dossier
                    System.setProperty("user.dir", newDir.getAbsoluteFile().toString());
                    String enfants[] = newDir.list();

                    for(String fils: enfants){
                        m = regex.matcher(fils);
                        if(m.matches())
                            System.out.println(fils);
                    }

                    // Une fois fini on revient au dossier parent
                    System.setProperty("user.dir", currentDir.getAbsoluteFile().toString());
                }
                else{
                    System.out.println(newDir.toString() + " n'est pas un dossier.");
                }
            }
            else{
                System.out.println("Usage: find <chemin> < -name | -iname > <expr. reg.>");
            }
        }
    }
    
    /**
     * Affiche les lignes qui matches la regex, stdin ou bien dans des fichiers
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_grep(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread grep = new Thread(){
                @Override
                public void run() {
                    execute_commande_grep(argv);
                }
            };
            grep.setName("grep");
            grep.start();
            Shell_Project.procList.add(grep);
        }
        else{
            ArrayList<File> fichiers = new ArrayList<File>();
            Scanner sc = new Scanner(System.in);
            Pattern motif;
            Matcher m;

            if(argv.size() == 2){
                motif = Pattern.compile(argv.get(1));
                String str;
                while(true){
                    try{
                        str = sc.nextLine();
                        m = motif.matcher(str);
                        if(m.matches()){
                            System.out.println(str);
                        }
                    }catch (NoSuchElementException e){ // Exception levé lors de Ctrl+D
                        break;
                    }
                }
            }
            else if(argv.size() > 2){
                File file;
                motif = Pattern.compile(argv.get(1));

                // Récupération des fichiers existants
                for(int i=2; i<argv.size(); i++){

                    file = new File(argv.get(i));
                    if(file.exists())
                        fichiers.add(file);
                    else
                        System.out.println("Le fichier " + argv.get(i) + " n'existe pas.");
                }

                // Lecture des fichiers
                for(int i=0; i<fichiers.size(); i++){
                    try{
                        // Ouverture du flux
                        BufferedReader in = new BufferedReader(new FileReader(fichiers.get(i)));

                        String ligne;
                        while (in.ready()){
                            // On matche la ligne
                            ligne = in.readLine();
                            m = motif.matcher(ligne);
                            if(m.matches()){
                                System.out.println(ligne);
                            }
                        }

                        in.close();
                    }
                    catch (Exception e){
                        System.out.println(e.toString());
                    }
                }
            }
            else{
                System.out.println("grep <expr. reg.> [ <fich. 1> [ <fich.2> [ ... ] ] ]");
            }
        }
    }
    
    /**
     * Permet de remplacer une chaine par une autre dans un fichier ou bien sur stdin
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_sed(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);
            
            Thread sed = new Thread(){
                @Override
                public void run() {
                    execute_commande_sed(argv);
                }
            };
            sed.setName("sed");
            sed.start();
            Shell_Project.procList.add(sed);
        }
        else{
            Scanner sc = new Scanner(System.in);
            File file, fileOut;
            Pattern motif;
            Matcher m;

            if(!(argv.size() >= 2))
                System.out.println("sed <format> [<fichier>]");
            else{
                motif = Pattern.compile("^s(?<sep>[/:|])(?<ch1>[^/:|]*)\\k<sep>(?<ch2>[^/:|]*)\\k<sep>(?<ch3>[^/:|]*)$");
                m = motif.matcher(argv.get(1));

                if(!m.matches())
                    System.out.println("<format> = s<separator><regexp1><separator><chaine1><separator><chaine2>");
                else{
                    // Entrée standard
                    if(argv.size() == 2){
                        String str;
                        while(true){
                            try{
                                str = sc.nextLine();
                                if(m.group("ch3").equals("g")){
                                    str = str.replaceAll(m.group("ch1"), m.group("ch2"));
                                }
                                else{
                                    str = str.replaceFirst(m.group("ch1"), m.group("ch2"));
                                }
                                System.out.println(str);
                            }catch (NoSuchElementException e){ // Exception levé lors de Ctrl+D
                                break;
                            }
                        }
                    }
                    // Fichier
                    else if(argv.size() == 3){
                        file = new File(argv.get(2));
                        fileOut = new File(argv.get(2)+"bis");

                        if(file.exists()){ // Si le fichier existe
                            if(!file.isDirectory()){ // Que ce n'est pas un dossier
                                try{
                                    // Ouverture des flux de lecture / ecriture
                                    BufferedReader in = new BufferedReader(new FileReader(file));
                                    BufferedWriter out = new BufferedWriter(new FileWriter(fileOut));

                                    String ligne;
                                    while (in.ready()){
                                        // On matche la ligne
                                        ligne = in.readLine();
                                        if(m.group("ch3").equals("g")){
                                            ligne = ligne.replaceAll(m.group("ch1"), m.group("ch2"));
                                        }
                                        else{
                                            ligne = ligne.replaceFirst(m.group("ch1"), m.group("ch2"));
                                        }
                                        // Ecriture
                                        out.write(ligne);
                                        out.newLine();
                                    }
                                    out.close();
                                    in.close();

                                    // On renome le fichier
                                    fileOut.createNewFile();
                                    fileOut.renameTo(file);
                                }
                                catch (Exception e){
                                    System.out.println(e.toString());
                                }
                            }
                            else
                                System.out.println(argv.get(2) + " est un dossier.");
                        }
                        else
                            System.out.println("Le fichier " + argv.get(2) + " n'existe pas.");
                    }
                }
            }
        }
    }
    
    /**
     * Permet d'afficher les entiers plus petits nombres entiers dans l'ordre et à raison d'un par seconde
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_compteJusqua(ArrayList<String> argv){
        if(argv.get(argv.size()-1).equals("&")){
            argv.remove(argv.size()-1);

            Thread compteJusqua = new Thread(){
                @Override
                public void run() {
                    execute_commande_compteJusqua(argv);
                }
            };

            compteJusqua.setName("compteJusqua");
            compteJusqua.start();
            Shell_Project.procList.add(compteJusqua);
        }
        else{
            if(argv.size()!=2 && argv.size()!=3){
                System.out.println("compteJusqua <entier> [<format>=%d\\n]");
            }
            else{
                System.out.println("PID = "+Thread.currentThread().getId());
                String format = (argv.size()==3)?argv.get(2):"%d\n";
                int n = Integer.parseInt(argv.get(1));
                for(int i=0; i<=n; i++){
                    try {
                        System.out.print(String.format(format, i));
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Builtin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    /**
     * Permet d'interrompre le processus correspondant au pid passé en argument
     * @param argv Arguments de la fonction
     */
    public static void execute_commande_kill(ArrayList<String> argv, LinkedList<Thread> procList){
        if(argv.size() != 2){
            System.out.println("kill <pid>");
        }
        else{
            Thread kill = new Thread(){
                @Override
                public void run() {  
                    for(Thread t : procList){
                        if(t.getId()==Integer.parseInt(argv.get(1))){
                            t.stop();
                            procList.remove(t);
                        }
                    }
                }
            };

            System.out.println("Execution de kill "+argv.get(1));
            kill.start();
        }      
    }
}