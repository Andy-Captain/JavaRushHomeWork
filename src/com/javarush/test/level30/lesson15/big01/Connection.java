package com.javarush.test.level30.lesson15.big01;


import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements Closeable {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;


    //Constructor
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }


    public void send(Message message) throws IOException {                             ///????

        synchronized (out) {
            out.writeObject(message);
            out.flush();

        }

    }


    public Message receive() throws IOException, ClassNotFoundException {                 ///????

        Message message;

        synchronized (in) {
            message = (Message)in.readObject();
            return message;
        }

    }


    public SocketAddress getRemoteSocketAddress() {

        return socket.getRemoteSocketAddress();
    }


    public void close() throws IOException {

        in.close();
        out.close();
        socket.close();
    }



}
/*
���! � ������ ������� ������ ������ �������?!! � ��������� ����� ����� ������ ��� �������??!!
����� ������ ����� ������� �����������(�������) ��������� ��� �������?
� ��� ������� ���:
private void messageToObjectOutput(Message mes) throws IOException {
ObjectOutputStream oos = new ObjectOutputStream(pout);
synchronized(mes) {
oos.writeObject(mes);
oos.flush();
pout.flush();
}
}
pout ��� PipedOutputStream

� �������� : ObjectInputStream ois = new ObjectInputStream(pin);
Message mes = (Message)ois.readObject(); pin PipedInputStream
 */

/*
������ � ����� Connection:

5.3.	����� void send(Message message) throws IOException. �� ������ ����������
(�������������) ��������� message � ObjectOutputStream. ���� ����� �����
���������� �� ���������� �������. ����������, ����� ������ � ������
ObjectOutputStream ���� �������� ������ ����� ������� � ������������ ������
�������, ��������� �������� ����� ���������� ������. ��� ���� ������ ������
������ Connection �� ������ ���� �������������.


5.4.	����� Message receive() throws IOException, ClassNotFoundException. �� ������ ������
(���������������) ������ �� ObjectInputStream. ������ ���, ����� �������� ������
�� ����� ���� ������������ ������� ����������� ��������, ��� ���� ����� ������
������ ������ Connection �� �����������.

5.5.	����� SocketAddress getRemoteSocketAddress(), ������������ ��������� �����
��������� ����������.
5.6.	����� void close() throws IOException, ������� ������ ��������� ��� ������� ������.
����� Connection ������ ������������� ��������� Closeable.
 */