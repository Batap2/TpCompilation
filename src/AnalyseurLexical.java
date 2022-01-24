import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnalyseurLexical {

    Scanner scanner;

    public void erreur(int numErr){
        GestionErreur.erreur(numErr);
    }

    public void initialiser() throws FileNotFoundException {
        Compilateur.num_ligne = 0;
        //scanner = new Scanner(Compilateur.source);
        insere_table_mots_reserves("PROGRAMME");
        insere_table_mots_reserves("DEBUT");
        insere_table_mots_reserves("FIN");
        insere_table_mots_reserves("CONST");
        insere_table_mots_reserves("VAR");
        insere_table_mots_reserves("ECRIRE");
        insere_table_mots_reserves("LIRE");

    }

    private void insere_table_mots_reserves(String mot_reserve){

        for(int indexTable = 0; indexTable < Compilateur.NB_MOTS_RESERVES; indexTable++){

            if(Compilateur.table_mots_reserves[indexTable] == null){
                Compilateur.table_mots_reserves[indexTable] = mot_reserve;
                return;
            }

            if(mot_reserve.compareTo(Compilateur.table_mots_reserves[indexTable]) < 0){

                String temp = Compilateur.table_mots_reserves[indexTable];
                Compilateur.table_mots_reserves[indexTable] = mot_reserve;
                for(indexTable = indexTable+1; indexTable<Compilateur.NB_MOTS_RESERVES; indexTable++){
                    String temp2 = Compilateur.table_mots_reserves[indexTable];
                    Compilateur.table_mots_reserves[indexTable] = temp;
                    temp = temp2;

                }
                return;
            }
        }

    }

    public void terminer(){
        scanner.close();
    }

    public void lire_car() {

        //todo : gérer l'appel aux autres fonctions

        while (scanner.hasNext()){
            String line = scanner.nextLine();
            Compilateur.num_ligne ++;

            for(int characterIndex = 0; characterIndex <line.length(); characterIndex++){
                char character = line.charAt(characterIndex);
                Compilateur.carlu = character;

            }

        }
        erreur(1);

    }







}