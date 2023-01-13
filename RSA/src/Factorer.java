import java.math.BigInteger;
import java.util.*;

import static java.lang.Math.floor;

public class Factorer {
    private int pairs = 30; //extra factoring
    private ArrayList<BigInteger> primes;



    public Factorer(){
        primes = primeFinderSieve();
//        System.out.println(primes);
    }

    private ArrayList<BigInteger> primeFinderSieve(){
        int primeLimit = 10000000;
        boolean[] primeBoolean = new boolean[primeLimit+1];
        ArrayList<BigInteger> primeInteger = new ArrayList<>();

       for (int i=2;i<primeBoolean.length;i++){
           primeBoolean[i] = true;
       }

        for (int i=2;i<Math.sqrt(primeBoolean.length);i++){
            if (primeBoolean[i]) {
                for (int p = i * 2; p < primeBoolean.length; p += i) {
                    primeBoolean[p] = false; //not a prime
                }
            }
        }

        for (int i=0;i<primeBoolean.length;i++){
            if (primeBoolean[i]){
                primeInteger.add(BigInteger.valueOf(i));
            }
        }

        return primeInteger;
    }

    public static BigInteger getGCD(BigInteger a, BigInteger b){
//        System.out.println(a+"    "+b);
        if (a.equals(b)){
            return a;
        }
        else if (a.compareTo(b)>0){
            return getGCD(a.subtract(b),b);
        }
        else{
            return getGCD(a,b.subtract(a));
        }
//        return a.gcd(b);
    }

    //n=(x^2-y^2) every odd semiprime n is a difference of 2 perfect squares
    //x^2 ≡ y^2 (mod n)     but x ≠ y
    // x^2 - y^2 ≡ 0 (mod n), so x^2 - y^2 is a multiple of n
    //(x + y)(x - y) is also a multiple of n.





    public BigInteger[] dixonFactor(BigInteger n){
//        int numFactors = 1000;
        int numFactors = (int)(5*Math.exp(0.07*n.bitLength()));
        System.out.println("numFactors: "+numFactors);
        System.out.println("highestPrime: "+primes.get(numFactors));
        //30*e^0.08*bitsN
        ArrayList<DixonRow> dixonRowList = getFactoredRows(n,numFactors);
        ArrayList<Integer> removedIndexes = matrixCombination(dixonRowList,numFactors);

        for (DixonRow row:dixonRowList){
            if (removedIndexes.contains(row.row)) continue;

            BigInteger[] data = row.computeFinalValues(dixonRowList);

            BigInteger x = data[0].mod(n);
            BigInteger y = data[1].sqrt().mod(n);

            BigInteger xy = x.add(y);

            BigInteger factor1 = getGCD(xy,n);

            if (!factor1.equals(BigInteger.ONE) && !factor1.equals(n)){
                BigInteger factor2 = n.divide(factor1);
                return new BigInteger[]{factor1,factor2};
            }
        }

        System.out.println("failed");
        return null;
    }





    public ArrayList<Integer> matrixCombination(ArrayList<DixonRow> dixonRowList,int numFactors){ //returns removedIndex list
        ArrayList<Integer> removedIndexes = new ArrayList<>();

        for (int column=0;column<numFactors;column++){ //loop through parity columns
            int rowMultiplierIndex = -1;

            for (int row=0;row<dixonRowList.size();row++){ //loop through dixon rows
                DixonRow dixonRow = dixonRowList.get(row);

                if (removedIndexes.contains(row)) continue;

                if (dixonRow.parityMatrix.get(column)){ //if column parity is 1
                    if (rowMultiplierIndex==-1){ //if row with 1 hasnt been found
                        rowMultiplierIndex = row;
                        removedIndexes.add(rowMultiplierIndex);
                    }
                    else{ //if row with 1 has been found for adding
                        dixonRow.add(dixonRowList.get(rowMultiplierIndex));
//                        System.out.println("add "+rowMultiplierIndex+" to "+row);
                    }
                }
            }
        }

        return removedIndexes;
    }


    public BitSet getFactorParity(BigInteger number,int numFactors){
        BitSet factorParityArray = new BitSet(); //do I need to miss with bitset size?
//        System.out.println(primes.get(numFactors));

        for (int i=0;i<numFactors;i++){
            BigInteger primeFactor = primes.get(i);

            if (number.mod(primeFactor).equals(BigInteger.ZERO)){ //if num%prime == 0
                int powers = 1;
                number = number.divide(primeFactor);
                while (number.mod(primeFactor).equals(BigInteger.ZERO)){ //while num can be entirely divided
                    number = number.divide(primeFactor);
                    powers++;
                }

                int factorParity = powers%2;
                if (factorParity==1){
                    factorParityArray.set(i,true);
                }
                //else false 0, which is default

                if (number.equals(BigInteger.ONE)){
                    return factorParityArray;
                }
            }
        }
        return null;
    }

    public ArrayList<DixonRow> getFactoredRows(BigInteger n, int numFactors){
        ArrayList<DixonRow> factoredNumbers = new ArrayList<>();

        BigInteger r = n.sqrtAndRemainder()[0].add(BigInteger.ONE);
        int counter = 0;
        while (factoredNumbers.size()<=numFactors+pairs) {
            BigInteger number = f(r,n);

            //is this true?
            if (number.equals(BigInteger.ZERO)){
                r=r.add(BigInteger.ONE);
                continue;
            }

            BitSet factorParityArray = getFactorParity(number,numFactors);


            if (factorParityArray!=null){
//                System.out.println("r: "+r);
                DixonRow dixonRow = new DixonRow(counter,r,number,factorParityArray);
                factoredNumbers.add(dixonRow);
                counter++;
            }

            r = r.add(BigInteger.ONE);
        }

        for (int i=0;i<factoredNumbers.size();i++){
            DixonRow row = factoredNumbers.get(i);
            row.identityMatrix.set(i,true);
        }
        return factoredNumbers;
    }

    private BigInteger f(BigInteger r,BigInteger n){
        return (r.multiply(r)).mod(n); //f(r) = r^2 % n
    }


}
