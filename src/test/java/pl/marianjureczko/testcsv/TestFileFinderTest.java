package pl.marianjureczko.testcsv;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TestFileFinderTest {

    @Test
    void shouldFindSampleTestCsvFile() {
        //given
        TestFileFinder sut = new TestFileFinder();

        //when
        Optional<String> testFile = sut.findTestFile(new File("."));

        //then
        assertThat(testFile.get()).isEqualTo("sample_test.csv");
    }
}