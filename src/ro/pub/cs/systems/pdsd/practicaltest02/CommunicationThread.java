package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;

import android.util.Log;


public class CommunicationThread extends Thread{
	
	private Socket socket;
	private ServerThread serverThread;
	
	public CommunicationThread(ServerThread serverThread,Socket socket) {
		this.socket = socket;
		this.serverThread = serverThread;
	}
	
	@Override
    public void run() {
        if (socket != null) {
            try {
            	BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client !");
                    String primit = bufferedReader.readLine();
                    String [] cuv = primit.split(",");
                    String operatie = cuv[0];
                    Integer op1 = Integer.parseInt(cuv[1]);
                    Integer op2 = Integer.parseInt(cuv[2]);
                    Integer result = 0;
                    if (op1 >= Integer.MAX_VALUE || op2 >= Integer.MAX_VALUE)
                    	result = null;
                    if (operatie.equals("add") == true)
                    	result = op1 + op2;
                    else if (operatie.equals("mul") == true)
                		result = op1 * op2;
                    printWriter.println(result);
                    printWriter.flush();
                    Log.i(Constants.TAG, "Am trimis: " + result);
                }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        }}
}
