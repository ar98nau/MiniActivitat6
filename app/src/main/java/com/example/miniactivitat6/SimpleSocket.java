package com.example.miniactivitat6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleSocket extends AppCompatActivity {

    private static final String CLASSTAG = SimpleSocket.class.getSimpleName();

    private EditText ipAddress;
    private EditText port;
    private EditText socketInput;
    private TextView socketOutput;

    @Override
    public void onCreate(final Bundle icicle) {
        Button socketButton;

        super.onCreate(icicle);
        setContentView(R.layout.simple_socket);

        this.ipAddress = findViewById(R.id.socket_ip);
        this.port = findViewById(R.id.socket_port);
        this.socketInput = findViewById(R.id.socket_input);
        this.socketOutput = findViewById(R.id.socket_output);
        socketButton = findViewById(R.id.socket_button);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        socketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socketOutput.setText("");
                try {

                    InetAddress serverAddr = InetAddress.getByName(ipAddress.getText().toString());
                    String output = callSocket(serverAddr, port.getText().toString(), socketInput.getText().toString());
                    socketOutput.setText(output);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String callSocket(final InetAddress ad, final String port, final String socketData) {

        Socket socket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        String output = null;

        try {
            socket = new Socket(ad, Integer.parseInt(port));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String input = socketData;
            writer.write(input + "\n", 0, input.length() + 1);
            writer.flush();

            output = reader.readLine();
            Log.d("NetworkExplorer", " " + SimpleSocket.CLASSTAG + " output - " + output);

            writer.write("EXIT\n", 0, 5);
            writer.flush();

        } catch (IOException e) {
            Log.d("NetworkExplorer", " " + SimpleSocket.CLASSTAG + " IOException calling socket", e);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
            }
            try {
                if (writer != null)
                    reader.close();
            } catch (IOException e) {
            }
            try {
                if (writer != null)
                    socket.close();
            } catch (IOException e) {
            }
        }
        return output;
    }
}
