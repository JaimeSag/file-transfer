package com.example.forensics;

import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketThread extends Thread{
    ServerSocket serverSocket;
    Socket socket = null;
    int PORT;

    ServerSocketThread(int PORT){
        this.PORT = PORT;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeInt(MainActivity.files.length);

                for (int i = 0; i < MainActivity.files.length; i++) {
                    int bytes = 0;
                    Log.d("Files", "FileName:" +MainActivity.files[i].getName());
                    File file = new File(MainActivity.files[i].getPath());
                    FileInputStream fileInputStream = new FileInputStream(file);

                    String name = MainActivity.files[i].getName();
                    dos.writeUTF(name);

                    long fileSize = MainActivity.files[i].length();
                    dos.writeLong(fileSize);

                    byte[] buffer = new byte[4*1024];
                    while ((bytes=fileInputStream.read(buffer))!=-1){
                        dos.write(buffer,0,bytes);
                        dos.flush();
                    }
                    fileInputStream.close();
                }
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeSocket() {
        try {
            serverSocket.close();
            if(socket!=null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
