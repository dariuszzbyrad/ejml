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

package org.ejml.dense.row.decomposition.hessenberg;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.RandomMatrices_DDRM;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@State(Scope.Benchmark)
@Fork(value = 2)
public class BenchmarkDecompositionHessenberg_DDRM {
    //    @Param({"100", "500", "1000", "5000", "10000"})
    @Param({"1000"})
    public int size;

    public DMatrixRMaj S, A;

    TridiagonalDecompositionHouseholder_DDRM tridiagonal = new TridiagonalDecompositionHouseholder_DDRM();

    @Setup
    public void setup() {
        Random rand = new Random(234);

        S = RandomMatrices_DDRM.symmetric(size, -1, 1, rand);
    }

    @Benchmark
    public void tridiagonal() {
        DMatrixRMaj A = tridiagonal.inputModified() ? S.copy() : S;
        tridiagonal.decompose(A);
    }

    public static void main( String[] args ) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkDecompositionHessenberg_DDRM.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
