package com.scrive.interview;

import com.scrive.interview.api.IText5;

public class App {
    public static void main(String[] args) throws Exception {
        var handler = new ITextHandler();
        LambdaUtils.registerProcessor(arg -> handler.processPdf(arg.getPayload(), arg.getiTextVersion()));
        LambdaUtils.handleInputBatch();
    }
}
