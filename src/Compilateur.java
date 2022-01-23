import java.io.File;
import java.io.FileNotFoundException;

public class Compilateur {

    static final int LONG_MAX_IDENT = 20;
    static final int LONG_MAX_CHAINE = 50;
    static final int NB_MOTS_RESERVES = 7;

    static File source;
    static char carlu;
    static int nombre;
    static String chaine;
    static int num_ligne = 0;
    static String[] table_mots_reserves = new String[NB_MOTS_RESERVES];

    AnalyseurLexical analyseurLexical = new AnalyseurLexical();

    public Compilateur(String path){
        source = new File(path);
    }


    public static void main(String args[]) throws FileNotFoundException {

        String path = "";

        Compilateur compilateur = new Compilateur(path);
        compilateur.analyseurLexical.lire_car();

    }
}
