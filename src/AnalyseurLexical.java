import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class AnalyseurLexical {

    RandomAccessFile file;

    final int asciiRetourCharriot = 13;
    final int asciiSautDeLigne = 10;
    final int asciiDebutEntier = 48;
    final int asciiFinEntier = 57;

    final int MAXINT = 32767;

    public void erreur(int numErr){
        GestionErreur.erreur(numErr);
    }

    public void initialiser() throws FileNotFoundException {
        Compilateur.num_ligne = 1;
        file = new RandomAccessFile(Compilateur.source, "r");
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


    public void terminer() throws IOException {
        file.close();
    }

    public void lire_car() throws IOException {

        //todo : gérer l'appel aux autres fonctions

        int car = file.read();
        while(car != -1){
            Compilateur.carlu = (char)car;

            System.out.println(car);

            if(car == asciiRetourCharriot){
                System.out.println("saut");
                car = file.read(); // va correspondre au asciiSautDeLigne
                Compilateur.num_ligne ++;
            }
            if(car >= asciiDebutEntier && car <= asciiFinEntier){
                reco_entier();
                return;
            }

            car = file.read();
        }

        erreur(1);

    //System.getProperty("line.separator")


    }


    public Compilateur.T_UNILEX reco_entier() throws IOException {

        String nombre;
        char temp = Compilateur.carlu;
        int car = file.read();
        Compilateur.carlu = (char)car;
        nombre = Character.toString(temp)+Character.toString(Compilateur.carlu);
        nombre = temp+""+Compilateur.carlu;
        car = file.read();


        while(car >= asciiDebutEntier && car <= asciiFinEntier){

            Compilateur.carlu = (char)car;
            nombre = nombre+""+Compilateur.carlu;
            car = file.read();

        }

        long pointer = file.getFilePointer();
        file.seek(pointer-1);


        int nombreFinal = Integer.parseInt(nombre);
        if(nombreFinal <= MAXINT){
            Compilateur.nombre = nombreFinal;
        }
        else{
            erreur(2);
            return null;
        }

        lire_car();
        
        return Compilateur.T_UNILEX.ent;

    }







}