import java.io.File;

public class Compilateur {

    static final int LONG_MAX_IDENT = 20;
    static final int LONG_MAX_CHAINE = 50;
    static final int NB_MOTS_RESERVES = 7;

    static File source;
    static char carlu;
    static int nombre;
    static String chaine;
    static int num_ligne;
    static String[] table_mots_reserves = new String[NB_MOTS_RESERVES];

    public static void main(String args[]){

        String path = "";

        AnalyseurLexical analyseurLexical = new AnalyseurLexical(path);
    }
}
