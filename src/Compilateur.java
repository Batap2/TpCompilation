import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    static T_UNILEX UNILEX; //la dernière unité lexicale reconnue


    static AnalyseurLexical analyseurLexical = new AnalyseurLexical();

    static public TableIdentificateur tableIdentificateur = new TableIdentificateur();

    public TableIdentificateur getTableIdentificateur(){
        return tableIdentificateur;
    }


    public Compilateur(String path){
        source = new File(path);
    }

    public void initialiser() throws FileNotFoundException {
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

        String path = "test.txt";

        Compilateur compilateur = new Compilateur(path);
        compilateur.initialiser();
        //compilateur.printTableMotsReserves();


        compilateur.analyseurLexical.boucle();

        compilateur.terminer();
        compilateur.getTableIdentificateur().affiche_table_ident();

    }
}
