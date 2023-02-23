import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerThread implements Runnable
{
    SSLSocket client;
    PrintWriter out;
    BufferedReader in;

    public ServerThread(SSLSocket socket)
    {
        this.client = socket;
    }

    @Override
    public void run()
    {

        try
        {
            in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
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
            String q = "";
            try
            {
                q = in.readLine();

                if(q != null && !q.equals("")) //short circuit to avoid equals running on null string
                {
                    System.out.println("data sent from client");
                    System.out.println(q);
                    String response = dealWith(q);
                    System.out.println("data sent to client");
                    System.out.println(response);
                    out.println(response);
                    out.flush();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                break;
            }

        }
    }
    public String dealWith(String query) // returns a response to the client query
    {
        String response = "";
        //queries are expected to be in the format type;requestID;data0;data1,optional
        int respond = -2;
        if(query == null)
        {
            respond = -1;
            return null;
        }
        String[] options = query.split(";", 5);
        int type = Integer.parseInt(options[0]);
        int requestID = Integer.parseInt(options[1]);
        switch (type) {
            case 0 -> //connection established
                    response += "0;" + String.valueOf(requestID) + ";null;null";
            case 1 -> //ask for magic number
                    response += "1;" + String.valueOf(requestID) +";87;null";
        }
        return response;
    }


}
