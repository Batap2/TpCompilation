import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Compilateur {

    static final int LONG_MAX_IDENT = 20;
    static final int LONG_MAX_CHAINE = 50;
    static final int NB_MOTS_RESERVES = 12;

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

    static Memoire memoire;
    private static final String SEPARATOR = "\n";

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

    static public void creer_fichier_code(String nomSource) throws IOException {
        memoire.print();

        FileWriter file = new FileWriter(nomSource+".COD");
        int premiereAdresseLibre = memoire.getNbMotsReservesVariableGlobales();
        file.append(premiereAdresseLibre+" mot(s) réservé(s) pour les variables globales"+SEPARATOR);
        int adresse = premiereAdresseLibre;
        while(adresse != memoire.getCO()){

            if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.ADDI.ordinal()){
                file.append("ADDI"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.SOUS.ordinal()){
                file.append("SOUS"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.MULT.ordinal()){
                file.append("MULT"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.DIVI.ordinal()){
                file.append("DIVI"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.MOIN.ordinal()){
                file.append("MOIN"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.AFFE.ordinal()){
                file.append("AFFE"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.LIRE.ordinal()){
                file.append("LIRE"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.ECRL.ordinal()){
                file.append("ECRL"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.ECRE.ordinal()){
                file.append("ECRE"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.ECRC.ordinal()){
                file.append("ECRC '");
                adresse++;
                while(memoire.getContenuP_CODE(adresse) != Memoire.MOT_MEMOIRE.FINC.ordinal()){
                    file.append(Character.toString(memoire.getContenuP_CODE(adresse)));
                    adresse++;
                }
                file.append("' FINC"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.EMPI.ordinal()){
                file.append("EMPI ");
                adresse++;
                file.append(Integer.toString(memoire.getContenuP_CODE(adresse))+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.CONT.ordinal()){
                file.append("CONT"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.STOP.ordinal()){
                file.append("STOP"+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.ALLE.ordinal()){
                file.append("ALLE ");
                adresse++;
                file.append(memoire.getContenuP_CODE(adresse)+SEPARATOR);
            }
            else if(memoire.getContenuP_CODE(adresse) == Memoire.MOT_MEMOIRE.ALSN.ordinal()){
                file.append("ALSN ");
                adresse++;
                file.append(memoire.getContenuP_CODE(adresse)+SEPARATOR);
            }
            else{
                GestionErreur.erreur(0);
            }
            adresse++;
        }
        file.close();
    }

    private void traitementInstructionArithmetique(String operation){
        int value = memoire.getContenuPilex(memoire.getSom_pilex()-1);
        if(operation.equals("+")){
            value = value +memoire.getContenuPilex(memoire.getSom_pilex());
        }
        if(operation.equals("-")){
            value = value - memoire.getContenuPilex(memoire.getSom_pilex());
        }
        if(operation.equals("*")){
            value = value * memoire.getContenuPilex(memoire.getSom_pilex());
        }
        if(operation.equals("/")){
            value = value / memoire.getContenuPilex(memoire.getSom_pilex());
        }
        memoire.setPilex(memoire.getSom_pilex()-1, value);
        memoire.setSom_pilex(-1);
    }

    public void interpreter(){

        int CO = memoire.getNbMotsReservesVariableGlobales();
        while(memoire.getContenuP_CODE(CO) != Memoire.MOT_MEMOIRE.STOP.ordinal()){

            if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.ADDI.ordinal()){
                traitementInstructionArithmetique("+");
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.SOUS.ordinal()){
                traitementInstructionArithmetique("-");
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.MULT.ordinal()){
                traitementInstructionArithmetique("*");
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.DIVI.ordinal()){
                if(memoire.getContenuPilex(memoire.getSom_pilex()) == 0){
                    GestionErreur.erreur(6, "division par zéro");
                }
                else{
                    traitementInstructionArithmetique("/");
                    CO++;
                }
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.MOIN.ordinal()){
                memoire.setPilex(memoire.getSom_pilex(), - memoire.getContenuPilex(memoire.getSom_pilex()));
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.AFFE.ordinal()){
                memoire.setMemvar(memoire.getContenuPilex(memoire.getSom_pilex()-1), memoire.getContenuPilex(memoire.getSom_pilex()));
                memoire.setSom_pilex(-2);
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.LIRE.ordinal()){
                Scanner scanner = new Scanner(System.in);
                memoire.setMemvar(memoire.getContenuPilex(memoire.getSom_pilex()), scanner.nextInt());
                scanner.close();
                memoire.setSom_pilex(-1);
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.ECRL.ordinal()){
                System.out.print("\n");
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.ECRE.ordinal()){
                System.out.print(memoire.getContenuPilex(memoire.getSom_pilex()));
                memoire.setSom_pilex(-1);
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.ECRC.ordinal()){
                CO++;
                String ch = Character.toString(memoire.getContenuP_CODE(CO));
                while(memoire.getContenuP_CODE(CO) != Memoire.MOT_MEMOIRE.FINC.ordinal()){
                    CO++;
                    ch = ch+Character.toString(memoire.getContenuP_CODE(CO));
                }
                System.out.print(ch);
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.EMPI.ordinal()){
                memoire.setSom_pilex(1);
                memoire.setPilex(memoire.getSom_pilex(), memoire.getContenuP_CODE(CO+1));
                CO = CO+2;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.CONT.ordinal()){
                memoire.setPilex(memoire.getSom_pilex(), memoire.getMemvar(memoire.getContenuPilex(memoire.getSom_pilex())));
                CO++;
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.ALSN.ordinal()){
                if(memoire.getContenuPilex(memoire.getSom_pilex()) == 0){
                    CO = memoire.getContenuP_CODE(CO+1);
                    //memoire.setCO(memoire.getContenuP_CODE(CO+1) - CO);
                }
                else{
                    //memoire.setCO(1);
                    CO = CO+2;
                }
                memoire.setSom_pilex(-1);
            }
            else if(memoire.getContenuP_CODE(CO) == Memoire.MOT_MEMOIRE.ALLE.ordinal()){
                CO = memoire.getContenuP_CODE(CO+1);
            }
            else{
                System.out.println("interpreteur");
                GestionErreur.erreur(0);
            }
        }

    }


    public static void main(String args[]) throws IOException {

        String path = "testSiSinon";

        Compilateur compilateur = new Compilateur(path);
        compilateur.initialiser();
        //compilateur.printTableMotsReserves();

        compilateur.analyseurSyntaxique.anasynt();

        //compilateur.analyseurLexical.boucleTest();


        compilateur.terminer();

        creer_fichier_code(path);
        compilateur.interpreter();

    }
}
