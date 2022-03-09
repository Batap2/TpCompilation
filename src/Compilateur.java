import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Compilateur {

    static final int LONG_MAX_IDENT = 20;
    static final int LONG_MAX_CHAINE = 50;
    static final int NB_MOTS_RESERVES = 7;

    public enum T_UNILEX{ motcle, ident, ent, ch, virg,
                          ptvirg, point, deuxpts, parouv,
                          parfer, inf, sup, eg, plus, moins,
                          mult, divi, infe, supe, diff, aff };

    static File source;     // fichier source
    static char carlu;      // le dernier caractère lu
    static int nombre;      // le dernier nombre lu
    static String chaine;   // la dernière chaine de caractères lue
    static int num_ligne;   // num de la ligne actuelle
    static String[] table_mots_reserves = new String[NB_MOTS_RESERVES];
    public static T_UNILEX UNILEX; //la dernière unité lexicale reconnue
    public static int NB_CONST_CHAINE;
    public static ArrayList<String> VAL_DE_CONST_CHAINE = new ArrayList<>();
    public static int DERNIERE_ADRESSE_VAR_GLOB;

    static AnalyseurLexical analyseurLexical = new AnalyseurLexical();
    static AnalyseurSyntaxique analyseurSyntaxique = new AnalyseurSyntaxique();

    static public TableIdentificateur tableIdentificateur = new TableIdentificateur();

    static public TableIdentificateur getTableIdentificateur(){
        return tableIdentificateur;
    }


    public Compilateur(String path){
        source = new File(path);
    }

    public void initialiser() throws FileNotFoundException {
        NB_CONST_CHAINE = 0;
        DERNIERE_ADRESSE_VAR_GLOB = -1;
        analyseurLexical.initialiser();
    }

    public void terminer() throws IOException {

        analyseurLexical.terminer();
    }

    public void printTableMotsReserves(){
        for(int i = 0; i < table_mots_reserves.length; i++){
            System.out.println(table_mots_reserves[i]);
        }
    }


    public static void main(String args[]) throws IOException {

        String path = "testSemantique";

        Compilateur compilateur = new Compilateur(path);
        compilateur.initialiser();
        //compilateur.printTableMotsReserves();

        compilateur.analyseurSyntaxique.anasynt();

        //compilateur.analyseurLexical.boucleTest();


        compilateur.terminer();

    }
}
