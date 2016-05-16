package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {

	private String address;
    private int port;
    private int op1;
    private int op2;
    private String operatie;
    private TextView rez;
	
	
	private Socket socket;
	
	public ClientThread(
            String address,
            int port,
            String operatie,
            Integer op1,
            Integer op2,
            TextView data) {
        this.address = address;
        this.port = port;
        this.op1 = op1;
        this.op2 = op2;
        this.rez = rez;
    }
	
	@Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                String deTrimis = operatie+","+op1+","+op2;
                printWriter.println(deTrimis);
                printWriter.flush();
                String data;
                while ((data = bufferedReader.readLine()) != null) {
                    final String finalizeddata = data;
                    rez.post(new Runnable() {
                        @Override
                        public void run() {
                            rez.append(finalizeddata + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
	
}
