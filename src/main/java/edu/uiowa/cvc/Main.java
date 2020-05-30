package edu.uiowa.cvc;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class Main
{
    public static void main(String args[])
    {
        try
        {
            Map<String, Argument> argumentMap = CVCTomlOptions.parseTomlFile("arith_options.toml");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(argumentMap);
            System.out.println(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
