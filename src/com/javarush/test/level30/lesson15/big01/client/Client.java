package com.javarush.test.level30.lesson15.big01.client;

import com.javarush.test.level30.lesson15.big01.Connection;
import com.javarush.test.level30.lesson15.big01.ConsoleHelper;
import com.javarush.test.level30.lesson15.big01.Message;
import com.javarush.test.level30.lesson15.big01.MessageType;

import java.io.IOException;

public class Client {

    protected Connection connection;
    private volatile boolean clientConnected = false;


    /** PSVM Client **/
    public static void main(String[] args) {

        Client client = new Client();
        client.run();
    }


    /** Methods **/
    /** run **/
    public void run() {

        // ��������� ����� �������� ����� � ������� ������ getSocketThread
        SocketThread socketThread = getSocketThread();
        // �������� ��������� ����� ��� daemon, ��� ����� ��� ����, ����� ��� ������
        // �� ��������� ��������������� ����� ��������� �������������.
        socketThread.setDaemon(true);
        // ��������� ��������������� �����
        socketThread.start();

        // ��������� ������� ����� �������, ���� �� �� ������� ����������� �� ������� ������
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage("�����");
            return;
        }

        //����� ����, ��� ����� �������� �����������, ������� �������� clientConnected
        if (clientConnected) {
            ConsoleHelper.writeMessage("���������� �����������. ��� ������ �������� ������� 'exit'.");

            //�������� ��������� � ������� ���� ������ ���������. ���� ����� ������� ������� 'exit', �� ����� �� �����
            String msg;
            while (!(msg = ConsoleHelper.readString()).equals("exit") && clientConnected) {

                // ����� ������� ����������, ���� ����� shouldSentTextFromConsole()
                // ���������� true, ������� ��������� ����� � ������� ������  sendTextMessage().
                if (shouldSentTextFromConsole()) {
                    sendTextMessage(msg);
                }
            }
        }
        else {
            ConsoleHelper.writeMessage("��������� ������ �� ����� ������ �������.");
        }
    }


    /** ������ ��������� ���� ������ ������� � ������� ��������� ��������**/
    protected String getServerAddress() {

        ConsoleHelper.writeMessage("������� ����� �������: ");
        return ConsoleHelper.readString();
    }


    /** ������ ����������� ���� ����� ������� � ���������� ��� **/
    protected int getServerPort() {

        ConsoleHelper.writeMessage("������� ���� �������: ");
        return ConsoleHelper.readInt();
    }


    /** ������ ����������� � ���������� ��� ������������ **/
    protected String getUserName() {

        ConsoleHelper.writeMessage("������� ��� ������������: ");
        return ConsoleHelper.readString();
    }


    protected boolean shouldSentTextFromConsole() {

        return true;
    }


    /** ������ ��������� � ���������� ����� ������ ������ SocketThread **/
    protected SocketThread getSocketThread() {

        return new SocketThread();
    }


    /**  ������� ����� ��������� ���������, ��������� ���������� ����� � ���������� ��� ������� ����� ���������� connection **/
    protected void sendTextMessage(String text) {

        try {
            connection.send(new Message(MessageType.TEXT, text));

        } catch (IOException e) {
            ConsoleHelper.writeMessage("������ ��������");
            clientConnected = false;
        }
    }


    /** SocketThread **/
    public class SocketThread extends Thread {

        /** Methods **/

        /** ������ �������� ����� message � ������� **/
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }


        /** ������ �������� � ������� ���������� � ���, ��� �������� � ������ userName ������������� � ���� **/
        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage("�������� " + userName + " ������������� � ����");
        }


        /**  ������ �������� � �������, ��� �������� � ������ userName ������� ��� **/
        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage("�������� " + userName + " ������� ���");
        }


        /** ������������� �������� ���� clientConnected ������ Client � ������������ �
         ���������� ����������.
            ��������� (���������� ���������) �������� ����� ������ Client **/
        protected void notifyConnectionStatusChanged(boolean clientConnected) {

            Client.this.clientConnected = clientConnected;

            synchronized (Client.this) {
                Client.this.notify();
            }
        }
    }
}

