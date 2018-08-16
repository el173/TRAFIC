import jxl.write.WriteException;
import org.opencv.core.Core;
import tw.edu.sju.ee.commons.nativeutils.NativeUtils;

import java.io.IOException;

public class Application {
    static {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            try {
                NativeUtils.loadLibraryFromJar("opencv_java310");
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    public static void main(String[] args) throws IOException, WriteException, InterruptedException {
        GUI gui = new GUI();
        gui.init();
    }
}
