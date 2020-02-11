import com.moandjiezana.toml.Toml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CVCOptionsParser
{
    public static void main(String args[])
    {
        try
        {
            String content = new String(Files.readAllBytes(Paths.get("arith_options.toml")));
            Toml toml = new Toml().read(content);
            List<HashMap<String, Object>> options = toml.getList("option");
            Map<String, Argument> argumetsMap = new HashMap<>();
            for (HashMap<String, Object> option: options)
            {
                String name = (String) option.get("long");
                Argument argument = new Argument();
                argument.prefix = "--";
                argument.description = (String) option.get("help");
                Object modes = option.get("mode");
                if(modes == null)
                {
                    String type = (String) option.get("type");
                    if(type.contains("bool"))
                    {
                        argument.type = "bool";
                    }
                    if(type.contains("int"))
                    {
                        argument.type = "int";
                    }
                }
                else
                {
                    argument.type = "string";
                    HashMap hashMap = (HashMap) modes;
                    argument.allowedValues = new ArrayList<>();
                    for (Object value: hashMap.values())
                    {
                        String allowedValue = (String) ((HashMap) ((ArrayList) value).get(0)).get("name");
                        String help = (String) ((HashMap) ((ArrayList) value).get(0)).get("help");
                        argument.allowedValues.add(allowedValue);
                    }
                }
                argumetsMap.put(name, argument);
            }

            System.out.println(argumetsMap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
