package edu.uiowa.cvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CVCTomlOptions
{

    public static Map<String, Argument> getAllArguments(String cvc4Directory) throws IOException
    {
        List<String> tomlFiles = getAllTomlFiles(new File(cvc4Directory));

        Map<String, Argument> allArguments = new HashMap<>();

        for (String file : tomlFiles)
        {
            Map<String, Argument> arguments = CVCTomlOptions.parseTomlFile(file);
            allArguments.putAll(arguments);
        }

        // add default arguments and override existing ones
        allArguments.putAll(getDefaultArguments());

        return allArguments;
    }

    public static Map<String, Argument> parseTomlFile(String tomlFile) throws IOException
    {
        String content = new String(Files.readAllBytes(Paths.get(tomlFile)));
        Toml toml = new Toml().read(content);
        List<HashMap<String, Object>> options = toml.getList("option");
        Map<String, Argument> argumentMap = new HashMap<>();
        if (options == null)
        {
            return argumentMap;
        }
        for (HashMap<String, Object> option : options)
        {
            // skip undocumented category
            String name = (String) option.get("long");
            if (name == null)
            {
                name = (String) option.get("name");
            }
            // remove = from the name
            if (name.contains("="))
            {
                name = name.split("=")[0];
            }
            Argument argument = new Argument();
            argument.category = (String) option.get("category");
            if(argument.category.equals("undocumented"))
            {
                continue;
            }
            argument.prefix = "--";
            argument.description = (String) option.get("help");
            Object defaultValue = option.get("default");
            if(defaultValue != null)
            {
                argument.defaultValue = ((String) defaultValue).toLowerCase().replace("_", "-");
            }
            Object modes = option.get("mode");
            if (modes == null)
            {
                String type = (String) option.get("type");
                if (type.contains("bool"))
                {
                    argument.type = "bool";
                    argument.defaultValue = "";
                    Boolean alternate = (Boolean) option.get("alternate");
                    if(alternate != null)
                    {
                        argument.alternate = alternate;
                    }

                }
                else if (type.contains("int"))
                {
                    argument.type = "int";
                    getIntegerMinMax(argument, option);
                }
                else if (type.contains("double"))
                {
                    argument.type = "float";
                    getFloatMinMax(argument, option);
                }
                else
                {
                    // skip if the type is not supported in the web
                    continue;
                }
            }
            else
            {
                argument.type = "list";
                HashMap hashMap = (HashMap) modes;
                argument.allowedValues = new ArrayList<>();
                for (Object value : hashMap.values())
                {
                    String allowedValue = (String) ((HashMap) ((ArrayList) value).get(0)).get("name");
                    String help = (String) ((HashMap) ((ArrayList) value).get(0)).get("help");
                    argument.allowedValues.add(allowedValue);
                }
                argument.allowedValues.add("help");
            }
            argumentMap.put(name, argument);
        }
        return argumentMap;
    }

    private static void getIntegerMinMax(Argument argument, HashMap<String, Object> option)
    {
        try
        {
            argument.min = Integer.parseInt((String) option.get("minimum"));
        }
        catch (NumberFormatException e)
        {
            argument.min = 0;
        }
    }

    private static void getFloatMinMax(Argument argument, HashMap<String, Object> option)
    {
        try
        {
            argument.min = Float.parseFloat((String) option.get("minimum"));
        }
        catch (NumberFormatException e)
        {
            argument.min = 0;
        }
    }

    public static Map<String, Argument> getDefaultArguments() throws IOException
    {
        InputStream inputStream =
                CVCTomlOptions.class.getResourceAsStream("/defaultArguments.json");

        String input = new String(inputStream.readAllBytes(), Charset.defaultCharset());
        inputStream.close();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Argument> arguments = mapper.readValue(input,
                new TypeReference<HashMap<String, Argument>>()
                {
                });
        return arguments;
    }

    private static List<String> getAllTomlFiles(File directory)
    {
        List<String> tomlFiles = new ArrayList<>();

        for (File file : directory.listFiles())
        {
            if (file.isDirectory())
            {
                tomlFiles.addAll(getAllTomlFiles(file));
            }
            else
            {
                if (file.getName().endsWith(".toml"))
                {
                    tomlFiles.add(file.getAbsolutePath());
                }
            }
        }

        return tomlFiles;
    }
}
