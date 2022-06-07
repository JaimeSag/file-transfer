package com.example.forensics;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ComponentActivity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread{
    String dstAddress, dstpath;
    int dstPort;
    private volatile int state;

    ClientThread(String address, int port, String path) {
        dstAddress = address;
        dstPort = port;
        dstpath = path;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(dstAddress, dstPort);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int filesCount = dis.readInt();

            Log.e("CANTIDAD CLIENTE", "" + filesCount);
            File[] files = new File[filesCount];
            DataInputStream is = new DataInputStream(socket.getInputStream());

            for (int i=0; i<filesCount; i++){
                String fileName = dis.readUTF();
                Long fileSize = dis.readLong();

                files[i] = new File(Environment.getExternalStorageDirectory(), dstpath + "/" + fileName);

                FileOutputStream fos = new FileOutputStream(files[i]);
                byte[] buffer = new byte[4*1024];
                int bytesRead=0;

                while (fileSize>0 && (bytesRead = is.read(buffer, 0, (int)Math.min(buffer.length, fileSize)))!=-1) {
                    fos.write(buffer, 0, bytesRead);
                    fileSize -= bytesRead;
                }
                Log.i("File copied", fileName);
                fos.close();
            }
            socket.close();
            state = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getValueState() {
        return state;
    }
}
