import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.currentTimeMillis;

public class Main {
    static HashMap<String,Key> classKeys = new HashMap<>();
    static Key myPublicKey;
    static Key myPrivateKey;
    static Factorer factorer = new Factorer();

    public static void test(){
        KeyGenerator keyGenerator = new KeyGenerator();
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
        myPublicKey = new Key(n,e);
        myPrivateKey =  new Key(n,d);
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
                Key publicKey = new Key(n,e);
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
        KeyGenerator keyGenerator = new KeyGenerator();
        Key[] keys = keyGenerator.produceKeyPair(100);
        Key publicKey = keys[0];
        Key privateKey = keys[1];
        String encryptedMessage = publicKey.encrypt("You suck so bad");
        privateKey.decrypt(encryptedMessage);
    }

    public static Key canvasCrackKey(String keyInfo,String message){
        String[] seperatedKeyInfo = keyInfo.split(";");
        String bits = seperatedKeyInfo[0];
        BigInteger n = new BigInteger(seperatedKeyInfo[1]);
        BigInteger e = new BigInteger(seperatedKeyInfo[2]);
        BigInteger d = findD(n,e);
        Key privateKey = new Key(n,d);
        System.out.println("Bits: "+bits);
        privateKey.decrypt(message);
        privateKey.printData();
        return privateKey;
    }

    public static BigInteger findD(BigInteger n, BigInteger e){
        long startTime =currentTimeMillis();
        BigInteger[] factors = factorer.dixonFactor(n);
        BigInteger p = factors[0];
        BigInteger q = factors[1];
//        System.out.println("n: "+n+"    e: "+e+"    p: "+p+"    q: "+q);//+"    pheeN: "+pheeN);
        BigInteger pheeN = n.subtract(p).subtract(q).add(BigInteger.ONE); //n-p-q+1
        BigInteger d = e.modInverse(pheeN);
        long endTime = currentTimeMillis()-startTime;
        System.out.println("Time: "+endTime+" ms");
        return d;
    }

    public static Key crackKey(Key publicKey){
        BigInteger n = publicKey.getN();
        BigInteger e = publicKey.getSpecial();
        BigInteger d = findD(n,e);
        Key privateKey = new Key(n,d);
        privateKey.printData();
        return privateKey;
    }


    public static void testGeneratorKeyCrack(){
        KeyGenerator keyGenerator = new KeyGenerator();
        Key[] keyPair = keyGenerator.produceKeyPair(54);
        Key publicKey = keyPair[0];
        Key privateKey = keyPair[1];
        crackKey(publicKey);
        privateKey.printData();
    }

    public static void testCanvasKeyCrack(){
        canvasCrackKey("50;835462715998297;73904438462551","89472864579878");
        canvasCrackKey("52;2701991620205419;2264180884674139","1031586900598663");
        canvasCrackKey("54;10476226632250553;878641215431059","1889936955106840");
        canvasCrackKey("56;57037472973154127;44633497289840189","44211460751424128");
        canvasCrackKey("58;203604704001977071;24554964057381863","76866191333607412");
        canvasCrackKey("60;579381566544856613;167626854986927251","257650070150933055");
        canvasCrackKey("62;2477125918244560757;1647627837496842041","1280219177944417002");
        canvasCrackKey("64;12666585948510574693;5751627713795123193","10701638942616915576");
        canvasCrackKey("66;53023821617559758477;40173719767177597903","48773975292560770112");
        canvasCrackKey("68;202232873723180674259;166535864112064027571","81621752924763879555");
        canvasCrackKey("70;942414678972247890257;748019844845171100337","409246786736656515242");
        canvasCrackKey("72;3130175103922026038381;246430842548413740787","2762683390798502897701");
        canvasCrackKey("76;43010193124618253126413;40861084162841179217731","19128745270276496883229");
        canvasCrackKey("80;744813552889676769041729;81898409786902340978333","532644044824224318248323");
        canvasCrackKey("84;12085709249038196304769177;5175928022096557160776071","1896587237665195275134549");
        canvasCrackKey("88;212370024095541198997518451;91640614716751572000760311","16639047210048981740410585");
        canvasCrackKey("92;4409408066659334619578007403;3993527718086145251847766891","2884418219346183226913485012");
        canvasCrackKey("96;41580433960332781796464352561;31946326301966296255958935705","3292397558350395213664709721");
        canvasCrackKey("100;644584770272762414000846637491;170528653750239160444941920491","619827331026687406295834516634");
        canvasCrackKey("104;11814131484279439364337297351659;6458286268002026785464273378359","6532168760678600944767404900634");
        canvasCrackKey("108;279580230883801352370508762343179;183415748560434834260835135712033","85195487382025105880672647096992");
        canvasCrackKey("112;3725351794123521491760526311959821;3650349237608419336134765349449517","2776370150054397686940055013765986");
        canvasCrackKey("116;54658127883858187205368597419999113;53097108976140456798262266822619063","212639136068899626838214441071891");
        canvasCrackKey("120;935412228367556503878872397351176113;841896309535750716044069956762073081","672603344780150657802481120350378175");
    }


    public static void main(String[] args) {
        processMyKeys();
        processClassKeys();
        testCanvasKeyCrack();
//        testGeneratorKeyCrack();
//        Factorer factorer = new Factorer(10000);
//        BigInteger n = BigInteger.valueOf(99999971).multiply(BigInteger.valueOf(99999989));
//        BigInteger[] factorPair = factorer.dixonFactor(n);
//        System.out.println(factorPair[0]+"      "+factorPair[1]);

    }
}
