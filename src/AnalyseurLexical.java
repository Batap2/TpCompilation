import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AnalyseurLexical {

    // Fichier source
    RandomAccessFile file;

    // Valeur max d'un entier
    final int MAXINT = 32767;

    // ASCII TABLE
    final int asciiCarriageReturn = 13;         // CR
    final int asciiLineFeed = 10;               // LF
    final int asciiEOF = '\uFFFF';              // EOF
    final int asciiFirstNumber = 48;            // 0
    final int asciiLastNumber = 57;             // 9
    final int asciiFirstUpperCaseLetter = 65;   // A
    final int asciiLastUpperCaseLetter = 90;    // Z
    final int asciiFirstLowerCaseLetter = 97;   // a
    final int asciiLastLowerCaseLetter = 122;   // z
    final int asciiLBrakets = 123;              // {
    final int asciiRBrakets = 125;              // }
    final int asciiSpace = 32;                  // SPACE
    final int asciiTab = 9;                     // TAB
    final int asciiApostrophe = 39;             // '
    final int asciiComa = 44;                   // ,
    final int asciiSemicolon = 59;              // ;
    final int asciiDot = 46;                    // .
    final int asciiColon = 58;                  // :
    final int asciiLParenthesis = 40;           // (
    final int asciiRParenthesis = 41;           // )
    final int asciiLessThan = 60;               // <
    final int asciiGreaterThan = 62;            // >
    final int asciiEquals = 61;                 // =
    final int asciiPlus = 43;                   // +
    final int asciiHyphen = 45;                 // -
    final int asciiAsterisk = 42;               // *
    final int asciiSlash = 47;                  // /
    final int asciiUnderscore = 95;             // _

    // Gère les erreurs
    public void erreur(int numErr){
        GestionErreur.erreur(numErr);
    }

    // Ajoute les mots réservés dans le tableau des mots réservés
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

    // Ajoute un mot dans le tableau des mots réservés en le classant dans l'ordre alphabéthique
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

    // Lit le prochain caractère du fichier source, si c'est la fin du fichier affiche l'erreur de fin de fichier
    // Incrémente la variable globale num-ligne si le caractère est un LineFeed
    public void lire_car() throws IOException {
        Compilateur.carlu = (char)file.read();

        if(Compilateur.carlu == asciiEOF){
            //todo : regler la double erreur fin de fichier
            erreur(1);
        }

        if(Compilateur.carlu == asciiLineFeed){
            Compilateur.num_ligne++;
        }
    }

    public void sauter_Separateur() throws IOException {

        //todo : prendre en compte commentaires imbriqués

        // le Retour Chariot (\r) n'est utilisé que sur windows pour faire un retour a la ligne,
        // il sera automatiquement suivi d'un caractère de retour à la ligne (\n)
        // donc on passe ce caractère
        while(Compilateur.carlu == asciiSpace || Compilateur.carlu == asciiCarriageReturn || Compilateur.carlu == asciiLineFeed || Compilateur.carlu == asciiTab){
            lire_car();
        }

        if(Compilateur.carlu == asciiLBrakets){
            while(Compilateur.carlu != asciiRBrakets){
                lire_car();
            }
            return;
        }
    }

    public Compilateur.T_UNILEX reco_Entier() throws IOException {

        StringBuilder newNombre = new StringBuilder();
        newNombre.append(Compilateur.carlu);
        lire_car();

        while(Compilateur.carlu >= asciiFirstNumber && Compilateur.carlu <= asciiLastNumber){
            newNombre.append((char)Compilateur.carlu);
            lire_car();
        }

        returnToPreviousChar();

        if(Integer.parseInt(newNombre.toString()) > MAXINT){
            erreur(2);
            return null;
        }

        Compilateur.nombre = Integer.parseInt(newNombre.toString());
        return Compilateur.T_UNILEX.ent;
    }

    public Compilateur.T_UNILEX reco_Chaine() throws IOException {

        lire_car();

        StringBuilder stringBuilder = new StringBuilder();

        while (true){
            if(Compilateur.carlu == asciiEOF){
                GestionErreur.erreur(2);
                return null;
            }

            if(Compilateur.carlu == asciiApostrophe){
                lire_car();
                if(Compilateur.carlu != asciiApostrophe){
                    break;
                }
            }
            stringBuilder.append(Compilateur.carlu);
            lire_car();
        }

        if(stringBuilder.toString().length() > Compilateur.LONG_MAX_CHAINE){
            GestionErreur.erreur(2);
            return null;
        }

        returnToPreviousChar();
        Compilateur.chaine = stringBuilder.toString();
        return Compilateur.T_UNILEX.ch;
    }

    public Compilateur.T_UNILEX reco_Ident_Ou_Mot_Reserve() throws IOException {

        int length = 0;
        StringBuilder str = new StringBuilder();

        while ( (Character.isLetter(Compilateur.carlu) || Character.isDigit(Compilateur.carlu) || Compilateur.carlu == asciiUnderscore) &&
                (length <= Compilateur.LONG_MAX_IDENT) ){
            str.append(Compilateur.carlu);
            length++;
            lire_car();
        }

        Compilateur.chaine = str.toString().toUpperCase();
        returnToPreviousChar();

        if(est_Un_Mot_Reserve()){
            return Compilateur.T_UNILEX.motcle;
        }
        return Compilateur.T_UNILEX.ident;
    }

    private boolean est_Un_Mot_Reserve(){
        for(String motRes : Compilateur.table_mots_reserves){
            if(Compilateur.chaine.equals(motRes)) return true;
        }
        return false;
    }

    public Compilateur.T_UNILEX reco_Symb() throws IOException {

        Compilateur.T_UNILEX returnUnilex;

        switch(Compilateur.carlu){
            case asciiSemicolon :
                returnUnilex = Compilateur.T_UNILEX.ptvirg;
                break;

            case asciiDot :
                returnUnilex = Compilateur.T_UNILEX.point;
                break;

            case asciiEquals :
                returnUnilex = Compilateur.T_UNILEX.eg;
                break;

            case asciiPlus :
                returnUnilex = Compilateur.T_UNILEX.plus;
                break;

            case asciiHyphen :
                returnUnilex = Compilateur.T_UNILEX.moins;
                break;

            case asciiAsterisk :
                returnUnilex = Compilateur.T_UNILEX.mult;
                break;

            case asciiSlash :
                returnUnilex = Compilateur.T_UNILEX.divi;
                break;

            case asciiLParenthesis :
                returnUnilex = Compilateur.T_UNILEX.parouv;
                break;

            case asciiRParenthesis :
                returnUnilex = Compilateur.T_UNILEX.parfer;
                break;

            case asciiComa :
                returnUnilex = Compilateur.T_UNILEX.virg;
                break;

            case asciiLessThan :
                lire_car();

                if(Compilateur.carlu == asciiEquals){
                    returnUnilex = Compilateur.T_UNILEX.infe;
                    break;
                }

                if(Compilateur.carlu == asciiGreaterThan){
                    returnUnilex = Compilateur.T_UNILEX.diff;
                    break;
                }

                returnToPreviousChar();
                returnUnilex = Compilateur.T_UNILEX.inf;
                break;

            case asciiGreaterThan :
                lire_car();

                if(Compilateur.carlu == asciiEquals){
                    returnUnilex = Compilateur.T_UNILEX.supe;
                    break;
                }

                returnToPreviousChar();
                returnUnilex = Compilateur.T_UNILEX.sup;
                break;

            case asciiColon :
                lire_car();

                if(Compilateur.carlu == asciiEquals){
                    returnUnilex = Compilateur.T_UNILEX.aff;
                    break;
                }

                returnToPreviousChar();
                returnUnilex = Compilateur.T_UNILEX.deuxpts;
                break;

            default :
                returnUnilex = null;
        }
        //lire_car(); // pas compris pk il nous dit de faire ça
        return returnUnilex;
    }

    public void analex() throws IOException {
        do{
            lire_car();
            Compilateur.T_UNILEX unilexLue = null;

            // car : Séparateur || Symbole de début de commentaire
            if(Compilateur.carlu == asciiSpace || Compilateur.carlu == asciiTab ||
                    Compilateur.carlu == asciiLineFeed || Compilateur.carlu == asciiCarriageReturn || Compilateur.carlu == asciiLBrakets){
                sauter_Separateur();
            }

            // car : Chiffre
            if(Character.isDigit(Compilateur.carlu)){
                unilexLue = reco_Entier();
                System.out.println(unilexLue);
                continue;
            }

            // car : Apostrophe
            if(Compilateur.carlu == asciiApostrophe){
                unilexLue = reco_Chaine();
                System.out.println(unilexLue);
                continue;
            }

            // car : Caractère majuscule ou minuscule
            if(Character.isLetter(Compilateur.carlu)){
                unilexLue = reco_Ident_Ou_Mot_Reserve();
                System.out.println(unilexLue);
                if(unilexLue == Compilateur.T_UNILEX.ident){
                    if(Compilateur.tableIdentificateur.chercher(Compilateur.chaine) == -1){
                        Compilateur.tableIdentificateur.inserer(Compilateur.chaine, null);
                    }
                }

                continue;
            }

            // car : Symbole simple { , ; . : ( ) < > = + - * / }
            if(isSymboleSimple()){
                unilexLue = reco_Symb();
                System.out.println(unilexLue);
                continue;
            }
        } while(Compilateur.carlu != asciiEOF);
    }

    // Retourne au caractère précédent. Si le caractère précédent était un LF on doit décrémenter num_ligne
    private void returnToPreviousChar() throws IOException {

        if(file.getFilePointer() > 0 && file.getFilePointer() <= file.length()){

            if(Compilateur.carlu == asciiLineFeed){
                Compilateur.num_ligne--;
            }

            do{
                // Si c'est la fin du fichier il faut seek que de 1, jsp pk
                if(Compilateur.carlu == asciiEOF){
                    file.seek(file.getFilePointer() - 1);
                } else {
                    file.seek(file.getFilePointer() - 2);
                }
                Compilateur.carlu = (char)file.read();
            } while (Compilateur.carlu == asciiCarriageReturn);
        }
    }

    // Dit si oui ou non CARLU est un symbole simple
    private boolean isSymboleSimple(){
        char car = Compilateur.carlu;
        return car == asciiComa | car == asciiSemicolon | car == asciiDot |
                car == asciiColon | car == asciiLParenthesis | car == asciiRParenthesis |
                car == asciiLessThan | car == asciiGreaterThan | car == asciiEquals |
                car == asciiPlus | car == asciiHyphen | car == asciiAsterisk | car == asciiSlash;
    }
}