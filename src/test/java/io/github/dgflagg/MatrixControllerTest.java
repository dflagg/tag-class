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
    public void idMatrixShouldReturnIdMatrixOfSizeN() throws Exception {
        int N = 3;
        Matrix matrix = Matrix.buildIdentityMatrix(N);

        this.mockMvc.perform(get("/idMatrix").param("n", N + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void idMatrixShouldReturnIdMatrixOfSizeOne() throws Exception {
        int N = 1;
        Matrix matrix = Matrix.buildIdentityMatrix(N);

        this.mockMvc.perform(get("/idMatrix").param("n", N + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void idMatrixShouldReturnIdMatrixOfSizeZeroByDefault() throws Exception {
        Matrix matrix = Matrix.buildZeroMatrix(0,0);
        this.mockMvc.perform(get("/idMatrix"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void zeroMatrixShouldReturnZeroMatrixOfSizeMxN() throws Exception {
        int M = 2;
        int N = 3;
        Matrix matrix = Matrix.buildZeroMatrix(M, N);

        this.mockMvc.perform(get("/zeroMatrix").param("n", N + "").param("m", M + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void zeroMatrixShouldReturnZeroMatrixOfSizeOne() throws Exception {
        int M = 1;
        int N = 1;
        Matrix matrix = Matrix.buildZeroMatrix(M, N);

        this.mockMvc.perform(get("/zeroMatrix").param("n", N + "").param("m", M + ""))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void zeroMatrixShouldReturnZeroMatrixOfSizeZeroByDefault() throws Exception {
        Matrix matrix = Matrix.buildZeroMatrix(0,0);
        this.mockMvc.perform(get("/zeroMatrix"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(matrix.getNumbers()));
    }

    @Test
    public void scalarMultShouldReturnScalarProductMatrix() throws Exception {
        double SCALAR = 3;
        Matrix operandMatrix = Matrix.csv("src/test/resources/2x4-matrix.csv");
        Matrix resultMatrix = MatrixAlgebra.scalarMultiply(operandMatrix, SCALAR);

        this.mockMvc.perform(get("/scalarMult").param("s", SCALAR + "").param("m", operandMatrix.getNumbers().toString().replace(" ","")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("*").value(resultMatrix.getNumbers()));
    }

}
