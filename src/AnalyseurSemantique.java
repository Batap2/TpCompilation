public class AnalyseurSemantique {

    public boolean definir_constante(String nom, Compilateur.T_UNILEX unilex){

        EnregIdent.GENRE genre = EnregIdent.GENRE.constante;
        EnregIdent enregIdent = new EnregIdent(nom, genre);

        if(Compilateur.tableIdentificateur.chercher(nom) != -1){
            return false;
        }
        else{
            if(unilex == Compilateur.T_UNILEX.ent){
                EnregIdent.TYPE typec = EnregIdent.TYPE.entier;
                enregIdent.setType(typec);
                enregIdent.setValeur(Compilateur.nombre);
            }
            else{
                EnregIdent.TYPE typec = EnregIdent.TYPE.chaine;
                Compilateur.NB_CONST_CHAINE ++;
                Compilateur.VAL_DE_CONST_CHAINE.add(Compilateur.chaine);
                enregIdent.setType(typec);
                enregIdent.setValeur(Compilateur.NB_CONST_CHAINE-1);
            }
            Compilateur.tableIdentificateur.inserer(enregIdent);
            return true;

        }

    }

    public boolean definir_variable(String nom){

        EnregIdent.GENRE genre = EnregIdent.GENRE.variable;
        EnregIdent enregIdent = new EnregIdent(nom, genre);

        if(Compilateur.tableIdentificateur.chercher(nom) != -1){
            return false;
        }
        else{

            enregIdent.setType(EnregIdent.TYPE.entier);
            Compilateur.DERNIERE_ADRESSE_VAR_GLOB++;
            enregIdent.setAdresse(Compilateur.DERNIERE_ADRESSE_VAR_GLOB);

            Compilateur.tableIdentificateur.inserer(enregIdent);
            return true;

        }

    }


}
