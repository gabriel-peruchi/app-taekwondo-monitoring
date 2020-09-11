package com.example.apptaekwondomonitoring.services;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.apptaekwondomonitoring.interfaces.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;

public class BluetoothConnection implements Serializable {

    private static final String TAG = "AppTaekwondoMonitoring";
    private static final UUID UUID_RFCOMM = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private Handler handler;


    private ConnectedThread connectedThread;
    private BluetoothSocket bluetoothSocket;

    public BluetoothConnection(BluetoothDevice device, Handler handler) {
        this.device = device;
        this.handler = handler;
    }

    public void connect() throws IOException {
        bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID_RFCOMM);
        bluetoothSocket.connect();

        connectedThread = new ConnectedThread(bluetoothSocket);
        connectedThread.start();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void close() throws IOException {
        connectedThread.cancel();
    }

    public void write(String input) {
        connectedThread.write(input);
    }

    public Boolean isConnected() {
        if (bluetoothSocket != null) {
            return bluetoothSocket.isConnected();
        }
        return false;
    }

    private class ConnectedThread extends Thread implements Serializable {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        private byte[] mmBuffer; // armazenamento mmBuffer para o fluxo

        ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Obtenha os fluxos de entrada e saída; usando objetos temporários porque
            // streams de membros são finais.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Ocorreu um erro ao criar fluxo de entrada", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Ocorreu um erro ao criar fluxo de saida", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes retornados de read ()

            // Continue ouvindo o InputStream até que ocorra uma exceção.
            while (true) {
                try {
                    // Leia a partir do InputStream.
                    numBytes = mmInStream.read(mmBuffer);

                    String data = new String(mmBuffer, 0, numBytes);

                    // Envie os bytes obtidos para a atividade da IU.
                    Message readMsg = handler.obtainMessage(
                            Constants.MESSAGE_READ, numBytes, -1,
                            data);

                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "O stream de entrada foi desconectado", e);
                    break;
                }
            }
        }

        // Chame isso da atividade principal para enviar dados para o dispositivo remoto.
        void write(String input) {
            try {

                byte[] bytes = input.getBytes();

                mmOutStream.write(bytes);

                // Compartilhe a mensagem enviada com a atividade da IU.
                Message writtenMsg = handler.obtainMessage(
                        Constants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Ocorreu um erro ao enviar dados", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(Constants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Não foi possível enviar dados para o outro dispositivo");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Não foi possível fechar o soquete de conexão", e);
            }
        }
    }
}
