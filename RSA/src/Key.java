import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;

public class Key {
    private String name;
    private boolean isPublic;
    private int keyLength;
    private BigInteger n;
    private BigInteger special; //e or d

    public Key(BigInteger n, BigInteger special, int keyLength,boolean isPublic, String name){
        this.n = n;
        this.special = special;
        this.keyLength = keyLength;
        this.isPublic = isPublic;
        this.name = name;
    }

    public int getKeyLength(){
        return keyLength;
    }

    public BigInteger getN(){
        return n;
    }

    public BigInteger getSpecial(){
        return special;
    }

    public BigInteger[] stringToBigIntegers(String message){
        //convert to byte array
        byte[] byteArray = message.getBytes();

        //convert to Bitset
        BitSet bitSet = BitSet.valueOf(byteArray);

        //convert to long array
        long[] longArray = bitSet.toLongArray();

        //convert to BigInteger array

        BigInteger[] bigIntegerArray = new BigInteger[longArray.length];
        for (int i=0;i<longArray.length;i++) {
            long value = longArray[i];
            bigIntegerArray[i] = BigInteger.valueOf(value);
        }
        return bigIntegerArray;
    }

    public String BigIntegersToString(BigInteger[] bigIntegerArray){
        //convert to long array
        long[] longArray = new long[bigIntegerArray.length];
        for (int i=0;i<bigIntegerArray.length;i++){
            BigInteger bigInteger = bigIntegerArray[i];
            longArray[i] = bigInteger.longValueExact();
        }

        //convert to bitset
        BitSet bitSet = BitSet.valueOf(longArray);

        //convert to byte array
        byte[] byteArray = bitSet.toByteArray();

        //convert to string
        return new String(byteArray);
    }


    public String encrypt(String entireMessage){
        System.out.println("Plaintext Message: "+entireMessage);
        BigInteger[] bigIntegerArray = stringToBigIntegers(entireMessage);
        for (int i=0;i<bigIntegerArray.length;i++){
            bigIntegerArray[i] = powModN(bigIntegerArray[i],special,n); // s^special mod(n)
        }

        //concatonate BigInteger values into 1 string
        String message = "";
        for (int i=0;i<bigIntegerArray.length;i++){
            if (i!=0){ //if it's not the first
                message+=",";
            }
            BigInteger value = bigIntegerArray[i];
            message+=value.toString();
        }
        System.out.println("Encrypted Message: "+message);
        System.out.println("------------------------");
        return message;
    }

    public String decrypt(String entireMessage){
        //many loop speed vs 1 loop and more actions?

        String[] messageArray = entireMessage.split(",");
        BigInteger[] bigIntegerArray = new BigInteger[messageArray.length];

        //convert to bigIntegerArray
        for (int i=0;i<messageArray.length;i++){
            String message = messageArray[i];
            bigIntegerArray[i] = new BigInteger(message);
        }

        //decrypt BigIntegers in bigIntegerArray
        for (int i=0;i<bigIntegerArray.length;i++){
            bigIntegerArray[i] = powModN(bigIntegerArray[i],special,n); // s^special mod(n)
        }

        String message = BigIntegersToString(bigIntegerArray);

        System.out.println("Decrypted Message: "+message);
        System.out.println("------------------------");
        return message;
    }

    public static BigInteger powModN(BigInteger base, BigInteger exponent, BigInteger n){
        if (exponent.equals(BigInteger.ZERO)){
            return BigInteger.ONE;
        }
        else if (exponent.equals(BigInteger.ONE)){
            return base.mod(n);
        }
        else if (exponent.mod(BigInteger.TWO).equals(BigInteger.ZERO)){ //if even
            return powModN(base,exponent.divide(BigInteger.TWO),n).pow(2).mod(n); //(a^b/2)^2 mod(n)
        }
        else{ //if odd
            return base.multiply(powModN(base,exponent.subtract(BigInteger.ONE),n)).mod(n); //a*a^b-1
        }
    }


    public void printData(){
        String type;
        String variable;
        if (isPublic){
            type = "Public Key";
            variable = "e";
        }
        else{
            type = "Private Key";
            variable = "d";
        }

        System.out.println(name+"'s "+type);

        String line = "b;n;"+variable;
        System.out.println(line);

        System.out.println(keyLength+";"+n+";"+special);
        System.out.println("------------------------");
    }
}
