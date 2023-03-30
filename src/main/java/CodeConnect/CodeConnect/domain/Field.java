package CodeConnect.CodeConnect.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.mapping.Array;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 관심 분야
 */

@Converter
public class Field implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}
