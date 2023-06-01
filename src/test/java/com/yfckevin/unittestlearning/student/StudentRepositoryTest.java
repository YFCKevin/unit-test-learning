package com.yfckevin.unittestlearning.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Order(1)
    @Test
    void itShouldCheckIfStudentExistsEmail() {
        // given
        String email = "kevin@gmail.com";
        Student student = new Student(
                "Kevin",
                "kevin@gmail.com",
                Gender.MALE);
        underTest.save(student);

        // when
        boolean excepted = underTest.selectExistsEmail(email);

        // then
        assertThat(excepted).isTrue();
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({
            "kevin@gmail.com,false",
            "yyy@gmail.com,false",
            "xxx@gmail.com,false"
    })
    void itShouldCheckIfStudentEmailDoesNotExists(String email, Boolean excepted) {
        // given
//        String email = "kevin@gmail.com";

        // when
        boolean doesExisted = underTest.selectExistsEmail(email);

        // then
        assertThat(doesExisted).isEqualTo(excepted);
    }
}
