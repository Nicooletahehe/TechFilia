package com.example.proiectdam.asyncTask;

public class HandlerMessage<R> implements Runnable {

    //sunt constante si nu vor mai fi modificate
    //sa ne asiguram ca o data creat acest mesaj, nu ii mai permit sa isi schimbe referinta
    //o data trimise niste informatii, nu mai pot fi modificabile
    //mesajul nu poate fi dinamic
    private final R result;
    private final Callback<R> mainThreadOperation;

    public HandlerMessage(R result, Callback<R> mainThreadOperation) {
        this.result = result;
        this.mainThreadOperation = mainThreadOperation;
    }

    @Override
    public void run() {
        //impingem resultatul primit catre mainThread
        mainThreadOperation.runResultOnUiThread(result);
    }
}
