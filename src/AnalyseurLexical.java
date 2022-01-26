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
    final int asciilastUpperCaseLetter = 90;    // Z
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

        //TODO : Appel des fonctions non terminé

        do{
            int car = file.read();
            Compilateur.carlu = (char)car;

            // car : Retour Chariot
            if(car == asciiCarriageReturn){
                car = file.read(); // passe ce caractère, il sera automatiquement suivi d'un saut de ligne sur windows
            }

            // car : Fin de ligne
            if(car == asciiLineFeed){
                sauter_Separateur();
                Compilateur.num_ligne ++;
            }

            // car : Séparateur || Symbole de début de commentaire
            if(car == asciiSpace || car == asciiTab || car == asciiLBrakets){
                System.out.println("ouais");
                sauter_Separateur();
            }

            // car : Chiffre
            if(car >= asciiFirstNumber && car <= asciiLastNumber){
                reco_Entier();
            }

            // car : Apostrophe
            if(car == asciiApostrophe){
                reco_Chaine();
            }

            // car : Caractère majuscule ou minuscule
            if((car >= asciiFirstUpperCaseLetter && car <= asciiLastLowerCaseLetter) || (car >= asciiFirstUpperCaseLetter+9 && car <= asciiLastLowerCaseLetter+9)){
                reco_Ident_Ou_Mot_Reserve();
            }

            // car : Symbole simple { , ; . : ( ) < > = + - * / }
            if(car == asciiComa | car == asciiSemicolon | car == asciiDot |
               car == asciiColon | car == asciiLParenthesis | car == asciiRParenthesis |
               car == asciiLessThan | car == asciiGreaterThan | car == asciiEquals |
               car == asciiPlus | car == asciiHyphen | car == asciiAsterisk | car == asciiSlash){
                reco_Symb();
            }

        } while(Compilateur.carlu != asciiEOF);

        erreur(1);
    }

    public void sauter_Separateur(){

    }

    public Compilateur.T_UNILEX reco_Entier() throws IOException {
        /*
        String nombre;
        char temp = Compilateur.carlu;
        int car = file.read();
        Compilateur.carlu = (char)car;
        nombre = Character.toString(temp)+Character.toString(Compilateur.carlu);
        nombre = temp+""+Compilateur.carlu;
        car = file.read();


        while(car >= asciiFirstNumber && car <= asciiLastNumber){

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
        */

        // PUCE : j'ai fais une nouvelle version de ton reco_entier() parce que je la trouvais bof et je comprenais pas pk t'as appelé lire_car()

        StringBuilder newNombre = new StringBuilder();
        newNombre.append(Compilateur.carlu);
        int car = file.read();

        while(car >= asciiFirstNumber && car <= asciiLastNumber){
            newNombre.append((char)car);
            car = file.read();
        }

        int newNombreInt = Integer.parseInt(newNombre.toString());

        if(newNombreInt > MAXINT){
            erreur(2);
            return null;
        }

        file.seek(file.getFilePointer() - 1);
        Compilateur.nombre = newNombreInt;
        return Compilateur.T_UNILEX.ent;
    }

    public Compilateur.T_UNILEX reco_Chaine(){
        // TODO
        return Compilateur.T_UNILEX.ch;
    }

    public Compilateur.T_UNILEX reco_Ident_Ou_Mot_Reserve(){
        // TODO
        // return Compilateur.T_UNILEX.motcle;
        return Compilateur.T_UNILEX.ident;
    }

    public boolean est_Un_Mot_Reserve(){
        // TODO
        return false;
    }

    public Compilateur.T_UNILEX reco_Symb() throws IOException {
        char car = Compilateur.carlu;
        Compilateur.T_UNILEX returnUnilex = null;

        switch(car){
            case asciiSemicolon :
                returnUnilex = Compilateur.T_UNILEX.ptvirg;

            case asciiComa :
                returnUnilex = Compilateur.T_UNILEX.virg;

            case asciiDot :
                returnUnilex = Compilateur.T_UNILEX.point;

            case asciiLParenthesis :
                returnUnilex = Compilateur.T_UNILEX.parouv;

            case asciiRParenthesis :
                returnUnilex = Compilateur.T_UNILEX.parfer;

            case asciiEquals :
                returnUnilex = Compilateur.T_UNILEX.eg;

            case asciiPlus :
                returnUnilex = Compilateur.T_UNILEX.plus;

            case asciiHyphen :
                returnUnilex = Compilateur.T_UNILEX.moins;

            case asciiAsterisk :
                returnUnilex = Compilateur.T_UNILEX.mult;

            case asciiSlash :
                returnUnilex = Compilateur.T_UNILEX.divi;

            case asciiLessThan :
                int newCar1 = file.read();

                if(newCar1 == asciiEquals){
                    returnUnilex = Compilateur.T_UNILEX.infe;
                }

                if(newCar1 == asciiGreaterThan){
                    returnUnilex = Compilateur.T_UNILEX.diff;
                }

                returnUnilex = Compilateur.T_UNILEX.inf;

            case asciiGreaterThan :
                int newCar2 = file.read();

                if(newCar2 == asciiEquals){
                    returnUnilex = Compilateur.T_UNILEX.supe;
                }

                returnUnilex = Compilateur.T_UNILEX.sup;

            case asciiColon :
                int newCar3 = file.read();

                if(newCar3 == asciiEquals){
                    returnUnilex = Compilateur.T_UNILEX.aff;
                }

                returnUnilex = Compilateur.T_UNILEX.deuxpts;
                // Pas compris la remarque de fin. On doit faire un file.read avant le return ? parce que de toute façon quand
                // on rappelle lire_car on met le prochain caractère de file dans CARLU, donc bon...
        }
        return returnUnilex;
    }

    public void analex(){

    }
}