import java.io.File;
import java.util.Scanner;

public class AnalyseurLexical {

    public AnalyseurLexical(String path){
        File sourceFile = new File(path);

    }

    public void erreur(int numErr){
        GestionErreur.erreur(numErr);
    }

    public void lire_car(){

    }
}