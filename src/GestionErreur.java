public class GestionErreur {

    static final String[] tableauErreurs = {
            "Erreur inconnu",
            "Fin de fichier atteinte",
            "Erreur lexicale",
            "Erreur syntaxique",
            "Erreur sémantique"
    };

    public static void erreur(int numErr){
        if(numErr >= 1 && numErr <= tableauErreurs.length){
            System.out.println(tableauErreurs[numErr] + " ligne : " + Compilateur.num_ligne);
        } else {
            System.out.println(tableauErreurs[0]);
        }
        //System.exit(0);
    }
}
