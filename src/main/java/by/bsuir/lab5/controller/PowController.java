package by.bsuir.lab5.controller;

import by.bsuir.lab5.model.CallableWithReturnAndArguments;
import com.google.common.collect.Maps;
import com.hubspot.jinjava.Jinjava;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;


@RestController
public class LabController {

    private static final String PUSH = "push";
    private static final String GENERATE_LESS = "generate less";
    private static final String GENERATE_GREATER = "generate greater";
    private static final String CLEAR = "clear";
    private static final String MIDDLE = "middle value";
    private static final Random random = new Random();

    private List<Integer> listToGenerate = new LinkedList<>();
    private Map<String, CallableWithReturnAndArguments<String, Integer>> choicesHandles;
    private Jinjava templateRender = new Jinjava();

    public LabController() {

        choicesHandles = new HashMap<>();

        choicesHandles.put(
                PUSH, (args) -> {
                        listToGenerate.add(args[0]);
                        return listToGenerate.toString();

                }
        );

        choicesHandles.put(
                CLEAR, (args) -> {
                    listToGenerate.clear();
                    return "cleared";
                }
        );

    }


    @GetMapping("/test")
    public String test(Map<String, Object> model) {
        return "test";
    }

    @PostMapping("/")
    @ResponseBody
    public String resolveChoice(@RequestParam("submit") String typeOfGeneration, @RequestParam("number") Integer number) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("detail", choicesHandles.get(typeOfGeneration).call(number));
        return templateRender.render(getTemplate("index.html"), map);
    }
    @PostMapping("/reverseString")
    @ResponseBody
    public String inverseString(@RequestParam("text") String text, @RequestParam(value = "tailRecurse", defaultValue = "false") Boolean useTaleRecurse)
    {
        Map<String, Object> map = Maps.newHashMap();
        if (useTaleRecurse)
            map.put("detail", reverseString("", text, text.length()));
        else
            map.put("detail", new StringBuffer(text).reverse().toString());
        return templateRender.render(getTemplate("index.html"), map);
    }

    private String getTemplate(String templateName) {
        try {
            FileReader reader = new FileReader(
                    "src/main/resources/templates/" + templateName
            );
            StringBuilder templateString = new StringBuilder();

            char[] symbol = new char[256];
            int readBytes;
            while((readBytes = reader.read(symbol)) > 0)
                for (int i = 0; i < readBytes; i++)
                    templateString.append(symbol[i]);

            return templateString.toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "{'detail': 'internal server error: page not found'}";
        }  catch (IOException e) {
            e.printStackTrace();
            return "{'detail': 'internal server error: page cannot be read'}";
        }
    }

    private String reverseString(String accumulator,String source, int size)
    {
        if (size == 0)
            return accumulator;
        return reverseString(accumulator.concat(source.substring(size - 1, size)), source, size - 1);
    }
}


