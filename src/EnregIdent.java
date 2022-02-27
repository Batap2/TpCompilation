public class EnregIdent {

    public enum GENRE{constante, variable, fonction, procedure, type};
    public enum TYPE{entier, chaine}

    private String nom;
    private GENRE genre;
    private TYPE type;
    private int valeur;
    private int adresse = -1;

    public void setType(TYPE type){
        this.type= type;
    }

    public void setValeur(int valeur){
        this.valeur = valeur;
    }

    public void setAdresse(int adresse){
        this.adresse = adresse;
    }

    public String getNom(){
        return nom;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(nom+" ");
        if(genre == GENRE.constante){
            builder.append("constante ");
        }
        else if(genre == GENRE.variable){
            builder.append("variable ");
        }
        else if(genre == GENRE.fonction){
            builder.append("fonction ");
        }
        else if(genre == GENRE.procedure){
            builder.append("procedure ");
        }
        else if(genre == GENRE.type){
            builder.append("type ");
        }
        else{
            builder.append("null ");
        }
        if(type == TYPE.entier){
            builder.append("entier ");
        }
        else if(type == TYPE.chaine){
            builder.append("chaine ");
        }
        else{
            builder.append("null ");
        }
        if(this.genre == GENRE.constante && this.type == TYPE.chaine){
            builder.append(Compilateur.VAL_DE_CONST_CHAINE.get(valeur));
            builder.append(" ");
        }
        else{
            builder.append(valeur+ " ");
        }

        builder.append(adresse);
        return builder.toString();
    }

    public EnregIdent(String nom, GENRE genre){
        this.nom = nom;
        this.genre = genre;
    }

    static public String[] getAttributs(){
        return new String[]{"nom", "genre", "type", "valeur", "adresse"};
    }

}
