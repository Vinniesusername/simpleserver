import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.ServerSocket;
import java.security.KeyStore;

public class Server
{

    private static final String HOST = "localhost"; //change this to server ip if running one
    private static final int PORT = 4422; // TODO: register port
    private static final String[] protocols = new String[]{"TLSv1.3"};
    private static final String[] ciphers = new String[]{"TLS_AES_256_GCM_SHA384", "TLS_AES_128_GCM_SHA256"};



    public static void main(String[] args)
    {
        int status = openServer();

    }


    public static int openServer() //starts server listening for clients. returns status code.
    {
        int status = 0;
        boolean running = true;
        String keyStorePath = "C:\\Users\\Vinnie\\Documents\\simpleserver\\serverkeystore"; //TODO change on server
        char[] keystorePassword = null;
        char[] keyPassword = null;
        try
        {
            File info = new File("C:\\Users\\Vinnie\\Documents\\simpleserver\\passwords.txt");
            BufferedReader reader = new BufferedReader(new FileReader(info));
            String[] passwords = new String[2];
            for(int i = 0; i < 2; i++) //read in passwords from file. change to database later
            {
                passwords[i] = reader.readLine();

            }
            keystorePassword = passwords[0].toCharArray();
            keyPassword = passwords[1].toCharArray();

        }
        catch (Exception e)
        {
            System.out.println("problem with I/O on ks passwords");
            e.printStackTrace();
        }
        KeyStore keystore;
        try
        {
            keystore = KeyStore.getInstance("pkcs12");
            keystore.load(new FileInputStream(keyStorePath), keystorePassword);
            KeyManagerFactory keyfact = KeyManagerFactory.getInstance("pkcs12");
            keyfact.init(keystore, keyPassword);
            SSLContext context = SSLContext.getInstance(protocols[0]);
            context.init(keyfact.getKeyManagers(), null, null); //TODO add trust manager


        }
        catch (Exception e)
        {
            System.out.println("Problem with keystore");
            e.printStackTrace();
        }
        while(running)
        {
            ServerSocketFactory factory = ServerSocketFactory.getDefault();

        }

        return status;

    }

}