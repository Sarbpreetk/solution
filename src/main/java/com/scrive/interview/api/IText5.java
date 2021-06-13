package com.scrive.interview.api;

public final class IText5 {
  /** in iText3 era all pdfs started with '!echo [{"payload": "Hello World!", "iTextVersion": "6"}] | java -jar target/java-interview.jar */
  public static boolean isNewStylePdf(String input) {
    return input.charAt(0) != '!';
  }

  public static String convertToNewFormat(String input) {
    if (isNewStylePdf(input)) {
      throw new IllegalArgumentException("Cannot convert already new-style pdf");
    }
    return input.substring(1);
  }

  public static String processPdf(String input) {
    return ">>> " + input + " <<<";
  }

  public static String seal(String input) {
    return "Sealed via iText5: [" + input + "]";
  }
}
