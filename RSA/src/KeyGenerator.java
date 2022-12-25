import java.math.BigInteger;
import java.util.Random;

public class KeyGenerator {
    private Random rand;
    private String name;


    public KeyGenerator(String name) { //65-100
        rand = new Random();
        this.name = name;
    }

    public Key[] produceKeyPair(int bitSize){ //(0,public) (1,private)

        int bitsP = rand.nextInt(bitSize);
        int bitsQ = bitSize-bitsP;

        BigInteger p = BigInteger.probablePrime(bitsP,rand);
        BigInteger q = BigInteger.probablePrime(bitsQ,rand);

        BigInteger n = p.multiply(q); //pq

        BigInteger pheeN = n.subtract(p).subtract(q).add(BigInteger.valueOf(1)); //n – p – q + 1.

        BigInteger[] ed = getED(pheeN); //(0,e) (1,d)

        Key publicKey = new Key(n,ed[0],bitSize,true,name);
        Key privateKey = new Key(n,ed[1],bitSize,false,name);

        return new Key[]{publicKey,privateKey};
    }


    private BigInteger[] getED(BigInteger pheeN){ //(0,e) (1,d)
        BigInteger e = getE(pheeN);
        BigInteger d = e.modInverse(pheeN);
        if (d.equals(e)){ //ASK ANDREW ABOUT EQUALS VS COMPARETO==0 AND ABOUT STACK OVERFLOW
            return getED(pheeN);
        }
        return new BigInteger[]{e,d};
    }


    private BigInteger getE(BigInteger pheeN) {
        BigInteger e = new BigInteger(pheeN.bitLength() - 1, rand); //does this ensure it's smaller?

        //how to do "do while" loop
        while (!Factorer.getGCD(e, pheeN).equals(BigInteger.ONE)) {
            e = new BigInteger(pheeN.bitLength() - 1, rand);
        }
        return e;
    }
}
