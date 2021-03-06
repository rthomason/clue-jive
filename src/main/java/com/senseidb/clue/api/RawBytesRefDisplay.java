package com.senseidb.clue.api;

public class RawBytesRefDisplay extends BytesRefDisplay {

    private RawBytesRefDisplay() {
    }

    @Override
    public BytesRefPrinter getBytesRefPrinter(String field) {
        return BytesRefPrinter.RawBytesPrinter;
    }

    public static RawBytesRefDisplay INSTANCE = new RawBytesRefDisplay();

}
