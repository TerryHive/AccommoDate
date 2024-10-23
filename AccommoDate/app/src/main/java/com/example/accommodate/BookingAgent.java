package com.example.accommodate;

import android.util.Log;

import com.example.accommodate.global.Action;
import com.example.accommodate.global.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.example.accommodate.global.*;

public class BookingAgent {
    private final String MASTER_IP;
    private final int MASTER_PORT;
    private final ExecutorService executorService;

    public BookingAgent(String master) {
        MASTER_IP = master.split(":")[0];
        MASTER_PORT = Integer.parseInt(master.split(":")[1]);
        executorService = Executors.newFixedThreadPool(4); // You can adjust the pool size as needed
    }
    @SuppressWarnings("unchecked")
    public ArrayList<Room> search(Filter filters) {
        Message request = new Message (0, Action.SEARCH, filters);
        Future<Object> futureResponse = sendToMasterAsync(request);

        try {
            Object response = futureResponse.get();
            if (response != null)
                return (ArrayList<Room>) response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String book(Pair<String, DateRange> room) {
        Message request = new Message(0, Action.BOOK, room);
        Future<Object> futureResponse = sendToMasterAsync(request);

        try {
            Object response = futureResponse.get();
            if (response == null)
               return ("Booking was unsuccessful! Please, try again!");
            else
                return((String) response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String review(Pair<String, Integer> star) {
        Message request = new Message(0, Action.REVIEW, star);
        Future<Object> futureResponse = sendToMasterAsync(request);

        try {
            Object response = futureResponse.get();
            if (response == null)
                return ("Your review could not be entered! Please, try again!");
            else
                return ((String) response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Future<Object> sendToMasterAsync(Message request) {
        return executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return sendToMaster(request);
            }
        });
    }
    private Object sendToMaster(Message request) {
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Object response = null;

        try {
            requestSocket = new Socket(MASTER_IP, MASTER_PORT);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            /* handshake with master */
            out.writeObject("CLIENT: Hello, MASTER!\n");
            out.flush();
            String handshake = (String) in.readObject();
            System.out.println(handshake);

            out.writeObject(request);
            out.flush();

            response = in.readObject();
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            System.err.println("An unexpected interruption occurred while trying to send data to Master!");
        } catch (ClassNotFoundException e) {
            System.err.println("Class was not found!");
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (requestSocket != null) requestSocket.close();
            } catch (IOException ioException) {
                System.err.println("Unable to close stream instances in client!");
            }
        }
        return response;
    }
}