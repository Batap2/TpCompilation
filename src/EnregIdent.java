public class EnregIdent {

    public enum GENRE{constante, variable, fonction, procedure, type};

    private String nom;
    private GENRE genre;
    private String valeur; //si int on parseToInt
    private int adresse;

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
        builder.append(valeur+ " ");
        builder.append(adresse);
        return builder.toString();
    }

    public EnregIdent(String nom, GENRE genre){
        this.nom = nom;
        this.genre = genre;
    }

    static public String[] getAttributs(){
        return new String[]{"nom", "genre", "valeur", "adresse"};
    }

}
