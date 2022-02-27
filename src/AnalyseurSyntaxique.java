import java.io.IOException;

public class AnalyseurSyntaxique {

    //private AnalyseurLexical analyseurLexical = new AnalyseurLexical();
    private AnalyseurSemantique analyseurSemantique = new AnalyseurSemantique();

    public void anasynt() throws IOException {
        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
        if(prog()){
            System.out.println("Le programme source est syntaxiquement correct");
            Compilateur.getTableIdentificateur().affiche_table_ident();
        }
        else{
            GestionErreur.erreur(3);
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
                            System.out.println(". attendu");
                            return false; // . attendu
                        }
                    }
                    else{
                        System.out.println("bloc incorrect");
                        return false; //bloc incorrect
                    }
                }
                else{
                    System.out.println("; attendu");
                    return false; // ; attendu
                }
            }
            else{
                System.out.println("ident attendu");
                return false; // ident attendu
            }
        }
        else{
            System.out.println("mot clé programme attendu");
            System.out.println(Compilateur.chaine);
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
                                                }

                                            }
                                            else{
                                                fin = true;
                                                erreur = true; //entier ou chaine de caractères attendu
                                            }
                                        }
                                        else{
                                            fin = true;
                                            erreur = true; //= attendu
                                        }
                                    }
                                    else{
                                        fin = true;
                                        erreur = true; //identifiant attendu
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
                                return false; // ; attendu
                            }
                        }
                        else{
                            //todo : erreur semantique
                            return false; //erreur sémantique dans la déclaration des constantes, ident déjà déclaré
                        }

                    }
                    else{
                        return false; //entier ou chaine de caracteres attendu
                    }
                }
                else{
                    return false; // = attendu
                }
            }
            else{
                return false; //identifiant attendu
            }
        }
        else{
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
                        return false; //identifiant attendu
                    }
                    else if(Compilateur.UNILEX == Compilateur.T_UNILEX.ptvirg){
                        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                        return true;
                    }
                    else{
                        return false; // ; attendu
                    }
                }
                else{
                    return false; //erreur sémantique dans la déclaration des variables : identificateur déjà déclaré
                }

            }
            else{
                return false; //identifiant attendu
            }
        }
        else{
            return false; // mot clé var attendu
        }

    }

    //'debut' instruction {';' instruction } 'fin'
    public boolean bloc() throws IOException {
        boolean fin, erreur;

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.motcle && Compilateur.chaine.equals("DEBUT")){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
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
                    System.out.println("mot cle fin attendu");
                    return false; //mot clé fin attendu
                }
            }
            else{
                return false; //instruction invalide
            }
        }
        else{
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

    // affectation -> 'ident' ':=' exp
    public boolean affectation() throws IOException {
        if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){
            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.aff){
                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                return exp();
            }
            else{
                return false; //affectation attendue
            }
        }
        else{
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
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    fin = false;
                    erreur = false;
                    while(!fin){
                        if(Compilateur.UNILEX == Compilateur.T_UNILEX.virg){
                            Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ident){
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
                        return false; //identificateur attendu
                    }
                    else if(Compilateur.UNILEX == Compilateur.T_UNILEX.parfer){
                        Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                        return true;
                    }
                    else{
                        return false; // ) attendu
                    }
                }
                else{
                    return false; // identificateur attendu
                }
            }
            else{
                return false; // ( attendu
            }
        }
        else{
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
                if(erreur){
                    return false; //Expression incorrecte
                }
                else if (Compilateur.UNILEX == Compilateur.T_UNILEX.parfer){
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                    return true;
                }
                else{
                    return false; // ) attendu
                }
            }
            else{
                return false; // ( attendu
            }
        }
        else{
            return false; //mot clé ECRIRE attendu
        }

    }

    // ecr_exp -> exp | 'ch'
    public boolean ecr_exp() throws IOException {

        if(!exp()){
            if(Compilateur.UNILEX == Compilateur.T_UNILEX.ch){
                Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
                return true;
            }
            else{
                return false;
            }
        }
        else{
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

        if(Compilateur.UNILEX == Compilateur.T_UNILEX.ent ||
        Compilateur.UNILEX == Compilateur.T_UNILEX.ident){
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
            return false;
        }

    }

}
