import java.io.IOException;

public class AnalyseurSyntaxique {

    public void anasynt() throws IOException {
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

}
