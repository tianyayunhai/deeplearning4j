/*
 * ******************************************************************************
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/


// automatically generated by the FlatBuffers compiler, do not modify

package org.nd4j.graph;

import com.google.flatbuffers.BaseVector;
import com.google.flatbuffers.BooleanVector;
import com.google.flatbuffers.ByteVector;
import com.google.flatbuffers.Constants;
import com.google.flatbuffers.DoubleVector;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.FloatVector;
import com.google.flatbuffers.IntVector;
import com.google.flatbuffers.LongVector;
import com.google.flatbuffers.ShortVector;
import com.google.flatbuffers.StringVector;
import com.google.flatbuffers.Struct;
import com.google.flatbuffers.Table;
import com.google.flatbuffers.UnionVector;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class FlatGraph extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_25_2_10(); }
  public static FlatGraph getRootAsFlatGraph(ByteBuffer _bb) { return getRootAsFlatGraph(_bb, new FlatGraph()); }
  public static FlatGraph getRootAsFlatGraph(ByteBuffer _bb, FlatGraph obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public FlatGraph __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public long id() { int o = __offset(4); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public org.nd4j.graph.FlatVariable variables(int j) { return variables(new org.nd4j.graph.FlatVariable(), j); }
  public org.nd4j.graph.FlatVariable variables(org.nd4j.graph.FlatVariable obj, int j) { int o = __offset(6); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int variablesLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public org.nd4j.graph.FlatVariable.Vector variablesVector() { return variablesVector(new org.nd4j.graph.FlatVariable.Vector()); }
  public org.nd4j.graph.FlatVariable.Vector variablesVector(org.nd4j.graph.FlatVariable.Vector obj) { int o = __offset(6); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public org.nd4j.graph.FlatNode nodes(int j) { return nodes(new org.nd4j.graph.FlatNode(), j); }
  public org.nd4j.graph.FlatNode nodes(org.nd4j.graph.FlatNode obj, int j) { int o = __offset(8); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int nodesLength() { int o = __offset(8); return o != 0 ? __vector_len(o) : 0; }
  public org.nd4j.graph.FlatNode.Vector nodesVector() { return nodesVector(new org.nd4j.graph.FlatNode.Vector()); }
  public org.nd4j.graph.FlatNode.Vector nodesVector(org.nd4j.graph.FlatNode.Vector obj) { int o = __offset(8); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public org.nd4j.graph.IntPair outputs(int j) { return outputs(new org.nd4j.graph.IntPair(), j); }
  public org.nd4j.graph.IntPair outputs(org.nd4j.graph.IntPair obj, int j) { int o = __offset(10); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int outputsLength() { int o = __offset(10); return o != 0 ? __vector_len(o) : 0; }
  public org.nd4j.graph.IntPair.Vector outputsVector() { return outputsVector(new org.nd4j.graph.IntPair.Vector()); }
  public org.nd4j.graph.IntPair.Vector outputsVector(org.nd4j.graph.IntPair.Vector obj) { int o = __offset(10); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public org.nd4j.graph.FlatConfiguration configuration() { return configuration(new org.nd4j.graph.FlatConfiguration()); }
  public org.nd4j.graph.FlatConfiguration configuration(org.nd4j.graph.FlatConfiguration obj) { int o = __offset(12); return o != 0 ? obj.__assign(__indirect(o + bb_pos), bb) : null; }
  public String placeholders(int j) { int o = __offset(14); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int placeholdersLength() { int o = __offset(14); return o != 0 ? __vector_len(o) : 0; }
  public StringVector placeholdersVector() { return placeholdersVector(new StringVector()); }
  public StringVector placeholdersVector(StringVector obj) { int o = __offset(14); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public String lossVariables(int j) { int o = __offset(16); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int lossVariablesLength() { int o = __offset(16); return o != 0 ? __vector_len(o) : 0; }
  public StringVector lossVariablesVector() { return lossVariablesVector(new StringVector()); }
  public StringVector lossVariablesVector(StringVector obj) { int o = __offset(16); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public String trainingConfig() { int o = __offset(18); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer trainingConfigAsByteBuffer() { return __vector_as_bytebuffer(18, 1); }
  public ByteBuffer trainingConfigInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 18, 1); }
  public org.nd4j.graph.UpdaterState updaterState(int j) { return updaterState(new org.nd4j.graph.UpdaterState(), j); }
  public org.nd4j.graph.UpdaterState updaterState(org.nd4j.graph.UpdaterState obj, int j) { int o = __offset(20); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int updaterStateLength() { int o = __offset(20); return o != 0 ? __vector_len(o) : 0; }
  public org.nd4j.graph.UpdaterState.Vector updaterStateVector() { return updaterStateVector(new org.nd4j.graph.UpdaterState.Vector()); }
  public org.nd4j.graph.UpdaterState.Vector updaterStateVector(org.nd4j.graph.UpdaterState.Vector obj) { int o = __offset(20); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public String metadataKeys(int j) { int o = __offset(22); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int metadataKeysLength() { int o = __offset(22); return o != 0 ? __vector_len(o) : 0; }
  public StringVector metadataKeysVector() { return metadataKeysVector(new StringVector()); }
  public StringVector metadataKeysVector(StringVector obj) { int o = __offset(22); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public String metadataValues(int j) { int o = __offset(24); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int metadataValuesLength() { int o = __offset(24); return o != 0 ? __vector_len(o) : 0; }
  public StringVector metadataValuesVector() { return metadataValuesVector(new StringVector()); }
  public StringVector metadataValuesVector(StringVector obj) { int o = __offset(24); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }

  public static int createFlatGraph(FlatBufferBuilder builder,
      long id,
      int variablesOffset,
      int nodesOffset,
      int outputsOffset,
      int configurationOffset,
      int placeholdersOffset,
      int lossVariablesOffset,
      int trainingConfigOffset,
      int updaterStateOffset,
      int metadataKeysOffset,
      int metadataValuesOffset) {
    builder.startTable(11);
    FlatGraph.addId(builder, id);
    FlatGraph.addMetadataValues(builder, metadataValuesOffset);
    FlatGraph.addMetadataKeys(builder, metadataKeysOffset);
    FlatGraph.addUpdaterState(builder, updaterStateOffset);
    FlatGraph.addTrainingConfig(builder, trainingConfigOffset);
    FlatGraph.addLossVariables(builder, lossVariablesOffset);
    FlatGraph.addPlaceholders(builder, placeholdersOffset);
    FlatGraph.addConfiguration(builder, configurationOffset);
    FlatGraph.addOutputs(builder, outputsOffset);
    FlatGraph.addNodes(builder, nodesOffset);
    FlatGraph.addVariables(builder, variablesOffset);
    return FlatGraph.endFlatGraph(builder);
  }

  public static void startFlatGraph(FlatBufferBuilder builder) { builder.startTable(11); }
  public static void addId(FlatBufferBuilder builder, long id) { builder.addLong(0, id, 0L); }
  public static void addVariables(FlatBufferBuilder builder, int variablesOffset) { builder.addOffset(1, variablesOffset, 0); }
  public static int createVariablesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startVariablesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addNodes(FlatBufferBuilder builder, int nodesOffset) { builder.addOffset(2, nodesOffset, 0); }
  public static int createNodesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startNodesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addOutputs(FlatBufferBuilder builder, int outputsOffset) { builder.addOffset(3, outputsOffset, 0); }
  public static int createOutputsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startOutputsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addConfiguration(FlatBufferBuilder builder, int configurationOffset) { builder.addOffset(4, configurationOffset, 0); }
  public static void addPlaceholders(FlatBufferBuilder builder, int placeholdersOffset) { builder.addOffset(5, placeholdersOffset, 0); }
  public static int createPlaceholdersVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startPlaceholdersVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addLossVariables(FlatBufferBuilder builder, int lossVariablesOffset) { builder.addOffset(6, lossVariablesOffset, 0); }
  public static int createLossVariablesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startLossVariablesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addTrainingConfig(FlatBufferBuilder builder, int trainingConfigOffset) { builder.addOffset(7, trainingConfigOffset, 0); }
  public static void addUpdaterState(FlatBufferBuilder builder, int updaterStateOffset) { builder.addOffset(8, updaterStateOffset, 0); }
  public static int createUpdaterStateVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startUpdaterStateVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addMetadataKeys(FlatBufferBuilder builder, int metadataKeysOffset) { builder.addOffset(9, metadataKeysOffset, 0); }
  public static int createMetadataKeysVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startMetadataKeysVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addMetadataValues(FlatBufferBuilder builder, int metadataValuesOffset) { builder.addOffset(10, metadataValuesOffset, 0); }
  public static int createMetadataValuesVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startMetadataValuesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endFlatGraph(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }
  public static void finishFlatGraphBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedFlatGraphBuffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public FlatGraph get(int j) { return get(new FlatGraph(), j); }
    public FlatGraph get(FlatGraph obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

