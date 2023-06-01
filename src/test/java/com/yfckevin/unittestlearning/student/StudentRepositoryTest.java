package com.yfckevin.unittestlearning.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
    @Test
    void itShouldCheckIfStudentEmailDoesNotExists() {
        // given
        String email = "kevin@gmail.com";

        // when
        boolean excepted = underTest.selectExistsEmail(email);

        // then
        assertThat(excepted).isFalse();
    }
}
