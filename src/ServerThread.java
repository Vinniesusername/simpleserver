import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerThread implements Runnable
{
    SSLSocket client;
    PrintWriter out;
    BufferedReader in;
    boolean listen = true;

    public ServerThread(SSLSocket socket)
    {
        this.client = socket;
    }

    @Override
    public void run()
    {
        System.out.println("connection made");

        try
        {
            in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            System.out.println(in);
            System.out.println(this.client.getInputStream().toString());
            out = new PrintWriter(this.client.getOutputStream());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("problem setting up socket i/o");
        }

        while(true) //main client loop
        {
            out.println("TEST SERVER");
            out.flush();
            String q = null;
            try
            {
                System.out.println("Theres data!#!!#");
                q = in.readLine(); //if more than one query is sent at a time we will only care about the last one

                if(q != null)
                {
                    System.out.println(q);
                    String response = dealWith(q);
                    System.out.println(response);
                    out.println(response);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }


    }

    public String dealWith(String query) // 0 = request denied, -1 = null query, -2 idk what you want, positive int = it did what you asked.
    {
        String response = "";
        //queries are expected to be in the format type;id;data0;data1,optional
        int respond = -2;
        if(query == null)
        {
            respond = -1;
        }
        String[] options = query.split(";", 4); // docs explain the format the query is expected in
        int type = Integer.parseInt(options[0]);
        switch (type)
        {
            case 0: //ask for server for magic number
                    response += "87;null;null;null;" + respond;
                    respond = 1;
                    break;


        }
        return response;

    }


}
