import java.io.IOException;

public class AnalyseurSyntaxique {

    //private AnalyseurLexical analyseurLexical = new AnalyseurLexical();
    private AnalyseurSemantique analyseurSemantique = new AnalyseurSemantique();
    //todo : enlever memoire ici
    private Memoire memoire;
    private String messageErreur;

    public void anasynt() throws IOException {
        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
        if(prog()){
            System.out.println("Le programme source est syntaxiquement correct");
            Compilateur.getTableIdentificateur().affiche_table_ident();
        }
        else{
            GestionErreur.erreur(3, messageErreur);
        }
    }


    // prog -> 'programme' 'ident' ';' [decl_const][decl_var] bloc '.'
    public boolean prog() throws IOException {
        //System.out.println(Compilateur.UNILEX);
        if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("PROGRAMME")){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){
                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                if(Compilateur.UNILEX == Compilateur.T_UNILEX.ptvirg){
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    decl_const();
                    decl_var();
                    if(bloc()){
                        if(Compilateur.UNILEX == Compilateur.T_UNILEX.point){
                            return true;
                        }
                        else{
                            messageErreur = ". attendu";
                            return false;
                        }
                    }
                    else{
                        return false; //bloc incorrect
                    }
                }
                else{
                    messageErreur = "; attendu";
                    return false; // ; attendu
                }
            }
            else{
                messageErreur = "ident attendu";
                return false; // ident attendu
            }
        }
        else{
            messageErreur = "mot clé PROGRAMME attendu";
            return false; // mot clé programme attendu
        }

    }


    // decl_const -> 'const' 'ident' '=' ('ent' | 'ch') { ',' 'ident' '=' ('ent' | 'ch')}';'
    public boolean decl_const() throws IOException {
        boolean fin, erreur;
        String nom_constante;

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("CONST")){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){

                nom_constante = Compilateur.chaine;

                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                if(Compilateur.UNILEX == Compilateur.T_UNILEX.eg){
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    if(Compilateur.UNILEX == Compilateur.T_UNILEX.ent || Compilateur.UNILEX == Compilateur.T_UNILEX.ch){

                        if(analyseurSemantique.definir_constante(nom_constante, Compilateur.UNILEX)){
                            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                            fin = false;
                            erreur = false;
                            while(!fin){
                                if(Compilateur.UNILEX == Compilateur.T_UNILEX.virg){
                                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                                    if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){

                                        nom_constante = Compilateur.chaine;

                                        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                                        if(Compilateur.UNILEX == Compilateur.T_UNILEX.eg){
                                            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                                            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ent || Compilateur.UNILEX == Compilateur.T_UNILEX.ch){

                                                if(analyseurSemantique.definir_constante(nom_constante, Compilateur.UNILEX)){
                                                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                                                    fin = false;
                                                }
                                                else{
                                                    fin = true;
                                                    GestionErreur.erreur(4, "identificateur déjà déclaré");
                                                }

                                            }
                                            else{
                                                fin = true;
                                                erreur = true; //entier ou chaine de caractères attendu
                                                messageErreur = "entier ou chaine de caractères attendu";
                                            }
                                        }
                                        else{
                                            fin = true;
                                            erreur = true; //= attendu
                                            messageErreur = "= attendu";
                                        }
                                    }
                                    else{
                                        fin = true;
                                        erreur = true; //identifiant attendu
                                        messageErreur = "identifiant attendu";
                                    }
                                }
                                else{
                                    fin = true;
                                }
                            }
                            if(erreur){
                                return false;
                            }
                            else if(Compilateur.UNILEX == Compilateur.T_UNILEX.ptvirg){
                                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                                return true;
                            }
                            else{
                                messageErreur = "; attendu";
                                return false; // ; attendu
                            }
                        }
                        else{
                            GestionErreur.erreur(4, "identifiant déjà déclaré");
                            return false; //erreur sémantique dans la déclaration des constantes, ident déjà déclaré
                        }

                    }
                    else{
                        messageErreur = "entier ou chaine de caractères attendu attendu";
                        return false; //entier ou chaine de caracteres attendu
                    }
                }
                else{
                    messageErreur = "= attendu";
                    return false; // = attendu
                }
            }
            else{
                messageErreur = "identifiant attendu";
                return false; //identifiant attendu
            }
        }
        else{
            messageErreur = "mot clé CONST attendu";
            return false; // mot clé const attendu
        }

    }

    // 'var' 'ident' {',' 'ident'}';'
    public boolean decl_var() throws IOException {
        boolean erreur, fin;
        String nom_variable;

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("VAR")){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){

                nom_variable = Compilateur.chaine;
                if(analyseurSemantique.definir_variable(nom_variable)){
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    erreur = false;
                    fin = false;
                    while(!fin){
                        if(Compilateur.UNILEX == Compilateur.T_UNILEX.virg){
                            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){

                                nom_variable = Compilateur.chaine;
                                if(analyseurSemantique.definir_variable(nom_variable)){
                                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                                    fin = false;
                                }
                                else{
                                    fin = true;
                                    GestionErreur.erreur(4, "identifiant déjà déclaré");
                                }

                            }
                            else{
                                erreur = true;
                                fin = true;
                            }
                        }
                        else{
                            fin = true;
                        }
                    }
                    if(erreur){
                        messageErreur = "identifiant attendu";
                        return false; //identifiant attendu
                    }
                    else if(Compilateur.UNILEX == Compilateur.T_UNILEX.ptvirg){
                        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                        return true;
                    }
                    else{
                        messageErreur = "; attendu";
                        return false; // ; attendu
                    }
                }
                else{
                    GestionErreur.erreur(4, "identifiant déjà déclaré");
                    return false; //erreur sémantique dans la déclaration des variables : identificateur déjà déclaré
                }

            }
            else{
                messageErreur = "identifiant attendu";
                return false; //identifiant attendu
            }
        }
        else{
            messageErreur = "mot clé VAR attendu";
            return false; // mot clé var attendu
        }

    }

    //'debut' instruction {';' instruction } 'fin'
    public boolean bloc() throws IOException {
        boolean fin, erreur;

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("DEBUT")){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();

            memoire = new Memoire();

            if(instruction()){
                fin = false;
                erreur = false;
                while(!fin){
                    if(Compilateur.UNILEX == Compilateur.T_UNILEX.ptvirg){
                        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                        if(!instruction()){
                            erreur = true;
                            fin = true;
                        }
                    }
                    else{
                        fin = true;
                    }
                }
                if(erreur){
                    return false; //instruction invalide
                }
                else if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("FIN")){
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    return true;
                }
                else{
                    messageErreur = "mot clé FIN attendu";
                    return false; //mot clé fin attendu
                }
            }
            else{
                return false; //instruction invalide
            }
        }
        else{
            messageErreur = "mot clé DEBUT attendu";
            return false; //mot clé debut attendu
        }

    }

    // instruction -> affectation | lecture | ecriture | bloc
    public boolean instruction() throws IOException {

        if(affectation() || lecture() || ecriture() || bloc()){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isDeclaredVariable(){
        int indice = Compilateur.tableIdentificateur.chercher(Compilateur.chaine);
        if(indice == -1){
            GestionErreur.erreur(4, "identifiant pas déclaré");
            return false; //erreur semantique : ident pas déclaré
        }
        else{
            if(Compilateur.tableIdentificateur.enregIdents.get(indice).getGenre() != EnregIdent.GENRE.variable){
                GestionErreur.erreur(4, "l'identifiant n'est pas une variable");
                return false; //pas une variable
            }
        }
        return true;
    }

    // affectation -> 'ident' ':=' exp
    public boolean affectation() throws IOException {
        if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){

            if(!isDeclaredVariable()){
                return false;
            }

            Compilateur.analyseurLexical.GENCODE_empilement();

            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.aff){
                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                boolean retour = exp();

                Compilateur.analyseurLexical.GENCODE_affectation();

                return retour;
            }
            else{
                messageErreur = ":= attendu";
                return false; //affectation attendue
            }
        }
        else{
            messageErreur = "identificateur attendu";
            return false; //identificateur attendu
        }

    }

    // lecture -> 'lire' '(' 'ident' { ',' 'ident' } ')'
    public boolean lecture() throws IOException {
        boolean fin, erreur;

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("LIRE")){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.parouv){
                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){

                    if(!isDeclaredVariable()){
                        return false;
                    }

                    Compilateur.analyseurLexical.GENCODE_empilement();
                    Compilateur.analyseurLexical.GENCODE_lecture();

                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    fin = false;
                    erreur = false;
                    while(!fin){
                        if(Compilateur.UNILEX == Compilateur.T_UNILEX.virg){
                            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){

                                if(!isDeclaredVariable()){
                                    return false;
                                }

                                Compilateur.analyseurLexical.GENCODE_empilement();
                                Compilateur.analyseurLexical.GENCODE_lecture();

                                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                            }
                            else{
                                fin = true;
                                erreur = true;
                            }
                        }
                        else{
                            fin = true;
                        }
                    }
                    if(erreur){
                        messageErreur = "identificateur attendu";
                        return false; //identificateur attendu
                    }
                    else if(Compilateur.UNILEX == Compilateur.T_UNILEX.parfer){
                        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                        return true;
                    }
                    else{
                        messageErreur = ") attendu";
                        return false; // ) attendu
                    }
                }
                else{
                    messageErreur = "identificateur attendu";
                    return false; // identificateur attendu
                }
            }
            else{
                messageErreur = "( attendu";
                return false; // ( attendu
            }
        }
        else{
            messageErreur = "mot clé LIRE attendu";
            return false; // mot clé lire attendu
        }

    }

    // ecriture -> 'ecrire' '(' [ecr_exp { ',' ecr_exp }] ')'
    public boolean ecriture() throws IOException {

        boolean fin, erreur;
        if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("ECRIRE")){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.parouv){
                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                erreur = false;
                if(ecr_exp()){
                    fin = false;
                    while(!fin){
                        if(Compilateur.UNILEX == Compilateur.T_UNILEX.virg){
                            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                            erreur = !ecr_exp();
                            if(erreur){
                                fin = true;
                            }
                        }
                        else{
                            fin = true;
                        }
                    }
                }
                else{
                    Compilateur.analyseurLexical.GENCODE_ecriture();
                }
                if(erreur){
                    return false; //Expression incorrecte
                }
                else if (Compilateur.UNILEX == Compilateur.T_UNILEX.parfer){
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    return true;
                }
                else{
                    messageErreur = ") attendu";
                    return false; // ) attendu
                }
            }
            else{
                messageErreur = "( attendu";
                return false; // ( attendu
            }
        }
        else{
            messageErreur = "mot clé ECRIRE attendu";
            return false; //mot clé ECRIRE attendu
        }

    }

    // ecr_exp -> exp | 'ch'
    public boolean ecr_exp() throws IOException {

        if(!exp()){
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ch){

                Compilateur.analyseurLexical.GENCODE_ecr_exp_ch();

                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                return true;
            }
            else{
                messageErreur = "chaine de caracteres attendu";
                return false;
            }
        }
        else{
            Compilateur.analyseurLexical.GENCODE_ecr_exp();
            return true;
        }

    }

    // exp -> terme suite_terme
    public boolean exp() throws IOException {
        if(terme()){
            return suite_terme();
        }
        else{
            return false;
        }
    }

    // epsilon | op_bin exp
    public boolean suite_terme() throws IOException {
        if(op_bin()){
            if(exp()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return true; //epsilon
        }
    }

    // 'ent' | 'ident' | '(' exp ')' | '-' terme
    public boolean terme() throws IOException {

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.ent){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            return true;
        }

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){
            int indice = Compilateur.tableIdentificateur.chercher(Compilateur.chaine);
            if(indice == -1){
                GestionErreur.erreur(4, "identifiant pas déclaré");
                return false; //ident pas déclaré
            }
            else{
                if(Compilateur.tableIdentificateur.enregIdents.get(indice).getType() != EnregIdent.TYPE.entier){
                    GestionErreur.erreur(4, "l'identifiant n'est pas un entier");
                    return false;
                }
            }

            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            return true;
        }

        else if(Compilateur.UNILEX == Compilateur.T_UNILEX.parouv){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(exp()){
                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                if(Compilateur.UNILEX == Compilateur.T_UNILEX.parfer){
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    return true;
                }
                else{
                    messageErreur = ") attendu";
                    return false; // ) attendue
                }
            }
            else{
                return false; //Expression incorrecte
            }
        }
        else if(Compilateur.UNILEX == Compilateur.T_UNILEX.moins){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(!terme()){
                return false;
            }
            else{
                return true;
            }
        }

        return false;
    }

    // op_bin -> '+' | '-' | '*' | '/'
    public boolean op_bin() throws IOException {
        if(Compilateur.UNILEX == Compilateur.T_UNILEX.plus ||
        Compilateur.UNILEX == Compilateur.T_UNILEX.moins ||
        Compilateur.UNILEX == Compilateur.T_UNILEX.mult ||
        Compilateur.UNILEX == Compilateur.T_UNILEX.divi){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            return true;
        }
        else{
            messageErreur = "+, -, * ou / attendu";
            return false;
        }

    }

}
