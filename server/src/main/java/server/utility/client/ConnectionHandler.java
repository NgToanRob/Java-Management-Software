package server.utility.client;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.utility.Outputer;
import server.App;
import server.Server;
import server.utility.CommandManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Handles user connection.
 */
public class ConnectionHandler implements Runnable {
    private final Server server;
    private final Socket clientSocket;
    private final CommandManager commandManager;
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private final ExecutorService cachedThreadPool =   Executors.newCachedThreadPool();


    public ConnectionHandler(Server server, Socket clientSocket, CommandManager commandManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
    }

    /**
     * Main handling cycle. The server receives a request from the client, processes it, and sends a response to the client
     */
    @Override
    public void run() {
        Request userRequest = null;
        Response responseToUser = null;
        boolean stopFlag = false;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                /*
                TODO: Tạo một class Runnale (hoặc Callable) mới để đọc Object
                 */
                userRequest = (Request) clientReader.readObject();

                // Phaỉ đợi processRequest xong rồi mới lấy đc response
                HandleRequestTask handleRequestTask = new HandleRequestTask(userRequest, commandManager);
                /* Có thể dùng 2 cách để tạo new Thread:
                    - Tạo new Runnable (implement bên dưới thông qua class HandleRequestTask)
                    - Có tạo thể tạo Callable để không phải dùng join()????
                 */
                responseToUser = forkJoinPool.invoke(new HandleRequestTask(userRequest, commandManager));

                App.logger.info("Request '" + userRequest.getCommandName() + "' processed.");
                Response finalResponseToUser = responseToUser;

                if (!cachedThreadPool.submit(() -> {
                    try {
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                        return true;
                    } catch (IOException exception) {
                        Outputer.printerror("An error occurred while sending data to the client!");
                        App.logger.error("An error occurred while sending data to the client!");
                    }
                    return false;
                }).get()) break;
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT
                    && responseToUser.getResponseCode() != ResponseCode.CLIENT_EXIT);
            if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT)
                stopFlag = true;
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("An error occurred while reading the received data!");
            App.logger.error("An error occurred while reading the received data!");
        } catch (CancellationException | ExecutionException | InterruptedException exception) {
            Outputer.println("A multithreading error occurred while processing the request!");
            App.logger.warn("A multithreading error occurred while processing the request!");
        } catch (IOException exception) {
            Outputer.printerror("Unexpected termination of connection with the client!");
            App.logger.warn("Unexpected termination of connection with the client!");
        } finally {
            try {
                cachedThreadPool.shutdown();
                clientSocket.close();
                Outputer.println("The client is disconnected from the server.");
                App.logger.info("The client is disconnected from the server.");
            } catch (IOException exception) {
                Outputer.printerror("An error occurred while trying to terminate the connection with the client!");
                App.logger.error("An error occurred while trying to terminate the connection with the client!");
            }
            if (stopFlag) server.stop();
            server.releaseConnection();
        }
    }
    
}
