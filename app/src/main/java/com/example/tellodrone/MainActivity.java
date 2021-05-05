package com.example.tellodrone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Thread Thread1 = null;
    private final String SERVER_IP = "192.168.10.1";
    private final int SERVER_PORT_SEND = 8889;
    private final int SERVER_PORT_RECV = 9000;
    private final int SERVER_PORT_RECV_STATE = 8890;


    private final String TAG = "Drone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_launch = findViewById(R.id.tello_launch);
        Button btn_land = findViewById(R.id.tello_land);
        Button btn_up = findViewById(R.id.tello_up);
        Button btn_down = findViewById(R.id.tello_down);
        Button btn_fwd = findViewById(R.id.tello_fwd);
        Button btn_bwd = findViewById(R.id.tello_bwd);
        Button btn_left = findViewById(R.id.tello_left);
        Button btn_right = findViewById(R.id.tello_right);
        Button btn_rot_left = findViewById(R.id.tello_left_rot);
        Button btn_rot_right = findViewById(R.id.tello_right_rot);

        btn_launch.setOnClickListener(view -> {
            sendtoDrone("takeoff");

        });

        btn_land.setOnClickListener(view -> {
            sendtoDrone("land");

        });

        btn_up.setOnClickListener(view -> {
            sendtoDrone("up 20");

        });

        btn_down.setOnClickListener(view -> {
            sendtoDrone("down 20");

        });

        btn_fwd.setOnClickListener(view -> {
            sendtoDrone("forward 20");

        });

        btn_bwd.setOnClickListener(view -> {
            sendtoDrone("back 20");

        });

        btn_left.setOnClickListener(view -> {
            sendtoDrone("left 20");

        });

        btn_right.setOnClickListener(view -> {
            sendtoDrone("right 20");

        });

        btn_rot_left.setOnClickListener(view -> {
            sendtoDrone("ccw 20");

        });

        btn_rot_right.setOnClickListener(view -> {
            sendtoDrone("cw 20");

        });


        // Run this thread to say tello to accept commands
        recvReplyfromDrone();
        sendtoDrone("command");
        sendtoDrone("speed?");
        sendtoDrone("battery?");
        //recvStatefromDrone();





    }

    private void sendtoDrone(String msg){

        Thread sendDate = new Thread() {
            @Override
            public void run() {

                DatagramSocket socket = null ;

                try {
                    socket = new DatagramSocket() ;

                    InetAddress host = InetAddress.getByName(SERVER_IP);
                    byte [] data = msg.getBytes() ;
                    DatagramPacket packet = new DatagramPacket( data, data.length, host, SERVER_PORT_SEND );
                    socket.send(packet) ;

                    Log.d(TAG, "Packet sent" );
                } catch( Exception e )
                {
                    Log.d(TAG, "Exception");
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                finally
                {
                    if( socket != null ) {
                        socket.close();
                    }
                }
            }
        };
        sendDate.start();

    }

    private void recvStatefromDrone(){

        Thread recvData = new Thread(){
            @Override
            public void run(){

                DatagramSocket mDataGramSocket = null;

                try {
                    mDataGramSocket = new DatagramSocket(SERVER_PORT_RECV_STATE);
                    mDataGramSocket.setReuseAddress(true);
                    mDataGramSocket.setSoTimeout(1000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                String text;

                byte[] message = new byte[1500];
                DatagramPacket p = new DatagramPacket(message, message.length);

                try {


                    while (true) {
                        try {
                            assert mDataGramSocket != null;
                            mDataGramSocket.receive(p);
                            text = new String(message, 0, p.getLength());

                            Log.d(TAG, text);


                        } catch (SocketTimeoutException | NullPointerException e) {
                            // no response received after 1 second.
                            e.printStackTrace();
                            mDataGramSocket.close();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }


        };
        recvData.start();

    }

    private void recvReplyfromDrone(){

        Thread recvData = new Thread(){
            @Override
            public void run(){

                DatagramSocket mDataGramSocket = null;

                try {
                    mDataGramSocket = new DatagramSocket(SERVER_PORT_RECV);
                    mDataGramSocket.setReuseAddress(true);
                    mDataGramSocket.setSoTimeout(1000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                String text;

                byte[] message = new byte[1500];
                DatagramPacket p = new DatagramPacket(message, message.length);

                try {


                    while (true) {
                        try {
                            assert mDataGramSocket != null;
                            mDataGramSocket.receive(p);
                            text = new String(message, 0, p.getLength());

                            Log.d(TAG, text);


                        } catch (SocketTimeoutException | NullPointerException e) {
                            // no response received after 1 second.
                            e.printStackTrace();
                            mDataGramSocket.close();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        };
        recvData.start();

    }

}