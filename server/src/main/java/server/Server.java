package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.utility.Outputer;
import server.utility.CommandManager;
import server.utility.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Runs the server.
 */
public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final CommandManager commandManager;
    private boolean isStopped;
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private final Semaphore semaphore;

    public Server(int port, int maxClients, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.semaphore = new Semaphore(maxClients);
    }

    /**
     * The server opens a socket, waits for a client to connect, and then creates a new thread to handle the connection
     */
    public void run() {
        try {
            openServerSocket();
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = connectToClient();
                    ConnectionHandler task = new ConnectionHandler(this, clientSocket, commandManager);
//                    cachedThreadPool.submit(task);
                    Thread readingRequest = new Thread(task);
                    readingRequest.start();

                } catch (ConnectionErrorException exception) {
                    if (!isStopped()) {
                        Outputer.printerror("An error occurred while connecting to the client!");
                        App.logger.error("An error occurred while connecting to the client!");
                    } else break;
                }
            }
            cachedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Outputer.println("The server has ended.");
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("The server cannot be started!");
            App.logger.error("The server cannot be started!");
        } catch (InterruptedException e) {
            Outputer.printerror("An error occurred while shutting down already connected clients!");
        }
    }

    /**
     * It tries to acquire a permit from the semaphore, if it fails, it prints an error message
     */
    public void acquireConnection() {
        try {
            semaphore.acquire();
            App.logger.info("Permission for a new connection has been received.");
        } catch (InterruptedException exception) {
            Outputer.printerror("An error occurred while obtaining permission for a new connection!");
            App.logger.error("An error occurred while obtaining permission for a new connection!");
        }
    }

    /**
     * Release a connection from the pool.
     */
    public void releaseConnection() {
        semaphore.release();
        App.logger.info("Connection disconnect registered.");
    }

    /**
     * Finishes server operation.
     */
    public synchronized void stop() {
        try {
            App.logger.info("Shutting down the server...");
            if (serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            cachedThreadPool.shutdown();
            serverSocket.close();
            Outputer.println("Completing work with already connected clients...");
            App.logger.info("The server has ended.");
        } catch (ClosingSocketException exception) {
            Outputer.printerror("It is not possible to shut down a server that has not yet been started!");
            App.logger.error("It is not possible to shut down a server that has not yet been started!");
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while shutting down the server!");
            Outputer.println("Completing work with already connected clients...");
            App.logger.error("An error occurred while shutting down the server!");
        }
    }

    /**
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            App.logger.info("Server start...");
            serverSocket = new ServerSocket(port);
            App.logger.info("The server is running.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Port '" + port + "' is out of range!");
            App.logger.error("Port '" + port + "' is out of range!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while trying to use the port '" + port + "'!");
            App.logger.error("An error occurred while trying to use the port '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Connecting to client.
     */
    private Socket connectToClient() throws ConnectionErrorException {
        try {
            Outputer.println("Port listening '" + port + "'...");
            App.logger.info("Port listening '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("The connection with the client has been established.");
            App.logger.info("The connection with the client has been established.");
            return clientSocket;
        } catch (IOException exception) {
            throw new ConnectionErrorException();
        }
    }
}
