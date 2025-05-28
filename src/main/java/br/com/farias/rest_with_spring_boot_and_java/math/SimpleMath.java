package br.com.farias.rest_with_spring_boot_and_java.math;

import br.com.farias.rest_with_spring_boot_and_java.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public class SimpleMath {

    public Double sum(Double numberOne, Double numberTwo) {

        return (numberOne) + (numberTwo);
    }

    public Double subtraction(Double numberOne, Double numberTwo) {

        return (numberOne) - (numberTwo);
    }

    public Double multiplication(Double numberOne, Double numberTwo) {

        return (numberOne) * (numberTwo);
    }

    public Double division(Double numberOne, Double numberTwo) {

        return (numberOne) / (numberTwo);
    }

    public Double average(Double numberOne, Double numberTwo) {
        return ((numberOne) + (numberTwo)) / 2;
    }

    public Double square(Double number) {

        return Math.sqrt((number));
    }
}
