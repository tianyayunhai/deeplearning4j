/* ******************************************************************************
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
//
// @author AbdelRauf
//
#include <ops/declarable/helpers/reductions.h>

#include <vector>

namespace sd {
namespace ops {
namespace helpers {

//////////////////////////////////////////////////////////////////////////
template <typename X, typename Z>
void variance_(NDArray& input, NDArray& output, const std::vector<sd::LongType>& dimensions, bool biasCorrected);

template <typename X, typename Z>
void standardDeviation_(NDArray& input, NDArray& output, const std::vector<sd::LongType>& dimensions, bool biasCorrected);
//////////////////////////////////////////////////////////////////////////
void variance(NDArray& input, NDArray& output, const std::vector<LongType>& dimensions, bool biasCorrected) {
  BUILD_DOUBLE_SELECTOR(input.dataType(), output.dataType(), variance_, (input, output, dimensions, biasCorrected),
                        SD_COMMON_TYPES, SD_FLOAT_TYPES);
}

void standardDeviation(NDArray& input, NDArray& output, const std::vector<LongType>& dimensions, bool biasCorrected) {
  BUILD_DOUBLE_SELECTOR(input.dataType(), output.dataType(), standardDeviation_,
                        (input, output, dimensions, biasCorrected), SD_COMMON_TYPES, SD_FLOAT_TYPES);
}

}  // namespace helpers
}  // namespace ops
}  // namespace sd
