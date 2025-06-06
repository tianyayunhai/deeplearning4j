/*
 *  ******************************************************************************
 *  *
 *  *
 *  * This program and the accompanying materials are made available under the
 *  * terms of the Apache License, Version 2.0 which is available at
 *  * https://www.apache.org/licenses/LICENSE-2.0.
 *  *
 *  * See the NOTICE file distributed with this work for additional
 *  * information regarding copyright ownership.
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  * License for the specific language governing permissions and limitations
 *  * under the License.
 *  *
 *  * SPDX-License-Identifier: Apache-2.0
 *  *****************************************************************************
 */

//
// @author raver119@gmail.com
//
#include <array/DataType.h>
#include <array/DataTypeUtils.h>
#include <array/NDArray.h>
#include <exceptions/cuda_exception.h>
#include <execution/ThreadPool.h>
#include <helpers/DebugHelper.h>
#include <loops/legacy_ops.h>
#include <system/Environment.h>
#include <system/op_boilerplate.h>
#include <types/types.h>

#include "helpers/ShapeUtils.h"

namespace sd {

// ----------- Unary Lambda Operations ----------------

template <typename T>
SD_KERNEL void applyLambdaKernel(const void* vx, const sd::LongType* xShapeInfo,
                                 void* vz, const sd::LongType* zShapeInfo,
                                 void* vextraParams) {
// Cast input and output pointers
  auto x = reinterpret_cast<const T*>(vx);
  auto z = reinterpret_cast<T*>(vz);
  auto extraParams = reinterpret_cast<void*>(vextraParams);

// Cache shape information for x buffer
  __shared__ sd::LongType length;
  __shared__ sd::LongType xRank;
  __shared__ const sd::LongType* xShapePtr;
  __shared__ const sd::LongType* xStridePtr;

// Cache shape information for z buffer
  __shared__ sd::LongType zRank;
  __shared__ const sd::LongType* zShapePtr;
  __shared__ const sd::LongType* zStridePtr;

  if (threadIdx.x == 0) {
    length = shape::length(xShapeInfo);

// Cache x shape information
    xRank = shape::rank(xShapeInfo);
    xShapePtr = shape::shapeOf(xShapeInfo);
    xStridePtr = shape::stride(xShapeInfo);

// Cache z shape information
    zRank = shape::rank(zShapeInfo);
    zShapePtr = shape::shapeOf(zShapeInfo);
    zStridePtr = shape::stride(zShapeInfo);
  }
  __syncthreads();

  auto tid = blockIdx.x * blockDim.x + threadIdx.x;
  int totalThreads = gridDim.x * blockDim.x;

  for (sd::LongType i = tid; i < length; i += totalThreads) {
    sd::LongType xCoords[SD_MAX_RANK];
    sd::LongType zCoords[SD_MAX_RANK];
    sd::LongType xOffset;
    sd::LongType zOffset;

    INDEX2COORDS(i, xRank, xShapePtr, xCoords);
    COORDS2INDEX(xRank, xStridePtr, xCoords, xOffset);
    INDEX2COORDS(i, zRank, zShapePtr, zCoords);
    COORDS2INDEX(zRank, zStridePtr, zCoords, zOffset);

// Apply the function using extraParams (this will be handled in the wrapper function)
// For now, using a placeholder
    z[zOffset] = x[xOffset]; // This will be replaced with the actual lambda function call
  }
}

// ----------- Indexed Lambda Operations ----------------

template <typename T>
SD_KERNEL void applyIndexedLambdaKernel(const void* vx, const sd::LongType* xShapeInfo,
                                        void* vz, const sd::LongType* zShapeInfo,
                                        void* vextraParams) {
// Cast input and output pointers
  auto x = reinterpret_cast<const T*>(vx);
  auto z = reinterpret_cast<T*>(vz);
  auto extraParams = reinterpret_cast<void*>(vextraParams);

// Cache shape information for x buffer
  __shared__ sd::LongType length;
  __shared__ sd::LongType xRank;
  __shared__ const sd::LongType* xShapePtr;
  __shared__ const sd::LongType* xStridePtr;

// Cache shape information for z buffer
  __shared__ sd::LongType zRank;
  __shared__ const sd::LongType* zShapePtr;
  __shared__ const sd::LongType* zStridePtr;

  if (threadIdx.x == 0) {
    length = shape::length(xShapeInfo);

// Cache x shape information
    xRank = shape::rank(xShapeInfo);
    xShapePtr = shape::shapeOf(xShapeInfo);
    xStridePtr = shape::stride(xShapeInfo);

// Cache z shape information
    zRank = shape::rank(zShapeInfo);
    zShapePtr = shape::shapeOf(zShapeInfo);
    zStridePtr = shape::stride(zShapeInfo);
  }
  __syncthreads();

  auto tid = blockIdx.x * blockDim.x + threadIdx.x;
  int totalThreads = gridDim.x * blockDim.x;

  for (sd::LongType i = tid; i < length; i += totalThreads) {
    sd::LongType xCoords[SD_MAX_RANK];
    sd::LongType zCoords[SD_MAX_RANK];
    sd::LongType xOffset;
    sd::LongType zOffset;

    INDEX2COORDS(i, xRank, xShapePtr, xCoords);
    COORDS2INDEX(xRank, xStridePtr, xCoords, xOffset);
    INDEX2COORDS(i, zRank, zShapePtr, zCoords);
    COORDS2INDEX(zRank, zStridePtr, zCoords, zOffset);

// Apply the indexed function - placeholder for actual lambda call
    z[zOffset] = x[xOffset]; // This will be replaced with the actual indexed lambda function call
  }
}

// ----------- Pairwise Lambda Operations ----------------

template <typename T>
SD_KERNEL void applyPairwiseLambdaKernel(const void* vx, const sd::LongType* xShapeInfo,
                                         const void* vy, const sd::LongType* yShapeInfo,
                                         void* vz, const sd::LongType* zShapeInfo,
                                         void* vextraParams, bool isScalar) {
// Cast input and output pointers
  auto x = reinterpret_cast<const T*>(vx);
  auto y = reinterpret_cast<const T*>(vy);
  auto z = reinterpret_cast<T*>(vz);
  auto extraParams = reinterpret_cast<void*>(vextraParams);

// Cache shape information
  __shared__ sd::LongType length;
  __shared__ sd::LongType xRank;
  __shared__ sd::LongType yRank;
  __shared__ sd::LongType zRank;
  __shared__ const sd::LongType* xShapePtr;
  __shared__ const sd::LongType* yShapePtr;
  __shared__ const sd::LongType* zShapePtr;
  __shared__ const sd::LongType* xStridePtr;
  __shared__ const sd::LongType* yStridePtr;
  __shared__ const sd::LongType* zStridePtr;
  __shared__ T scalarValue;
  __shared__ sd::LongType yOffset0;

  if (threadIdx.x == 0) {
    length = shape::length(xShapeInfo);

// Cache shape information
    xRank = shape::rank(xShapeInfo);
    yRank = shape::rank(yShapeInfo);
    zRank = shape::rank(zShapeInfo);

    xShapePtr = shape::shapeOf(xShapeInfo);
    yShapePtr = shape::shapeOf(yShapeInfo);
    zShapePtr = shape::shapeOf(zShapeInfo);

    xStridePtr = shape::stride(xShapeInfo);
    yStridePtr = shape::stride(yShapeInfo);
    zStridePtr = shape::stride(zShapeInfo);

    if (isScalar) {
      sd::LongType yCoords[SD_MAX_RANK];
      for (int i = 0; i < yRank; i++) {
        yCoords[i] = 0;
      }
      COORDS2INDEX(yRank, yStridePtr, yCoords, yOffset0);
      scalarValue = y[yOffset0];
    }
  }
  __syncthreads();

  auto tid = blockIdx.x * blockDim.x + threadIdx.x;
  int totalThreads = gridDim.x * blockDim.x;

  for (sd::LongType i = tid; i < length; i += totalThreads) {
    sd::LongType xCoords[SD_MAX_RANK];
    sd::LongType yCoords[SD_MAX_RANK];
    sd::LongType zCoords[SD_MAX_RANK];
    sd::LongType xOffset;
    sd::LongType yOffset;
    sd::LongType zOffset;

    INDEX2COORDS(i, xRank, xShapePtr, xCoords);
    COORDS2INDEX(xRank, xStridePtr, xCoords, xOffset);
    INDEX2COORDS(i, zRank, zShapePtr, zCoords);
    COORDS2INDEX(zRank, zStridePtr, zCoords, zOffset);

    if (isScalar) {
// Apply the pairwise function with scalar - placeholder
      z[zOffset] = x[xOffset]; // Will be replaced with actual function call using scalarValue
    } else {
      INDEX2COORDS(i, yRank, yShapePtr, yCoords);
      COORDS2INDEX(yRank, yStridePtr, yCoords, yOffset);

      // Apply the pairwise function - placeholder
      z[zOffset] = x[xOffset]; // Will be replaced with actual function call using y[yOffset]
    }
  }
}

// ----------- Indexed Pairwise Lambda Operations ----------------

template <typename T>
SD_KERNEL void applyIndexedPairwiseLambdaKernel(const void* vx, const sd::LongType* xShapeInfo,
                                                const void* vy, const sd::LongType* yShapeInfo,
                                                void* vz, const sd::LongType* zShapeInfo,
                                                void* vextraParams) {
// Cast input and output pointers
  auto x = reinterpret_cast<const T*>(vx);
  auto y = reinterpret_cast<const T*>(vy);
  auto z = reinterpret_cast<T*>(vz);
  auto extraParams = reinterpret_cast<void*>(vextraParams);

// Cache shape information
  __shared__ sd::LongType length;
  __shared__ sd::LongType xRank;
  __shared__ sd::LongType yRank;
  __shared__ sd::LongType zRank;
  __shared__ const sd::LongType* xShapePtr;
  __shared__ const sd::LongType* yShapePtr;
  __shared__ const sd::LongType* zShapePtr;
  __shared__ const sd::LongType* xStridePtr;
  __shared__ const sd::LongType* yStridePtr;
  __shared__ const sd::LongType* zStridePtr;

  if (threadIdx.x == 0) {
    length = shape::length(xShapeInfo);

// Cache shape information
    xRank = shape::rank(xShapeInfo);
    yRank = shape::rank(yShapeInfo);
    zRank = shape::rank(zShapeInfo);

    xShapePtr = shape::shapeOf(xShapeInfo);
    yShapePtr = shape::shapeOf(yShapeInfo);
    zShapePtr = shape::shapeOf(zShapeInfo);

    xStridePtr = shape::stride(xShapeInfo);
    yStridePtr = shape::stride(yShapeInfo);
    zStridePtr = shape::stride(zShapeInfo);
  }
  __syncthreads();

  auto tid = blockIdx.x * blockDim.x + threadIdx.x;
  int totalThreads = gridDim.x * blockDim.x;

  for (sd::LongType i = tid; i < length; i += totalThreads) {
    sd::LongType xCoords[SD_MAX_RANK];
    sd::LongType yCoords[SD_MAX_RANK];
    sd::LongType zCoords[SD_MAX_RANK];
    sd::LongType xOffset;
    sd::LongType yOffset;
    sd::LongType zOffset;

    INDEX2COORDS(i, xRank, xShapePtr, xCoords);
    COORDS2INDEX(xRank, xStridePtr, xCoords, xOffset);
    INDEX2COORDS(i, yRank, yShapePtr, yCoords);
    COORDS2INDEX(yRank, yStridePtr, yCoords, yOffset);
    INDEX2COORDS(i, zRank, zShapePtr, zCoords);
    COORDS2INDEX(zRank, zStridePtr, zCoords, zOffset);

// Apply the indexed pairwise function - placeholder
    z[zOffset] = x[xOffset]; // Will be replaced with actual function call
  }
}

// ----------- Triplewise Lambda Operations ----------------

template <typename T>
SD_KERNEL void applyTriplewiseLambdaKernel(const void* vx, const sd::LongType* xShapeInfo,
                                           const void* vy, const sd::LongType* yShapeInfo,
                                           const void* vt, const sd::LongType* tShapeInfo,
                                           void* vz, const sd::LongType* zShapeInfo,
                                           void* vextraParams) {
// Cast input and output pointers
  auto x = reinterpret_cast<const T*>(vx);
  auto y = reinterpret_cast<const T*>(vy);
  auto t = reinterpret_cast<const T*>(vt);
  auto z = reinterpret_cast<T*>(vz);
  auto extraParams = reinterpret_cast<void*>(vextraParams);

// Cache shape information
  __shared__ sd::LongType length;
  __shared__ sd::LongType xRank;
  __shared__ sd::LongType yRank;
  __shared__ sd::LongType tRank;
  __shared__ sd::LongType zRank;
  __shared__ const sd::LongType* xShapePtr;
  __shared__ const sd::LongType* yShapePtr;
  __shared__ const sd::LongType* tShapePtr;
  __shared__ const sd::LongType* zShapePtr;
  __shared__ const sd::LongType* xStridePtr;
  __shared__ const sd::LongType* yStridePtr;
  __shared__ const sd::LongType* tStridePtr;
  __shared__ const sd::LongType* zStridePtr;

  if (threadIdx.x == 0) {
    length = shape::length(xShapeInfo);

// Cache shape information
    xRank = shape::rank(xShapeInfo);
    yRank = shape::rank(yShapeInfo);
    tRank = shape::rank(tShapeInfo);
    zRank = shape::rank(zShapeInfo);

    xShapePtr = shape::shapeOf(xShapeInfo);
    yShapePtr = shape::shapeOf(yShapeInfo);
    tShapePtr = shape::shapeOf(tShapeInfo);
    zShapePtr = shape::shapeOf(zShapeInfo);

    xStridePtr = shape::stride(xShapeInfo);
    yStridePtr = shape::stride(yShapeInfo);
    tStridePtr = shape::stride(tShapeInfo);
    zStridePtr = shape::stride(zShapeInfo);
  }
  __syncthreads();

  auto tid = blockIdx.x * blockDim.x + threadIdx.x;
  int totalThreads = gridDim.x * blockDim.x;

  for (sd::LongType i = tid; i < length; i += totalThreads) {
    sd::LongType xCoords[SD_MAX_RANK];
    sd::LongType yCoords[SD_MAX_RANK];
    sd::LongType tCoords[SD_MAX_RANK];
    sd::LongType zCoords[SD_MAX_RANK];
    sd::LongType xOffset;
    sd::LongType yOffset;
    sd::LongType tOffset;
    sd::LongType zOffset;

    INDEX2COORDS(i, xRank, xShapePtr, xCoords);
    COORDS2INDEX(xRank, xStridePtr, xCoords, xOffset);
    INDEX2COORDS(i, yRank, yShapePtr, yCoords);
    COORDS2INDEX(yRank, yStridePtr, yCoords, yOffset);
    INDEX2COORDS(i, tRank, tShapePtr, tCoords);
    COORDS2INDEX(tRank, tStridePtr, tCoords, tOffset);
    INDEX2COORDS(i, zRank, zShapePtr, zCoords);
    COORDS2INDEX(zRank, zStridePtr, zCoords, zOffset);

// Apply the triplewise function - placeholder
    z[zOffset] = x[xOffset]; // Will be replaced with actual function call
  }
}

// ---------------------- Wrapper functions -----------------------

// Helper class for CUDA Lambda operations
template <typename T>
class NDArrayLambdaCuda {
 public:
  static int constexpr LAMBDA_THREADS = 256;
  static int constexpr LAMBDA_BLOCKS = 512;

// Unary lambda wrapper
  static void executeLambda(cudaStream_t* stream, const void* x, const sd::LongType* xShapeInfo,
                            void* z, const sd::LongType* zShapeInfo, void* extraParams) {
    if(stream == nullptr) {
      THROW_EXCEPTION("executeLambda: Stream must not be nullptr!");
    }
    dim3 launchDims(LAMBDA_BLOCKS, LAMBDA_THREADS, 1024);
    applyLambdaKernel<T><<<launchDims.x, launchDims.y, launchDims.z, *stream>>>(
        x, xShapeInfo, z, zShapeInfo, extraParams);
    sd::DebugHelper::checkErrorCode(stream, "NDArrayLambdaCuda::executeLambda failed");
  }

// Indexed lambda wrapper
  static void executeIndexedLambda(cudaStream_t* stream, const void* x, const sd::LongType* xShapeInfo,
                                   void* z, const sd::LongType* zShapeInfo, void* extraParams) {
    if(stream == nullptr) {
      THROW_EXCEPTION("executeIndexedLambda: Stream must not be nullptr!");
    }
    dim3 launchDims(LAMBDA_BLOCKS, LAMBDA_THREADS, 1024);
    applyIndexedLambdaKernel<T><<<launchDims.x, launchDims.y, launchDims.z, *stream>>>(
        x, xShapeInfo, z, zShapeInfo, extraParams);
    sd::DebugHelper::checkErrorCode(stream, "NDArrayLambdaCuda::executeIndexedLambda failed");
  }

// Pairwise lambda wrapper
  static void executePairwiseLambda(cudaStream_t* stream, const void* x, const sd::LongType* xShapeInfo,
                                    const void* y, const sd::LongType* yShapeInfo,
                                    void* z, const sd::LongType* zShapeInfo, void* extraParams, bool isScalar) {
    dim3 launchDims(LAMBDA_BLOCKS, LAMBDA_THREADS, 1024);
    if(stream == nullptr) {
      THROW_EXCEPTION("executePairwiseLambda: Stream must not be nullptr!");
    }
    applyPairwiseLambdaKernel<T><<<launchDims.x, launchDims.y, launchDims.z, *stream>>>(
        x, xShapeInfo, y, yShapeInfo, z, zShapeInfo, extraParams, isScalar);
    sd::DebugHelper::checkErrorCode(stream, "NDArrayLambdaCuda::executePairwiseLambda failed");
  }

// Indexed pairwise lambda wrapper
  static void executeIndexedPairwiseLambda(cudaStream_t* stream, const void* x, const sd::LongType* xShapeInfo,
                                           const void* y, const sd::LongType* yShapeInfo,
                                           void* z, const sd::LongType* zShapeInfo, void* extraParams) {
    dim3 launchDims(LAMBDA_BLOCKS, LAMBDA_THREADS, 1024);
    applyIndexedPairwiseLambdaKernel<T><<<launchDims.x, launchDims.y, launchDims.z, *stream>>>(
        x, xShapeInfo, y, yShapeInfo, z, zShapeInfo, extraParams);
    sd::DebugHelper::checkErrorCode(stream, "NDArrayLambdaCuda::executeIndexedPairwiseLambda failed");
  }

// Triplewise lambda wrapper
  static void executeTriplewiseLambda(cudaStream_t* stream, const void* x, const sd::LongType* xShapeInfo,
                                      const void* y, const sd::LongType* yShapeInfo,
                                      const void* t, const sd::LongType* tShapeInfo,
                                      void* z, const sd::LongType* zShapeInfo, void* extraParams) {
    if(stream == nullptr) {
      THROW_EXCEPTION("executeTriplewiseLambda: Stream must not be nullptr!");
    }
    dim3 launchDims(LAMBDA_BLOCKS, LAMBDA_THREADS, 1024);
    applyTriplewiseLambdaKernel<T><<<launchDims.x, launchDims.y, launchDims.z, *stream>>>(
        x, xShapeInfo, y, yShapeInfo, t, tShapeInfo, z, zShapeInfo, extraParams);
    sd::DebugHelper::checkErrorCode(stream, "NDArrayLambdaCuda::executeTriplewiseLambda failed");
  }
};



// Implementation of the NDArray Lambda methods for CUDA
template <typename T>
SD_LIB_EXPORT void NDArray::applyLambda(std::function<T(T)>& func, NDArray* target) {
// Validate types
  if (dataType() != DataTypeUtils::fromT<T>())
    THROW_EXCEPTION(
        "NDArray::applyLambdaCuda<T> method: wrong template parameter T, its type should be the same as type of this "
        "array!");
  if (dataType() != target->dataType())
    THROW_EXCEPTION("NDArray::applyLambdaCuda<T> method: types of this and target array should match!");

// Get device pointers and stream
  auto stream = LaunchContext::defaultContext()->getCudaStream(); // Get the CUDA stream
  auto x = this->specialBuffer();
  auto z = target->specialBuffer();
  auto xShapeInfo = this->specialShapeInfo();
  auto zShapeInfo = target->specialShapeInfo();

// Create and set up extraParams
  void* extraParams = nullptr; // This would hold the function pointer for the lambda

// Execute the CUDA kernel
  NDArrayLambdaCuda<T>::executeLambda(stream, x, xShapeInfo, z, zShapeInfo, extraParams);
}

template <typename T>
SD_LIB_EXPORT void NDArray::applyIndexedLambda(std::function<T(sd::LongType, T)>& func, NDArray* target) {
// Validate types
  if (dataType() != DataTypeUtils::fromT<T>())
    THROW_EXCEPTION(
        "NDArray::applyIndexedLambdaCuda<T> method: wrong template parameter T, its type should be the same as type of "
        "this array!");
  if (dataType() != target->dataType())
    THROW_EXCEPTION("NDArray::applyIndexedLambdaCuda<T> method: types of this and target array should match!");

// Get device pointers and stream
  auto stream = LaunchContext::defaultContext()->getCudaStream(); // Get the CUDA stream
  auto x = this->specialBuffer();
  auto z = target->specialBuffer();
  auto xShapeInfo = this->specialShapeInfo();
  auto zShapeInfo = target->specialShapeInfo();

// Create and set up extraParams
  void* extraParams = nullptr; // This would hold the function pointer for the lambda

// Execute the CUDA kernel
  NDArrayLambdaCuda<T>::executeIndexedLambda(stream, x, xShapeInfo, z, zShapeInfo, extraParams);
}

template <typename T>
SD_LIB_EXPORT void NDArray::applyPairwiseLambda(NDArray* other,  std::function<T(T, T)>& func,
                                                NDArray* target) {
// Validate types
  if (dataType() != DataTypeUtils::fromT<T>())
    THROW_EXCEPTION(
        "NDArray::applyPairwiseLambdaCuda<T> method: wrong template parameter T, its type should be the same as type of "
        "this array!");
  if (dataType() != other->dataType() || dataType() != target->dataType())
    THROW_EXCEPTION(
        "NDArray::applyPairwiseLambdaCuda<T> method: all three arrays (this, other, target) must have the same type!");

// Check for scalar or same length
  bool isScalar = other->isScalar();
  if (this->lengthOf() != other->lengthOf() && !this->isScalar() && !isScalar) {
    THROW_EXCEPTION("applyPairwiseLambdaCuda requires both operands to have the same shape or one to be a scalar");
  }

// Get device pointers and stream
  auto stream = LaunchContext::defaultContext()->getCudaStream(); // Get the CUDA stream
  auto x = this->specialBuffer();
  auto y = other->specialBuffer();
  auto z = target->specialBuffer();
  auto xShapeInfo = this->specialShapeInfo();
  auto yShapeInfo = other->specialShapeInfo();
  auto zShapeInfo = target->specialShapeInfo();

// Create and set up extraParams
  void* extraParams = nullptr; // This would hold the function pointer for the lambda

// Execute the CUDA kernel
  NDArrayLambdaCuda<T>::executePairwiseLambda(stream, x, xShapeInfo, y, yShapeInfo, z, zShapeInfo, extraParams, isScalar);
}

template <typename T>
SD_LIB_EXPORT void NDArray::applyIndexedPairwiseLambda(NDArray* other,  std::function<T(sd::LongType, T, T)>& func,
                                                       NDArray* target) {
// Validate types
  if (dataType() != DataTypeUtils::fromT<T>())
    THROW_EXCEPTION(
        "NDArray::applyIndexedPairwiseLambdaCuda<T> method: wrong template parameter T, its type should be the same as "
        "type of this array!");
  if (dataType() != target->dataType())
    THROW_EXCEPTION(
        "NDArray::applyIndexedPairwiseLambdaCuda<T> method: types of this and target array should match!");
  if (this->lengthOf() != other->lengthOf()) {
    THROW_EXCEPTION("applyIndexedPairwiseLambdaCuda requires both operands to have the same shape");
  }

// Get device pointers and stream
  auto stream = LaunchContext::defaultContext()->getCudaStream(); // Get the CUDA stream
  auto x = this->specialBuffer();
  auto y = other->specialBuffer();
  auto z = target->specialBuffer();
  auto xShapeInfo = this->specialShapeInfo();
  auto yShapeInfo = other->specialShapeInfo();
  auto zShapeInfo = target->specialShapeInfo();

// Create and set up extraParams
  void* extraParams = nullptr; // This would hold the function pointer for the lambda

// Execute the CUDA kernel
  NDArrayLambdaCuda<T>::executeIndexedPairwiseLambda(stream, x, xShapeInfo, y, yShapeInfo, z, zShapeInfo, extraParams);
}

template <typename T>
SD_LIB_EXPORT void NDArray::applyTriplewiseLambda(NDArray* second, NDArray* third,
                                                  std::function<T(T, T, T)>& func, NDArray* target) {
// Validate types
  if (dataType() != DataTypeUtils::fromT<T>())
    THROW_EXCEPTION(
        "NDArray::applyTriplewiseLambdaCuda<T> method: wrong template parameter T, its type should be the same as type of "
        "this array!");
  if (dataType() != second->dataType() || dataType() != third->dataType() || dataType() != target->dataType())
    THROW_EXCEPTION(
        "NDArray::applyTriplewiseLambdaCuda<T> method: all four arrays (this, second, third, target) should have the "
        "same type!");

  if (this->lengthOf() != second->lengthOf() || this->lengthOf() != third->lengthOf() || !this->isSameShape(second) ||
      !this->isSameShape(third)) {
    std::string errorMessage;
    errorMessage += "applyTriplewiseLambdaCuda requires all operands to have the same shape\n";
    errorMessage += "this shape: " + ShapeUtils::shapeAsString(this->shapeInfo()) + "\n";
    errorMessage += "second shape: " + ShapeUtils::shapeAsString(second->shapeInfo()) + "\n";
    errorMessage += "third shape: " + ShapeUtils::shapeAsString(third->shapeInfo()) + "\n";
    errorMessage += "target shape: " + ShapeUtils::shapeAsString(target->shapeInfo()) + "\n";
    THROW_EXCEPTION(errorMessage.c_str());
  }

// Get device pointers and stream
  auto stream = LaunchContext::defaultContext()->getCudaStream(); // Get the CUDA stream
  auto x = this->specialBuffer();
  auto y = second->specialBuffer();
  auto t = third->specialBuffer();
  auto z = target->specialBuffer();
  auto xShapeInfo = this->specialShapeInfo();
  auto yShapeInfo = second->specialShapeInfo();
  auto tShapeInfo = third->specialShapeInfo();
  auto zShapeInfo = target->specialShapeInfo();

// Create and set up extraParams
  void* extraParams = nullptr; // This would hold the function pointer for the lambda

// Execute the CUDA kernel
  NDArrayLambdaCuda<T>::executeTriplewiseLambda(stream, x, xShapeInfo, y, yShapeInfo, t, tShapeInfo,
                                                z, zShapeInfo, extraParams);
}


#define INSTANTIATE_LAMBDA_METHODS(T) template SD_LIB_EXPORT void NDArray::applyLambda( std::function<GET_SECOND(T)(GET_SECOND(T))>& func, NDArray* target);
ITERATE_LIST((SD_COMMON_TYPES),INSTANTIATE_LAMBDA_METHODS);

#define INSTANTIATE_LAMBDA_METHODS_INDEXED(T) template SD_LIB_EXPORT void NDArray::applyIndexedLambda( std::function<GET_SECOND(T)(sd::LongType, GET_SECOND(T))>& func, NDArray* target);
ITERATE_LIST((SD_COMMON_TYPES),INSTANTIATE_LAMBDA_METHODS_INDEXED);

#define INSTANTIATE_LAMBDA_METHODS_PAIRWISE(T) template SD_LIB_EXPORT void NDArray::applyPairwiseLambda(NDArray* other,  std::function<GET_SECOND(T)(GET_SECOND(T), GET_SECOND(T))>& func, NDArray* target);
ITERATE_LIST((SD_COMMON_TYPES),INSTANTIATE_LAMBDA_METHODS_PAIRWISE);

#define INSTANTIATE_LAMBDA_METHODS_INDEX_PAIR(T) template SD_LIB_EXPORT void NDArray::applyIndexedPairwiseLambda(NDArray* other,  std::function<GET_SECOND(T)(sd::LongType, GET_SECOND(T), GET_SECOND(T))>& func, NDArray* target);
ITERATE_LIST((SD_COMMON_TYPES),INSTANTIATE_LAMBDA_METHODS_INDEX_PAIR);

#define INSTANTIATE_LAMBDA_METHODS_TRIPLE(T) template void NDArray::applyTriplewiseLambda(NDArray* second, NDArray* third,  std::function<GET_SECOND(T)(GET_SECOND(T), GET_SECOND(T), GET_SECOND(T))>& func, NDArray* target);
ITERATE_LIST((SD_COMMON_TYPES),INSTANTIATE_LAMBDA_METHODS_TRIPLE);
} // namespace sd