package fullcare.backend.config;

import fullcare.backend.global.State;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListStringToEnumConverter implements Converter<List<String>, List<State>> {
    @Override
    public List<State> convert(List<String> source) {
        List<State> states = new ArrayList<>();
        for (String s : source) {
            states.add(State.valueOf(s.toUpperCase()));
        }
        return states;
    }
}