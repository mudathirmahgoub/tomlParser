package edu.uiowa.cvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;

public class Main
{

    public static final String SEP = File.separator;
    public static final String OUTPUT_DIR = System.getProperty("java.io.tmpdir");
    public static final String DEFAULT_OUTPUT_FILE = "cvcArguments.json";

    public static boolean isValidInputFilePath(String path)
    {
        File cvc4Directory = new File(path);

        return cvc4Directory.exists() && cvc4Directory.canRead() && cvc4Directory.isDirectory();
    }

    public static boolean isValidOutputFilePath(String path) throws IOException
    {
        try
        {
            Paths.get(path);
        }
        catch (InvalidPathException | NullPointerException ex)
        {
            return false;
        }

        File outputFile = new File(path);

        if (outputFile.getParentFile() != null)
        {
            outputFile.getParentFile().mkdirs();
        }

        outputFile.createNewFile();

        return true;
    }

    public static void main(String args[])
    {
        Options options = new Options();
        CommandLineParser commandLineParser = new DefaultParser();

        options.addOption(Option.builder("src").longOpt("source").desc("CVC4 directory").hasArg().build());
        options.addOption(Option.builder("o").longOpt("output").desc("Output file for all CVC4 options in json format").hasArg().build());

        try
        {
            CommandLine command = commandLineParser.parse(options, args);

            if (!command.hasOption("src"))
            {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar tomlParser.jar ", options);
                return;
            }

            String cvc4Directory = command.getOptionValue("src").trim();

            if (!isValidInputFilePath(cvc4Directory))
            {
                throw new Exception("Can not open directory " + cvc4Directory);
            }

            Map<String, Argument> allArguments = CVCTomlOptions.getAllArguments(cvc4Directory);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(allArguments);
            System.out.println(json);

            File outputFile = new File(DEFAULT_OUTPUT_FILE);
            if (command.hasOption("o"))
            {
                if (isValidOutputFilePath(command.getOptionValue("o")))
                {
                    outputFile = new File(command.getOptionValue("o").trim());
                }
            }

            Formatter formatter = new Formatter(outputFile);
            formatter.format("%s", json);
            formatter.close();
        }
        catch (ParseException exception)
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar tomlParser.jar ", options);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
