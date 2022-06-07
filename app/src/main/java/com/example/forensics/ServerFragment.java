package com.example.forensics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;

public class ServerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ServerSocketThread serverSocketThread;
    static int PORT = 8080;

    private String mParam1;
    private String mParam2;

    public ServerFragment() {
        // Required empty public constructor
    }

    public static ServerFragment newInstance(String param1, String param2) {
        ServerFragment fragment = new ServerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView ipAddress, jport, jdir;
        Switch jswitch;
        Button jselectbtn;
        LottieAnimationView server;
        String IP_ADDRESS;
        int rqCode = 1;

        super.onViewCreated(view, savedInstanceState);

        ipAddress = view.findViewById(R.id.xipdevice);
        jport = view.findViewById(R.id.xport);
        jdir = view.findViewById(R.id.xdir);
        jswitch = view.findViewById(R.id.xswitch);
        server = view.findViewById(R.id.bt_animation);
        jselectbtn = view.findViewById(R.id.xselectbtn);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Forensics", Context.MODE_PRIVATE);
        String DEFAULT_IP = getDefaultIP();
        IP_ADDRESS = sharedPref.getString("Address", DEFAULT_IP);
        PORT = sharedPref.getInt("Port", 8080);
        Log.e("ADDDREES", IP_ADDRESS);
        Log.e("PORT", ""+PORT);

        if(MainActivity.directory!=null) jdir.append(""+ MainActivity.directory.getName());

        jselectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                getActivity().startActivityForResult(intent, rqCode);
            }
        });

        jswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    if (MainActivity.directory == null) {
                        Snackbar.make(view, "Selecciona el directorio", Snackbar.LENGTH_LONG).show();
                        jswitch.setChecked(false);
                        return;
                    }
                    serverSocketThread = new ServerSocketThread(PORT);
                    serverSocketThread.start();

                    ipAddress.append(" " + IP_ADDRESS);
                    jport.append(" " + PORT);

                    server.playAnimation();
                } else{
                    serverSocketThread.closeSocket();
                    server.cancelAnimation();
                    ipAddress.setText("Dirección IP:");
                    jport.setText("Puerto:");
                }
            }
        });
    }

    private String getDefaultIP() {
        WifiManager wm = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}