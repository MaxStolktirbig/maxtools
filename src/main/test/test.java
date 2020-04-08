import mx.helper.tools.IAC2020.AuthenticationHelper;
import mx.helper.tools.communication.Logger;
import mx.helper.tools.communication.SystemMessage;

import java.net.MalformedURLException;

public class test {
    public static void main(String[] args){
        SystemMessage.finishedTaskMessage();
        Logger.off();
        SystemMessage.exceptionMessage(new MalformedURLException("not a url"));
    }
}
