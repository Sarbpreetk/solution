package com.scrive.interview;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class LambdaUtils {
  private static volatile Function<Args, String> processor = args -> null;
  private static volatile Function<String, String> sealer = input -> input;

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static class Args {
    private final String payload;
    private String iTextVersion;


    @JsonCreator
    public Args(@JsonProperty("payload") String payload, @JsonProperty("iTextVersion") String iTextVersion) {
      this.payload = payload;
      this.iTextVersion= iTextVersion;
    }
    public String getPayload() {
      return payload;
    }

    public String getiTextVersion() {
      return iTextVersion;
    }
  }

  public static class Result {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";

    private final String status;

    private final String output;

    public Result(String status, String output) {
      this.status = status;
      this.output = output;
    }

    public static Result success(String output) {
      return new Result(STATUS_SUCCESS, output);
    }

    public static Result error(String output) {
      return new Result(STATUS_ERROR, output);
    }

    public String getStatus() {
      return status;
    }

    public String getOutput() {
      return output;
    }
  }

  public static void registerProcessor(Function<Args, String> lambda) {
    processor = lambda;
  }

  public static void registerSealer(Function<String, String> lambda) {
    sealer = lambda;
  }

  public static Result handleInput(Args args) {
    try {
      String result = processor.apply(args);
      String sealed = sealer.apply(result);

      return Result.success(sealed);
    } catch (RuntimeException e) {
      return Result.error(e.getMessage());
    }
  }

  public static void handleInputBatch() throws IOException {
    try {
      var args = readArgs();
      var results = args.stream()
                      .map(LambdaUtils::handleInput)
                      .collect(Collectors.toList());
      respond(results);
    } catch (IOException e) {
      e.printStackTrace();
      respond(Result.error("Invalid JSON input: " + e.getMessage()));
    }
  }

  private static List<Args> readArgs() throws IOException {
    return OBJECT_MAPPER.readValue(System.in, new TypeReference<List<Args>>() {});
  }

  public static void respond(Object result) throws IOException {
    OBJECT_MAPPER.writeValue(System.out, result);
  }
}
