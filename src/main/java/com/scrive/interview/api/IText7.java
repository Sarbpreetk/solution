package com.scrive.interview.api;

import static java.util.Objects.requireNonNull;

public class IText7 {
  private boolean licenseOk = false;

  public void init(String licenseKey) {
    requireNonNull(licenseKey);
    System.out.println("Checking license against server ... " + licenseKey);
    try {
      // expensive network call
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // ignore
    }
    licenseOk = true;
  }

  public String processAndSeal(String input) {
    if (!licenseOk) {
      throw new IllegalStateException("License not verified!");
    }
    if (input.startsWith("!")) {
      throw new IllegalArgumentException("'old-style' pdfs are not longer supported!");
    }
    return "Sealed via IText7: [~~~ " + input + " ~~~]";
  }
}
