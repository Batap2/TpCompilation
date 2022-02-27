import java.util.ArrayList;
import java.util.List;

public class TableIdentificateur {

    public List<EnregIdent> enregIdents = new ArrayList();
    static final int INDEX_NO_FOUND = -1;

    public int chercher(String name){
        for(int indiceTable = 0; indiceTable<enregIdents.size(); indiceTable++){
            EnregIdent enreg = enregIdents.get(indiceTable);
            if(enreg.getNom().equals(name)){
                return indiceTable;
            }
        }
        return INDEX_NO_FOUND;
    }

    public int inserer(EnregIdent enregIdent){
        enregIdents.add(enregIdent);
        return enregIdents.size()-1;
    }

    public void affiche_table_ident(){
        String[] columnsName = EnregIdent.getAttributs();
        for(int indiceAttributs = 0; indiceAttributs<columnsName.length; indiceAttributs++){
            System.out.print(columnsName[indiceAttributs]+" ");
        }
        System.out.println("");
        for(int indiceTable = 0; indiceTable<enregIdents.size(); indiceTable++){
            EnregIdent enreg = enregIdents.get(indiceTable);
            System.out.println(enreg.toString());
        }

    }


}
