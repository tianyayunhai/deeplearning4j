/*
 *  ******************************************************************************
 *  *
 *  *
 *  * This program and the accompanying materials are made available under the
 *  * terms of the Apache License, Version 2.0 which is available at
 *  * https://www.apache.org/licenses/LICENSE-2.0.
 *  *
 *  *  See the NOTICE file distributed with this work for additional
 *  *  information regarding copyright ownership.
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  * License for the specific language governing permissions and limitations
 *  * under the License.
 *  *
 *  * SPDX-License-Identifier: Apache-2.0
 *  *****************************************************************************
 */

package org.eclipse.deeplearning4j.dl4jcore.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.BaseDL4JTest;
import org.deeplearning4j.exception.DL4JException;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.conf.preprocessor.CnnToFeedForwardPreProcessor;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.nd4j.common.tests.tags.NativeTag;
import org.nd4j.common.tests.tags.TagNames;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@NativeTag
@Tag(TagNames.EVAL_METRICS)
@Tag(TagNames.TRAINING)
@Tag(TagNames.DL4J_OLD_API)
public class TestInvalidConfigurations extends BaseDL4JTest {

    public static MultiLayerNetwork getDensePlusOutput(int nIn, int nOut) {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                        .layer(0, new DenseLayer.Builder().nIn(nIn).nOut(10).build())
                        .layer(1, new OutputLayer.Builder().nIn(10).nOut(nOut).build()).build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        return net;
    }

    public static MultiLayerNetwork getLSTMPlusRnnOutput(int nIn, int nOut) {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                        .layer(0, new LSTM.Builder().nIn(nIn).nOut(10).build())
                        .layer(1, new RnnOutputLayer.Builder().nIn(10).nOut(nOut).build()).build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        return net;
    }

    public static MultiLayerNetwork getCnnPlusOutputLayer(int depthIn, int inH, int inW, int nOut) {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                        .layer(0, new ConvolutionLayer.Builder().nIn(depthIn).nOut(5).build())
                        .layer(1, new OutputLayer.Builder().nOut(nOut).build())
                        .setInputType(InputType.convolutional(inH, inW, depthIn)).build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        return net;
    }

    @Test
    public void testDenseNin0() {
        try {
            getDensePlusOutput(0, 10);
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testDenseNin0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testDenseNout0() {
        try {
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                            .layer(0, new DenseLayer.Builder().nIn(10).nOut(0).build())
                            .layer(1, new OutputLayer.Builder().nIn(10).nOut(10).build()).build();

            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testDenseNout0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testOutputLayerNin0() {
        try {
            getDensePlusOutput(10, 0);
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testOutputLayerNin0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testRnnOutputLayerNin0() {
        try {
            getLSTMPlusRnnOutput(10, 0);
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testRnnOutputLayerNin0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testLSTMNIn0() {
        try {
            getLSTMPlusRnnOutput(0, 10);
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testLSTMNIn0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testLSTMNOut0() {
        try {
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                            .layer(0, new LSTM.Builder().nIn(10).nOut(0).build())
                            .layer(1, new RnnOutputLayer.Builder().nIn(10).nOut(10).build()).build();

            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testLSTMNOut0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testConvolutionalNin0() {
        try {
            getCnnPlusOutputLayer(0, 10, 10, 10);
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testConvolutionalNin0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testConvolutionalNOut0() {
        try {
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                            .layer(0, new ConvolutionLayer.Builder().nIn(5).nOut(0).build())
                            .layer(1, new OutputLayer.Builder().nOut(10).build())
                            .setInputType(InputType.convolutional(10, 10, 5)).build();

            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testConvolutionalNOut0(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }


    @Test
    public void testCnnInvalidConfigPaddingStridesHeight() {
        //Idea: some combination of padding/strides are invalid.

        int depthIn = 3;
        int hIn = 10;
        int wIn = 10;

        //Using kernel size of 3, stride of 2:
        //(10-3+2*0)/2+1 = 7/2 + 1

        try {
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().convolutionMode(ConvolutionMode.Strict)
                            .list()
                            .layer(0, new ConvolutionLayer.Builder().kernelSize(3, 2).stride(2, 2).padding(0, 0).nOut(5)
                                            .build())
                            .layer(1, new OutputLayer.Builder().nOut(10).build())
                            .setInputType(InputType.convolutional(hIn, wIn, depthIn)).build();

            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testCnnInvalidConfigPaddingStridesHeight(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testCnnInvalidConfigOrInput_SmallerDataThanKernel() {
        //Idea: same as testCnnInvalidConfigPaddingStridesHeight() but network is fed incorrect sized data
        // or equivalently, network is set up without using InputType functionality (hence missing validation there)

        int depthIn = 3;
        int hIn = 10;
        int wIn = 10;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                        .layer(0, new ConvolutionLayer.Builder().kernelSize(7, 7).stride(1, 1).padding(0, 0).nOut(5)
                                        .build())
                        .layer(1, new OutputLayer.Builder().nOut(10).activation(Activation.SOFTMAX).build())
                        .setInputType(InputType.convolutional(hIn, wIn, depthIn)).build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        try {
            net.feedForward(Nd4j.create(3, depthIn, 5, 5));
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testCnnInvalidConfigOrInput_SmallerDataThanKernel(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testCnnInvalidConfigOrInput_BadStrides() {
        //Idea: same as testCnnInvalidConfigPaddingStridesHeight() but network is fed incorrect sized data
        // or equivalently, network is set up without using InputType functionality (hence missing validation there)

        int depthIn = 3;
        int hIn = 10;
        int wIn = 10;

        //Invalid: (10-3+0)/2+1 = 4.5

        MultiLayerConfiguration conf =
                        new NeuralNetConfiguration.Builder().convolutionMode(ConvolutionMode.Strict).list()
                                        .layer(0, new ConvolutionLayer.Builder().kernelSize(3, 3).stride(2, 2)
                                                        .padding(0, 0).nIn(depthIn).nOut(5).build())
                                        .layer(1, new OutputLayer.Builder().nIn(5 * 4 * 4).nOut(10).activation(Activation.SOFTMAX).build())
                                        .inputPreProcessor(1, new CnnToFeedForwardPreProcessor()).build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        try {
            net.feedForward(Nd4j.create(3, depthIn, hIn, wIn));
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testCnnInvalidConfigOrInput_BadStrides(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }



    @Test
    public void testCnnInvalidConfigPaddingStridesWidth() {
        //Idea: some combination of padding/strides are invalid.
        int depthIn = 3;
        int hIn = 10;
        int wIn = 10;

        //Using kernel size of 3, stride of 2:
        //(10-3+2*0)/2+1 = 7/2 + 1

        try {
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().list()
                            .layer(0, new ConvolutionLayer.Builder().kernelSize(2, 3).stride(2, 2).padding(0, 0).nOut(5)
                                            .build())
                            .layer(1, new OutputLayer.Builder().nOut(10).activation(Activation.SOFTMAX).build())
                            .setInputType(InputType.convolutional(hIn, wIn, depthIn)).build();
        } catch (Exception e) {
            fail("Did not expect exception with default (truncate)");
        }

        try {
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().convolutionMode(ConvolutionMode.Strict)
                            .list()
                            .layer(0, new ConvolutionLayer.Builder().kernelSize(2, 3).stride(2, 2).padding(0, 0).nOut(5)
                                            .build())
                            .layer(1, new OutputLayer.Builder().nOut(10).build())
                            .setInputType(InputType.convolutional(hIn, wIn, depthIn)).build();

            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testCnnInvalidConfigPaddingStridesWidth(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test
    public void testCnnInvalidConfigPaddingStridesWidthSubsampling() {
        //Idea: some combination of padding/strides are invalid.
        int depthIn = 3;
        int hIn = 10;
        int wIn = 10;

        //Using kernel size of 3, stride of 2:
        //(10-3+2*0)/2+1 = 7/2 + 1

        try {
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().convolutionMode(ConvolutionMode.Strict)
                            .list()
                            .layer(0, new SubsamplingLayer.Builder().kernelSize(2, 3).stride(2, 2).padding(0, 0)
                                            .build())
                            .layer(1, new OutputLayer.Builder().nOut(10).build())
                            .setInputType(InputType.convolutional(hIn, wIn, depthIn)).build();

            MultiLayerNetwork net = new MultiLayerNetwork(conf);
            net.init();
            fail("Expected exception");
        } catch (DL4JException e) {
            System.out.println("testCnnInvalidConfigPaddingStridesWidthSubsampling(): " + e.getMessage());
        } catch (Exception e) {
            log.error("",e);
            fail();
        }
    }

    @Test()
    public void testCnnInvalidKernel() {
        assertThrows(IllegalStateException.class, () -> {
            new ConvolutionLayer.Builder().kernelSize(3, 0).build();

        });
    }

    @Test()
    public void testCnnInvalidKernel2() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ConvolutionLayer.Builder().kernelSize(2, 2, 2).build();

        });
    }

    @Test()
    public void testCnnInvalidStride() {
        assertThrows(IllegalStateException.class,() -> {
            new ConvolutionLayer.Builder().kernelSize(3, 3).stride(0, 1).build();

        });
    }

    @Test()
    public void testCnnInvalidStride2() {
        assertThrows(IllegalArgumentException.class,() -> {
            new ConvolutionLayer.Builder().kernelSize(3, 3).stride(1).build();

        });
    }

    @Test()
    public void testCnnInvalidPadding() {
        assertThrows(IllegalArgumentException.class,() -> {
            new ConvolutionLayer.Builder().kernelSize(3, 3).stride(1, 1).padding(-1, 0).build();

        });
    }

    @Test()
    public void testCnnInvalidPadding2() {
        assertThrows(IllegalArgumentException.class,() -> {
            new ConvolutionLayer.Builder().kernelSize(3, 3).stride(1, 1).padding(0, 0, 0).build();

        });
    }

    @Test()
    public void testSubsamplingInvalidKernel() {
        assertThrows(IllegalStateException.class,() -> {
            new SubsamplingLayer.Builder().kernelSize(3, 0).build();

        });
    }

    @Test()
    public void testSubsamplingInvalidKernel2() {
        assertThrows(IllegalArgumentException.class,() -> {
            new SubsamplingLayer.Builder().kernelSize(2).build();

        });
    }

    @Test()
    public void testSubsamplingInvalidStride() {
        assertThrows(IllegalStateException.class,() -> {
            new SubsamplingLayer.Builder().kernelSize(3, 3).stride(0, 1).build();

        });
    }

    @Test()
    public void testSubsamplingInvalidStride2() {
        assertThrows(RuntimeException.class,() -> {
            new SubsamplingLayer.Builder().kernelSize(3, 3).stride(1, 1, 1).build();

        });
    }

    @Test()
    public void testSubsamplingInvalidPadding() {
        assertThrows(IllegalArgumentException.class,() -> {
            new SubsamplingLayer.Builder().kernelSize(3, 3).stride(1, 1).padding(-1, 0).build();

        });
    }

    @Test()
    public void testSubsamplingInvalidPadding2() {
        assertThrows(RuntimeException.class,() -> {
            new SubsamplingLayer.Builder().kernelSize(3, 3).stride(1, 1).padding(0).build();

        });
    }

}
