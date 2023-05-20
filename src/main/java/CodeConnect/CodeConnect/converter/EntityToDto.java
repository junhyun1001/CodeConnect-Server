package CodeConnect.CodeConnect.converter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityToDto {
    public static <T, R> List<R> mapListToDto(List<T> sourceList, Function<T, R> converter) {
        return sourceList.stream()
                .map(converter)
                .collect(Collectors.toList());
    }
}
