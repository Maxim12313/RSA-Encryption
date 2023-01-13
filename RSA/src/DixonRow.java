import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

public class DixonRow {
    //class only used in my class, so effectively private
    public int row;
    public BigInteger r;
    public BigInteger number; //f(r)
    public BitSet parityMatrix;
    public BitSet identityMatrix;

    public DixonRow(int row, BigInteger r, BigInteger number, BitSet parityMatrix) {
        this.row = row;
        this.r = r;
        this.number = number;
        this.parityMatrix = parityMatrix;
        this.identityMatrix = new BitSet();
    }

    public void add(DixonRow otherRow){
        parityMatrix.xor(otherRow.parityMatrix);
        identityMatrix.xor(otherRow.identityMatrix);
    }

    public BigInteger[] computeFinalValues(ArrayList<DixonRow> dixonRowList){
        BigInteger newR = BigInteger.ONE;
        BigInteger newNumber = BigInteger.ONE;
        for (int i=0;i<identityMatrix.size();i++){
            if (identityMatrix.get(i)){
                DixonRow otherRow = dixonRowList.get(i);
                newR=newR.multiply(otherRow.r);
                newNumber = newNumber.multiply(otherRow.number);
            }
        }
        return new BigInteger[]{
                newR,
                newNumber
        };
    }


    public void dataPrint(){
        System.out.println("row: "+row+"    r: "+r+"    number: "+number);
        System.out.println("parityMatrix: ");
        for (int i=0;i<parityMatrix.size();i++){
            System.out.print(i+" : "+parityMatrix.get(i)+"    ");
        }

        System.out.println();
        System.out.println("identityMatrix: ");
        for (int i=0;i<identityMatrix.size();i++){
            System.out.print(i+": "+identityMatrix.get(i)+"    ");
        }
        System.out.println();
        System.out.println();
    }



}
