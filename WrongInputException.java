
/**
   Custom Exception Class WrongInputException
*/


public class WrongInputException extends Exception {
  /**
   * Constructor for custom Exception 
   * Retrieves error message
      @param error message to display for exception
   */
  public WrongInputException(String error)
  {
    super("Invalid option! \n" + error);
  }
}

