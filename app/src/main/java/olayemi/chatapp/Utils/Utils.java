package olayemi.chatapp.Utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

    public static final String HOST_URI = "tcp://broker.hivemq.com:1883";
    public static final String CONNECTION_FAILED = "Connection Failed...";
    public static final String CONNECTION_SUCCESSFUL = "Connection Successful...";
    public static final String CONNECTION_SUCCESSFULLY = "Connection Successfully";
    public static final String CONNECTED = "Connected...";
    public static final String MESSAGE_DELIVERED= "Message Delivered Successfully...";
    public static final String DELETED_SUCCESSFULLY= "Deleted Successfully...";
    public static final String CONNECT= "Connect...";
    public static final String CANCEL= "Cancel";
    public static final String CONNECTION_DISCONNECTED= "Disconnected Successfully...";


    public static Point getWindowSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


}
