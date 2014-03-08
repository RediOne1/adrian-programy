import java.security.Security;
//add the provider package

import org.bouncycastle.jce.provider.BouncyCastleProvider;
 
public class Test {
    public static void main(String[] args) {
 
        //add at runtime the Bouncy Castle Provider
    	//the provider is available only for this application
    	Security.addProvider(new BouncyCastleProvider());
 
    	//BC is the ID for the Bouncy Castle provider;
        if (Security.getProvider("BC") == null){
            System.out.println("Bouncy Castle provider is NOT available");
        }
        else{
            System.out.println("Bouncy Castle provider is available");
        }
    }
}