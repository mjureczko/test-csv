package pl.marianjureczko.testcsv;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CsvParserTest {

    static Stream<Arguments> csvLines() {
        return Stream.of(
                Arguments.of(
                        "", List.of()
                ),
                Arguments.of(
                        "1683 - odsiecz wiedeńska", List.of(new Question("odsiecz wiedeńska", "1683"))
                ),
                Arguments.of(
                        "3 maj 1791-konstytucja 3-maja ", List.of(new Question("konstytucja 3-maja", "3 maj 1791"))
                ),
                Arguments.of(
                        "1648-1668- panowanie Jana Kazimierza", List.of(new Question("panowanie Jana Kazimierza", "1648-1668"))
                )
        );
    }

    @ParameterizedTest(name = "SHOULD convert {0} to {1}")
    @MethodSource("csvLines")
    void shouldParse(String given, List<Question> expected) {
        //given
        CsvParser csvParser = new CsvParser();

        //when
        List<Question> actual = csvParser.parseCsvContent(List.of(given));

        //then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}