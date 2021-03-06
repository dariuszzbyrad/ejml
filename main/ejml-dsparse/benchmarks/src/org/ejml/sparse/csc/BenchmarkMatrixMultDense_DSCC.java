/*
 * Copyright (c) 2009-2020, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ejml.sparse.csc;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.dense.row.RandomMatrices_DDRM;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Peter Abeles
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@State(Scope.Benchmark)
@Fork(value = 2)
public class BenchmarkMatrixMultDense_DSCC {

    @Param({"3000"})
    private int dimension;

    @Param({"10000"})
    private int elementCount;

    DMatrixSparseCSC A;
    DMatrixRMaj B = new DMatrixRMaj(1, 1);
    DMatrixRMaj C = new DMatrixRMaj(1, 1);

    @Setup
    public void setup() {
        Random rand = new Random(2345);
        A = RandomMatrices_DSCC.rectangle(dimension, dimension, elementCount, rand);
        B = RandomMatrices_DDRM.rectangle(dimension, dimension, -1, 1, rand);
        C = B.create(dimension, dimension);
    }

    @Benchmark public void mult() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void multAdd() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void multTransA() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void multAddTransA() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void multTransB() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void multAddTransB() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void multTransAB() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void multAddTransAB() { CommonOps_DSCC.multAdd(A, B, C); }
    @Benchmark public void invert() { CommonOps_DSCC.invert(A, C); }

    public static void main( String[] args ) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkMatrixMultDense_DSCC.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
