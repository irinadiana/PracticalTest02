package ro.pub.cs.systems.pdsd.practicaltest02;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends Activity {

	
	private EditText portEditText = null;
    private Button start = null;

    // Client widgets
    private EditText op1 = null;
    private EditText op2 = null;
    private TextView result = null;
    private Button add = null;
    private Button mul = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;
    
    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = portEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }
	
    private GetRezAdd getrezAdd = new GetRezAdd();
    private class GetRezAdd implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort    = portEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            Integer oper1 = Integer.parseInt(op1.getText().toString());
            Integer oper2 = Integer.parseInt(op2.getText().toString());
            String operatie = "add";
            result.setText("");

            clientThread = new ClientThread(clientAddress,Integer.parseInt(clientPort),operatie, oper1, oper2, result);
            clientThread.start();
        }
    }
    
    private GetRezMul getrezMul = new GetRezMul();
    private class GetRezMul implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort    = portEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            Integer oper1 = Integer.parseInt(op1.getText().toString());
            Integer oper2 = Integer.parseInt(op2.getText().toString());
            String operatie = "mul";
            result.setText("");

            clientThread = new ClientThread(clientAddress,Integer.parseInt(clientPort),operatie, oper1, oper2, result);
            clientThread.start();
        }
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_main);
		
		portEditText = (EditText)findViewById(R.id.port_edit_text);
        start = (Button)findViewById(R.id.start_button);
        start.setOnClickListener(connectButtonClickListener);

        op1 = (EditText)findViewById(R.id.op1);
        op2 = (EditText)findViewById(R.id.op2);
        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(getrezAdd);
        mul = (Button)findViewById(R.id.mul);
        mul.setOnClickListener(getrezMul);
        result = (TextView)findViewById(R.id.rez);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
