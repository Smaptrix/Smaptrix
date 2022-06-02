package ServerClientUtility;


import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.*;


//TODO - Password based key derivation ?

//TODO - Store keys on the server?? Need to encrypt before sending to the server but cant download key?
//TODO - Actually encrypt the username - for now maybe not worth the overhead



//A class that provides the encryption/decryption tools required by the client to make sure that the users' data
//is secure before it is sent to the server
public class UserSecurity {

    User currUser;


    SecretKey key;





    public UserSecurity(User currUser)  {

        this.currUser = currUser;

        //Turns the password into a character array
        //deriveEncryptionKey();


    }

    /*
    public void deriveEncryptionKey() throws InvalidKeySpecException, NoSuchAlgorithmException {

        //Determining the method required for the encryption
        String method = "PBKDF2WithHmacSHA1";



        //Create a keyfactory with the method
        SecretKeyFactory kf = SecretKeyFactory.getInstance(method);

        KeySpec keySpec = new KeySpec(method);
        key = kf.generateSecret(keySpec);

        System.out.println(key.getEncoded());


    }

*/


    /**
     * Encrypts the users username
     * @return the hex value of the encrypted string
     */
   /*
    public String encryptUsername() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        /*
        //Turns the string into bytes for the encryption
        byte[] input = string.getBytes(StandardCharsets.UTF_8);


        //Create the cipher
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        //Bytes to store the key and the IV
        byte[] keyBytes = new byte[256];
        byte[] ivBytes= new byte[cipher.getBlockSize()];


        //Create a key generator
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);


        //Generate the key and initialisation vector
        SecretKey key = keyGen.generateKey();
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        //Pass the key and iv to the cipher
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encrypted = new byte[cipher.getOutputSize(input.length)];

        int enc_len = cipher.update(input, 0, input.length, encrypted, 0);

        enc_len += cipher.doFinal(encrypted, enc_len);


        */

            /*
        //Creating a salt
        Random r = new SecureRandom();
        byte[] salt = new byte[20];
        r.nextBytes(salt);


        String method = "AES";


        Cipher cipher = Cipher.getInstance(method);

        cipher.init(Cipher.ENCRYPT_MODE, key);





        byte[] encrypted = cipher.doFinal(currUser.getUsername().getBytes(StandardCharsets.UTF_8));


        String encryptedString = Utils.bytesToHex(encrypted);

        System.out.println(encryptedString);

        return encryptedString;

    }


*/

    //TODO - Salt to slow down dictionary attacks

    public String hashPassword(){



        //Hash the password because we never want to undo it
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;

        String saltedPass = currUser.getPassword() + currUser.getSalt();


        byte[] hashPass = digest.digest(saltedPass.getBytes(StandardCharsets.UTF_8));


        return Utils.bytesToHex(hashPass);
    }


    //Creates a random salt value
    //Used to create new users
    public static String generateSalt(){

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return Utils.bytesToHex(salt);

    }



    //Hashes a provided string with a provided salt
    public static String hashThis(String pass, String salt) {

        //Hash the password because we never want to undo it
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;

        String saltedPass = pass + salt;



        byte[] hashPass = digest.digest(saltedPass.getBytes(StandardCharsets.UTF_8));


        return Utils.bytesToHex(hashPass);
    }

}