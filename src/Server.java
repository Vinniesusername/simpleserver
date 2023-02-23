import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.KeyStore;

public class Server
{
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
        String keyStorePath =  "D:\\SPS\\keystore\\spsclient"; //TODO change on server
        char[] keystorePassword = null;
        char[] keyPassword = null;
        /*
        try // get passwords to keystore for server from file
        {
            File info = new File("C:\\Users\\Vinnie\\Documents\\simpleserver\\passwords.txt");
            BufferedReader reader = new BufferedReader(new FileReader(info));
            String[] passwords = new String[2];
            for(int i = 0; i < 2; i++) //read in passwords from file. change to database later
            {
                passwords[i] = reader.readLine();

            }
            keystorePassword = "testclient".toCharArray(); //undo this
            keyPassword = "testkey".toCharArray();

        }
        catch (Exception e)
        {
            System.out.println("problem with I/O on ks passwords");
            e.printStackTrace();
        }
        */

        keystorePassword = "testclient".toCharArray(); //undo this
        keyPassword = "testkey".toCharArray();
        KeyStore keystore;
        try
        {
            //key manager
            keystore = KeyStore.getInstance("pkcs12");
            keystore.load(new FileInputStream(keyStorePath), keystorePassword);
            KeyManagerFactory keyfact = KeyManagerFactory.getInstance("PKIX");
            keyfact.init(keystore, keyPassword);

            //trust manager
            TrustManagerFactory trstfact = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trstfact.init(keystore);
            
            SSLContext context = SSLContext.getInstance(protocols[0]);

            context.init(keyfact.getKeyManagers(), trstfact.getTrustManagers(), null);
            SSLServerSocketFactory ssf = context.getServerSocketFactory();
            SSLServerSocket s   = (SSLServerSocket) ssf.createServerSocket(PORT);
            while(running) //main loop to accept new clients and start threads
            {
                SSLSocket client = (SSLSocket) s.accept();
                //client.setEnabledProtocols(protocols);
                //client.setEnabledCipherSuites(ciphers);

                //test code to view enabled configs remove later
                String[] temp = client.getEnabledCipherSuites();
                String[] temp2 = client.getEnabledProtocols();
                for(int i =0; i < temp.length; i++)
                    System.out.println(temp[i]);
                for(int i =0; i < temp2.length; i++)
                    System.out.println(temp2[i]);
                // end test code

                ServerThread st = new ServerThread(client);
                Thread t = new Thread(st);
                t.start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Problem with keystore");
            e.printStackTrace();
            status = -1;
        }

        return status;

    }

}