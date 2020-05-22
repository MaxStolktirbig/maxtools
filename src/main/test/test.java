import mx.helper.tools.DataTool;
import mx.helper.tools.communication.Logger;
import mx.helper.tools.communication.SystemMessage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class test {
    public static void main(String[] args){
        File settings = new File("src/main/test/resources/settings.json");
        System.out.println(settings.exists());
        try {
            JSONObject jsonObject = DataTool.convertJsonFileToJSONObject(settings);
            System.out.println(jsonObject.toString());
        } catch (IOException e){
            SystemMessage.stacktraceOn();
            SystemMessage.exceptionMessage(e);
        }

//        List<Integer> numberlist = new ArrayList<>();
//        for(int i = 0; i<3;i++) {
//            numberlist.set(i, i+1);
//        }
//
//        String teststring = "hellp/there";
//        teststring.split("/");
//        HashMap<Integer, Object> testMap = new HashMap<>();
//        testMap.put(1, "test");
//        testMap.put(2, 32);
//        SystemMessage.finishedTaskMessage(teststring);
//        Logger.off();
//        SystemMessage.exceptionMessage(new MalformedURLException("not a url"));
    }
}
