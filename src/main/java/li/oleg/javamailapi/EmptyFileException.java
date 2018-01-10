package li.oleg.javamailapi;

import java.io.*;

public class EmptyFileException extends Exception{
    EmptyFileException(File file){
        super(String.format("File %1$S is empty",file.getAbsolutePath()));
    }
}
