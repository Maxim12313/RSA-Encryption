import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    static HashMap<String,Key> classKeys = new HashMap<>();
    static Key myPublicKey;
    static Key myPrivateKey;

    public static void test(){
        KeyGenerator keyGenerator = new KeyGenerator("Maxim");
        Key[] keys = keyGenerator.produceKeyPair(100);
        Key publicKey = keys[0];
        Key privateKey = keys[1];
        publicKey.printData();
        privateKey.printData();
        String encryptedMessage = publicKey.encrypt("Maxim So Cool");
        String decryptedMessage = privateKey.decrypt(encryptedMessage);

    }

    //public
    //n: 996822230391233382618927203947    special: 413018650419270147154599310873
    //n: [12, -108, -24, 5, -20, -103, -33, 64, 101, -91, 100, 6, 107]    special: [5, 54, -119, 5, 47, 20, -121, 76, -47, -73, -27, 118, 25]

    //private
    //n: 996822230391233382618927203947    special: 487949788420529566596459702697
    //n: [12, -108, -24, 5, -20, -103, -33, 64, 101, -91, 100, 6, 107]    special: [6, 40, -90, -102, -39, 120, 17, -86, -106, -108, 116, -87, -87]

    //my name is maximmmmm!
    //2334392247088347501,7883964982526767977,143569808749
    //my name is maximmmmm!

    //b:n:e    100:996822230391233382618927203947:413018650419270147154599310873

    public static void processMyKeys(){
        BigInteger n = new BigInteger("996822230391233382618927203947");
        BigInteger e = new BigInteger("413018650419270147154599310873");
        BigInteger d = new BigInteger("487949788420529566596459702697");
        myPublicKey = new Key(n,e,100,false,"Maxim");
        myPrivateKey =  new Key(n,d,100,true,"Maxim");
    }

    public static void processClassKeys() {
        try {
            FileReader file = new FileReader("classKeys.txt");
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            while (line != null) {
                String[] splitData = line.split(";");
                String name = splitData[0];
                String keyLength = splitData[1];
                BigInteger n = new BigInteger(splitData[2]);
                BigInteger e = new BigInteger(splitData[3]);
                Key publicKey = new Key(n,e,Integer.parseInt(keyLength),true,name);
                classKeys.put(name,publicKey);
                line = reader.readLine();
            }
        } catch (IOException ioexception) {
            System.out.println("Ack!  We had a problem: " + ioexception.getMessage());
        }
    }

    public static String decryptPublicMessage(String message,String name){
        Key publicKey = classKeys.get(name);
        publicKey.printData();
        return publicKey.decrypt(message);
    }

    public static String decryptPrivateMessage(String message){
        myPrivateKey.printData();
        return myPrivateKey.decrypt(message);
    }

    public static String encryptPublicMessage(String message){
        myPrivateKey.printData();
        return myPrivateKey.encrypt(message);
    }

    public static String encryptPrivateMessage(String message,String name){
        Key publicKey = classKeys.get(name);
        publicKey.printData();
        return publicKey.encrypt(message);
    }

    public static BigInteger getFactors(BigInteger mainNumber){
        return null;
    }

    public static void testing(){
        KeyGenerator keyGenerator = new KeyGenerator("Maxim");
        Key[] keys = keyGenerator.produceKeyPair(100);
        Key publicKey = keys[0];
        Key privateKey = keys[1];
        String encryptedMessage = publicKey.encrypt("You suck so bad");
        privateKey.decrypt(encryptedMessage);
    }

    public static void main(String[] args) {
        processMyKeys();
        processClassKeys();
        Factorer factorer = new Factorer(100);
        factorer.getFactoredNumbers(BigInteger.valueOf(80),4);



    }
}
