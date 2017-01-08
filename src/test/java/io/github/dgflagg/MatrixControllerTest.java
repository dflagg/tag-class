package io.github.dgflagg;

import io.github.dgflagg.model.Matrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dgflagg on 12/30/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MatrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void idShouldReturnIdMatrixOfSizeN() throws Exception {
        int N = 3;
        Matrix matrix = Matrix.buildIdentityMatrix(N);

        this.mockMvc.perform(get("/matrix/id").param("n", N + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void idShouldReturnIdMatrixOfSizeOne() throws Exception {
        int N = 1;
        Matrix matrix = Matrix.buildIdentityMatrix(N);

        this.mockMvc.perform(get("/matrix/id").param("n", N + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void idShouldReturnIdMatrixOfSizeZeroByDefault() throws Exception {
        Matrix matrix = Matrix.buildZeroMatrix(0,0);
        this.mockMvc.perform(get("/matrix/id"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void zeroShouldReturnZeroMatrixOfSizeMxN() throws Exception {
        int M = 2;
        int N = 3;
        Matrix matrix = Matrix.buildZeroMatrix(M, N);

        this.mockMvc.perform(get("/matrix/zero").param("n", N + "").param("m", M + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void zeroShouldReturnZeroMatrixOfSizeOne() throws Exception {
        int M = 1;
        int N = 1;
        Matrix matrix = Matrix.buildZeroMatrix(M, N);

        this.mockMvc.perform(get("/matrix/zero").param("n", N + "").param("m", M + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void zeroShouldReturnZeroMatrixOfSizeZeroByDefault() throws Exception {
        Matrix matrix = Matrix.buildZeroMatrix(0,0);
        this.mockMvc.perform(get("/matrix/zero"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void scalarShouldReturnScalarProductMatrix2x4() throws Exception {
        double SCALAR = 3;
        Matrix operandMatrix = Matrix.csv("src/test/resources/2x4-matrix.csv");
        Matrix resultMatrix = MatrixAlgebra.scalarMultiply(operandMatrix, SCALAR);

        this.mockMvc.perform(get("/matrix/scalar").param("s", SCALAR + "").param("m", operandMatrix.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(resultMatrix.getNumbers()));
    }

    @Test
    public void scalarShouldReturnScalarProductMatrix3x3() throws Exception {
        double SCALAR = 2.3;
        Matrix operandMatrix = Matrix.csv("src/test/resources/3x3-matrix.csv");
        Matrix resultMatrix = MatrixAlgebra.scalarMultiply(operandMatrix, SCALAR);

        this.mockMvc.perform(get("/matrix/scalar").param("s", SCALAR + "").param("m", operandMatrix.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(resultMatrix.getNumbers()));
    }

    @Test
    public void scalarShouldReturnScalarProductMatrix4x2() throws Exception {
        double SCALAR = -2;
        Matrix operandMatrix = Matrix.csv("src/test/resources/4x2-matrix.csv");
        Matrix resultMatrix = MatrixAlgebra.scalarMultiply(operandMatrix, SCALAR);

        this.mockMvc.perform(get("/matrix/scalar").param("s", SCALAR + "").param("m", operandMatrix.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(resultMatrix.getNumbers()));
    }

    @Test
    public void scalarShouldReturnScalarProductMatrixOfScalarOneByDefault() throws Exception {
        Matrix operandMatrix = Matrix.csv("src/test/resources/4x2-matrix.csv");

        this.mockMvc.perform(get("/matrix/scalar").param("m", operandMatrix.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(operandMatrix.getNumbers()));
    }

    @Test
    public void addShouldReturnSumMatrix() throws Exception {
        Matrix m1 = Matrix.csv("src/test/resources/3x3-matrix.csv");
        Matrix m2 = Matrix.buildValueMatrix(3,3,5d);
        Matrix sumMatrix = MatrixAlgebra.add(m1,m2);

        this.mockMvc.perform(get("/matrix/add").param("m1", m1.toString()).param("m2", m2.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(sumMatrix.getNumbers()));
    }

    @Test
    public void subtractShouldReturnDifferenceMatrix() throws Exception {
        Matrix m1 = Matrix.csv("src/test/resources/3x3-matrix.csv");
        Matrix m2 = Matrix.buildValueMatrix(3,3,7d);
        Matrix differenceMatrix = MatrixAlgebra.subtract(m1,m2);

        this.mockMvc.perform(get("/matrix/subtract").param("m1", m1.toString()).param("m2", m2.toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(differenceMatrix.getNumbers()));
    }

}
