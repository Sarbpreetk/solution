package com.scrive.interview;

import com.scrive.interview.api.IText5;
import com.scrive.interview.api.IText7;

public class ITextHandler {
  public String processPdf(String input) {
    String output = input;
    if (!IText5.isNewStylePdf(input)) {
      output = IText5.convertToNewFormat(input);
    }
    output = IText5.processPdf(output);
    output = IText5.seal(output);
    return output;
  }

  /**
   * To decide which iTextApi methods be called based on the version.
   * @param input
   * @param version
   * @return
   */
  public String processPdf(String input, String version) {
    String output = input;
    if (version != null && !version.isEmpty()) {
      switch (version) {
        case "5":
          return processPdf(input);
        case "7":
          IText7 iText7 = new IText7();
          String licenseKey = System.getenv().get("ITEXT7_LICENSE_KEY");
          iText7.init(licenseKey);
          output = iText7.processAndSeal(input);
          return output;
        default:
          output = "unknown backend specified";
          throw new RuntimeException(output);

      }

    }

    return processPdf(input);
  }
}
