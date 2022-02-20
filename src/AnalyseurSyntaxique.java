import java.io.IOException;

public class AnalyseurSyntaxique {

    public void anasynt() throws IOException {
        Compilateur.UNILEX = Compilateur.analyseurLexical.analex(); //?
        if(prog()){
            System.out.println("Le programme source est syntaxiquement correct");
        }
        else{
            GestionErreur.erreur(3);
        }
    }

    public boolean prog(){
        //todo
        return false;
    }

    public boolean decl_const(){
        //Todo
        return false;
    }

    public boolean decl_var(){
        //Todo
        return false;
    }

    public boolean bloc(){
        //Todo
        return false;
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
                    Compilateur.UNILEX = Compilateur.analyseurLexical.analex();
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
                    return false; // ) atterndu
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

    public boolean ecr_exp(){
        //Todo
        return false;
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
    public boolean suite_terme(){
        //Todo
        return false;
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
            //todo
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
