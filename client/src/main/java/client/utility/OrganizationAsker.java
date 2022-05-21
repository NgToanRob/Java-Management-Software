package client.utility;

import common.data.*;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.utility.Outputer;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks user a marine's value.
 */


public class OrganizationAsker {
    private Scanner userScanner;
    private boolean fileMode;

    public OrganizationAsker(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    /**
     * Set asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Set asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    /**
     * Asks a user the organization's name.
     *
     * @return Organization's name.
     * @throws IncorrectInputInScriptException If script is running and something
     *                                         goes wrong.
     */
    public String askName() throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                Outputer.println("Enter name:");
                Outputer.print(Outputer.PS2);
                name = userScanner.nextLine().trim();
                if (fileMode)
                    Outputer.println(name);
                if (name.equals(""))
                    throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("The name is not recognized!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("The name cannot be empty!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Asks a user the organization's X coordinate.
     *
     * @return Organization's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something
     *                                         goes wrong.
     */
    public int askX() throws IncorrectInputInScriptException {
        String strX;
        int x;
        while (true) {
            try {
                Outputer.println("Enter X coordinate:");
                Outputer.print(Outputer.PS2);
                strX = userScanner.nextLine().trim();
                if (fileMode)
                    Outputer.println(strX);
                x = Integer.parseInt(strX);
                if (x < Organization.MIN_X)
                    throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("The X coordinate is not recognized!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("The X coordinate must be represented by a number!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException e) {
                Outputer.printerror("The X coordinate cannot exceed" + Organization.MIN_X + "!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks a user the organization's Y coordinate.
     *
     * @return Organization's Y coordinate.
     * @throws IncorrectInputInScriptException If script is running and something
     *                                         goes wrong.
     */
    public Float askY() throws IncorrectInputInScriptException {
        String strY;
        float y;
        while (true) {
            try {
                Outputer.println("Enter Y coordinate: ");
                Outputer.print(Outputer.PS2);
                strY = userScanner.nextLine().trim();
                if (fileMode)
                    Outputer.println(strY);
                y = Float.parseFloat(strY);
                if (y > Organization.MAX_Y)
                    throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("The Y coordinate is not recognized!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("The Y coordinate cannot exceed " + Organization.MAX_Y + "!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("The Y coordinate must be represented by a number!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Asks a user the organization's coordinates.
     *
     * @return Organization's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something
     *                                         goes wrong.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        int x;
        float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the organization's annual turnover.
     *
     * @return organization's annual turnover.
     * @throws IncorrectInputInScriptException If script is running and something
     *                                         goes wrong.
     */
    public long askAnnualTurnover() throws IncorrectInputInScriptException {
        String strAnnualTurnover;
        long annualTurnover;
        while (true) {
            try {
                Outputer.println("Enter annual turnover: ");
                Outputer.print(Outputer.PS2);
                strAnnualTurnover = userScanner.nextLine().trim();
                if (fileMode)
                    Outputer.println(strAnnualTurnover);
                annualTurnover = Long.parseLong(strAnnualTurnover);
                if (annualTurnover < Organization.MIN_ANNUAL_TURNOVER)
                    throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Annual turnover is not recognized!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Annual turnover must be greater than zero!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Health should be represented by a number!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return annualTurnover;
    }

    /**
     * Asks a user the organization's type.
     * @return organization's type.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public OrganizationType askOrganizationType() throws IncorrectInputInScriptException {
        String strType;
        OrganizationType type;
        while (true) {
            try {
                Outputer.println("Organization type list - " + OrganizationType.nameList());
                Outputer.println("Enter organization type: ");
                Outputer.print(Outputer.PS2);
                strType = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strType);
                if (strType.equals("")){
                    throw new MustBeNotEmptyException();
                } else type = OrganizationType.valueOf(strType.toUpperCase());

                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("The organization type is not recognized!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("The type is not in the list!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            } catch (MustBeNotEmptyException e) {
                Outputer.printerror("The organization type cannot be empty!");
            }
        }
        return type;
    }

    /**
     * Asks a user the organization chapter's name.
     *
     * @return Chapter's name.
     * @throws IncorrectInputInScriptException If script is running and something
     *                                         goes wrong.
     */
    public String askStreet() throws IncorrectInputInScriptException {
        String street;
        while (true) {
            try {
                Outputer.println("Enter street:");
                Outputer.print(Outputer.PS2);
                street = userScanner.nextLine().trim();
                if (fileMode)
                    Outputer.println(street);
                if (street.equals("")) street =null;
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("The street is not recognized!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return street;
    }

    public String askZipCode() throws IncorrectInputInScriptException {
        String street;
        while (true) {
            try {
                Outputer.println("Enter ZipCode:");
                Outputer.print(Outputer.PS2);
                street = userScanner.nextLine().trim();
                if (fileMode)
                    Outputer.println(street);
                if (street.equals(""))
                    throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("The ZipCode is not recognized!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("The ZipCode cannot be empty!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return street;
    }

    /**
     * Ask the user for the street and zip code
     *
     * @return Organization's address.
     */
    public Address askOfficialAddress() throws IncorrectInputInScriptException {
        String street = askStreet();
        String ZipCode = askZipCode();
        return new Address(street, ZipCode);
    }

    /**
     * Asks a user a question.
     *
     * @return Answer (true/false).
     * @param question A question.
     * @throws IncorrectInputInScriptException If script is running and something
     *                                         goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (Y/N):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(Outputer.PS2);
                answer = userScanner.nextLine().trim().toUpperCase();
                if (fileMode)
                    Outputer.println(answer);
                if (!answer.equals("Y") && !answer.equals("N"))
                    throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Answer not recognized!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("The answer must be represented by 'Y' or 'N'!");
                if (fileMode)
                    throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return answer.equals("Y");
    }

    @Override
    public String toString() {
        return "Asker (helper class for queries to the user)";
    }
}
