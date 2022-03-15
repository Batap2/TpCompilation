public class Memoire {

    //3 parties : MEMVAR, P_CODE et PILEX
    //MEMVAR et P_CODE de taille fixe

    private final int taille = 10000;
    private int[] memoireCentrale = new int[taille];
    private int indiceDebutP_CODE = Compilateur.DERNIERE_ADRESSE_VAR_GLOB + 1;
    private int indiceDebutPILEX = ((taille - indiceDebutP_CODE)/2) + indiceDebutP_CODE;
    private int CO = indiceDebutP_CODE;

    private int[] PILOP = new int[taille/4];
    private int som_pilop = -1;
    private int som_pilex = indiceDebutPILEX-1;

    public enum MOT_MEMOIRE{ADDI, SOUS, MULT, DIVI, MOIN, AFFE, LIRE, ECRL, ECRE, ECRC, FINC, EMPI, CONT, STOP};

    public void setMemvar(int indice, int value){
        memoireCentrale[indice] = value;
    }

    public int getMemvar(int indice){
        return memoireCentrale[indice];
    }

    public int getNbMotsReservesVariableGlobales(){
        return indiceDebutP_CODE;
    }

    public void setPilex(int indice, int value){
        memoireCentrale[indice] = value;
    }

    public int getSom_pilex(){
        return som_pilex;
    }

    public int getContenuPilex(int indice){
        return memoireCentrale[indice];
    }

    public void setSom_pilex(int saut){
        som_pilex = som_pilex+saut;
    }

    public void setCO(int saut){
        CO = CO+saut;
    }

    public void setSom_pilop(int saut){
        som_pilop = som_pilop + saut;
    }

    public int getCO(){
        return CO;
    }

    public int getSom_pilop(){
        return som_pilop;
    }

    public int getContenuP_CODE(int indice){
        return memoireCentrale[indice];
    }

    public int getContenuPILOP(int indice){
        return PILOP[indice];
    }

    public void setP_CODE(int indice, int value){
        memoireCentrale[indice] = value;
    }

    public void setPILOP(int indice, int value){
        PILOP[indice] = value;
    }

    public void print(){
        for(int i=0; i<taille;i++){
            System.out.println(i+": "+memoireCentrale[i]);
        }
    }

}
