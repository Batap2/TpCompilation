public class GestionErreur {

    static String MESSAGE_ERREUR;

    static final String[] tableauErreurs = {
            "Erreur inconnu",
            "Fin de fichier atteinte",
            "Erreur lexicale",
            "Erreur syntaxique",
            "Erreur sémantique"
    };

    public static void erreur(int numErr){
        if(numErr >= 1 && numErr <= tableauErreurs.length){
            System.out.println(tableauErreurs[numErr] + " ligne : " + Compilateur.num_ligne + " "+MESSAGE_ERREUR);
        } else {
            System.out.println(tableauErreurs[0]);
        }

        System.exit(0);
    }

    public static void erreur(int numErr, String message){
        GestionErreur.MESSAGE_ERREUR = message;
        GestionErreur.erreur(numErr);
    }


}
