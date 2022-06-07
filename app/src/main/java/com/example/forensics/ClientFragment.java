package com.example.forensics;

import static java.lang.Thread.sleep;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientFragment extends Fragment {
    Boolean state = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientFragment newInstance(String param1, String param2) {
        ClientFragment fragment = new ClientFragment();
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
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button jgetfilesbtn, jfilesname;
        EditText jipservet, jpuerto;
        TextView jconnect, jstate;
        LottieAnimationView check;

        File forensicsTransfer = new File(Environment.getExternalStorageDirectory(), "ForensicsTransfer");
        forensicsTransfer.mkdirs();

        super.onViewCreated(view, savedInstanceState);

        jgetfilesbtn = view.findViewById(R.id.xgetfilesbtn);
        jipservet = view.findViewById(R.id.xipservet);
        jpuerto = view.findViewById(R.id.xpuerto);
        jconnect = view.findViewById(R.id.xconnect);
        jstate = view.findViewById(R.id.xstate);
        check = view.findViewById(R.id.check_animation);
        check.cancelAnimation();

        jgetfilesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientThread clientThread = new ClientThread(jipservet.getText().toString(), Integer.parseInt(jpuerto.getText().toString()), forensicsTransfer.getName());
                clientThread.start();

                while(true){
                    if(!clientThread.isAlive()){
                        state = true;
                        break;
                    }
                }

                if(clientThread.getValueState()==0){
                    jconnect.setText("Remitente " + jipservet.getText().toString());
                    jstate.setText("Archivos recibidos");
                    check.playAnimation();
                    jgetfilesbtn.setEnabled(true);
                }
            }
        });
    }
}