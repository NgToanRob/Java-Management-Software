package server.utility.client;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.interaction.User;
import server.utility.CommandManager;
import server.utility.PasswordHasher;
import server.utility.ResponseOutputer;

import java.util.concurrent.RecursiveTask;

/**
 * A class for handle request task.
 */
//public class HandleRequestTask extends RecursiveTask<Response> {
public class HandleRequestTask extends RecursiveTask<Response> {

    private final Request request;
    private final CommandManager commandManager;

    public HandleRequestTask(Request request, CommandManager commandManager) {
        this.request = request;
        this.commandManager = commandManager;
    }

// ** Uncoment below lines to implement compute when using RecursiveTask
//
//    @Override
//    protected Response compute() {
//        User hashedUser = new User(
//                request.getUser().getUsername(),
//                PasswordHasher.hashPassword(request.getUser().getPassword())
//        );
//        commandManager.addToHistory(request.getCommandName(), request.getUser());
//        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
//                request.getCommandObjectArgument(), hashedUser);
//        return new Response(responseCode, ResponseOutputer.getAndClear());
//    }

    @Override
    public Response compute() {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword())
        );
        commandManager.addToHistory(request.getCommandName(), request.getUser());
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    /**
     * Executes a command from a request.
     *
     * @param command               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private synchronized ResponseCode executeCommand(String command, String commandStringArgument,
                                                     Object commandObjectArgument, User user) {
        switch (command) {
            case "":
                break;
            case "help":
                if (!commandManager.help(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeById(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.CLIENT_EXIT;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_lower":
                if (!commandManager.removeGreater(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "history":
                if (!commandManager.history(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "average_of_annual_turnover":
                if (!commandManager.averageOfAnnualTurnover(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "count_greater_than_official_address":
                if (!commandManager.countGreaterThanOfficialAddress(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "filter_greater_than_type":
                if (!commandManager.filterGreaterThanType(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "server_exit":
                if (!commandManager.serverExit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.SERVER_EXIT;
            case "login":
                if (!commandManager.login(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "register":
                if (!commandManager.register(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            default:
                ResponseOutputer.appendln("Command '" + command + "' not found. Type 'help' for help.");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
