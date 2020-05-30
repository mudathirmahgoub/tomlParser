package edu.uiowa.cvc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Main
{
    public static void main(String args[])
    {
        try
        {
            Map<String, Argument> argumentMap = CVCTomlOptions.parseTomlFile("arith_options.toml");
            System.out.println(argumentMap);

//            String json = IOUtils.toString(inputStream, Charset.defaultCharset());
//            inputStream.close();
//
//            ObjectMapper mapper = new ObjectMapper();
//            cvcArguments = mapper.readValue(json,
//                    new TypeReference<HashMap<String, Argument>>(){});
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
