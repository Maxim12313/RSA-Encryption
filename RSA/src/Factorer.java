import java.math.BigInteger;
import java.util.*;

public class Factorer {
    private int pairs = 3; //extra factoring
    private ArrayList<BigInteger> primes;



    public Factorer(int primeLimit){
        primes = primeFinderSieve(primeLimit);
    }

    private ArrayList<BigInteger> primeFinderSieve(int primeLimit){
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
    }

    //n=(x^2-y^2) every odd semiprime n is a difference of 2 perfect squares
    //x^2 ≡ y^2 (mod n)     but x ≠ y
    // x^2 - y^2 ≡ 0 (mod n), so x^2 - y^2 is a multiple of n
    //(x + y)(x - y) is also a multiple of n.

    public BigInteger dixonFactor(BigInteger n){
        int numFactors = 10;

        return null;
    }



    public BigInteger[][] getFactorPairs(BigInteger n, HashMap<BigInteger,HashMap<BigInteger,BigInteger>> factoredNumbers){
        BigInteger[][] factoredPairs = new BigInteger[pairs][2]; //number of pairs, pair
        for (BigInteger factoredNumber: factoredNumbers.keySet()){
            HashMap<BigInteger,BigInteger> factors = factoredNumbers.get(factoredNumber);

        }

        return null;

    }
    public HashMap<BigInteger,HashMap<BigInteger,BigInteger>> getFactors(BigInteger n, HashMap<BigInteger,HashMap<BigInteger,BigInteger>> factoredNumbers){
        for (BigInteger factoredNumber: factoredNumbers.keySet()){
            HashMap<BigInteger,BigInteger> factors = factoredNumbers.get(factoredNumber);
        }

        return null;

    }

    public HashMap<BigInteger,BigInteger[]> getFactoredNumbers(BigInteger n, int numFactors){
        HashMap<BigInteger,BigInteger[]> factoredNumbers = new HashMap<>(); //number - > factors - > even or odd 0,1
        List<BigInteger> factors = primes.subList(0,numFactors);

        BigInteger number = BigInteger.valueOf(2);

        while (factoredNumbers.size()<=numFactors+pairs){
            BigInteger[] numberFactors = new BigInteger[factors.size()];
            Arrays.fill(numberFactors,BigInteger.ZERO);
            boolean factored = false;

            for (int i=0;i<factors.size();i++){
                BigInteger primeFactor = factors.get(i);
                if (primeFactor.compareTo(number.sqrt())>0){
                    break;
                }

                if (number.mod(primeFactor).equals(BigInteger.ZERO)){ //if num%prime == 0
                    BigInteger num = number.divide(primeFactor); // number/primeFactor
                    BigInteger factorParity = num.mod(BigInteger.TWO); // (number/primeFactor)%2, if even 0, if odd 1
                    numberFactors[i]=factorParity;
                    factored = true;
//                    System.out.println("number: "+number+"    factor: "+primeFactor+"    timesInto: "+dynamicNumber);
                }
            }

            if (factored){
                System.out.print("num: "+number+"    ");
                for (int i=0;i<numberFactors.length;i++){
                    System.out.print(factors.get(i)+": "+numberFactors[i]+"    ");
                }
                System.out.println();
                factoredNumbers.put(number,numberFactors);
            }
            number = number.add(BigInteger.ONE);
        }
        return factoredNumbers;
    }

    private BigInteger f(BigInteger r,BigInteger n){
        return (r.multiply(r)).mod(n); //f(r) = r^2 % n

    }


}
