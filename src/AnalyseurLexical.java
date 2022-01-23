import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnalyseurLexical {

    public void erreur(int numErr){
        GestionErreur.erreur(numErr);
    }

    public void lire_car() throws FileNotFoundException {

        //todo : gérer l'appel aux autres fonctions

        Scanner scanner = new Scanner(Compilateur.source);
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