public class Memoire {

    //3 parties : MEMVAR, P_CODE et PILEX
    //MEMVAR et P_CODE de taille fixe

    private final int taille = 10000;
    private int[] memoireCentrale = new int[taille];
    private int indiceDebutP_CODE = Compilateur.DERNIERE_ADRESSE_VAR_GLOB + 1;
    private int indiceDebutPILEX = ((taille - indiceDebutP_CODE)/2) + indiceDebutP_CODE;
    private int CO = indiceDebutP_CODE;

    public enum MOT_MEMOIRE{ADDI, SOUS, MULT, DIVI, MOIN, AFFE, LIRE, ECRL, ECRE, ECRC, FINC, EMPI, CONT, STOP};

    public void setCO(int saut){
        CO = CO+saut;
    }

    public int getCO(){
        return CO;
    }

    public void setP_CODE(int indice, int value){
        memoireCentrale[indice] = value;
    }


}
