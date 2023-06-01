package com.yfckevin.unittestlearning.student;

import com.yfckevin.unittestlearning.student.exception.BadRequestException;
import com.yfckevin.unittestlearning.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
//    private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    @Test
    void canGetAllStudents() {
        // when
        underTest.getAllStudents();

        // then
        verify(studentRepository).findAll();

    }

    @Test
    void canAddStudent() {
        // given
        Student student = new Student(
                "Kevin",
                "kevin@gmail.com",
                Gender.MALE);

        // when
        underTest.addStudent(student);

        // then
        // 設定創建ArgumentCaptor指定型別為Student.class，可用來測試save()方法中要帶入的參數，類似於Student.class
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        // 利用capture()將抓到的參數放入ArgumentCaptor內，用於verify
        verify(studentRepository).save(studentArgumentCaptor.capture());
        // 取出上述放入的值，用於assert
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken(){
        // given
        Student student = new Student("Kevin",
                "kevin@gmail.com",
                Gender.MALE);

        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);

        // then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Email " + student.getEmail() + " taken");

        // studentRepository.save(student) 避免作用到這句
        verify(studentRepository, never()).save(any());
    }

    @Test
    void canDeleteStudent() {
        // given
        Long studentId = 1L;
        Mockito.when(studentRepository.existsById(studentId)).thenReturn(true);

        // when
        underTest.deleteStudent(studentId);

        // then
        Mockito.verify(studentRepository).deleteById(studentId);
    }


    @Test
    void willThrowWhenIdDoesNotExist() {
        // given
        Long studentId = 1L;
        given(studentRepository.existsById(studentId)).willReturn(false);

        // then
        assertThatThrownBy(() -> underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Student with id " + studentId + " does not exists");

        verify(studentRepository, never()).deleteById(studentId);
    }
}