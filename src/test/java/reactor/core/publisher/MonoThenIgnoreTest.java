/*
 * Copyright (c) 2011-2016 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.core.publisher;

import java.time.Duration;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.test.StepVerifier;

public class MonoThenIgnoreTest {

	@Test
	public void normal() {
		StepVerifier.create(Mono.just(1)
		                        .thenEmpty(Flux.empty()))
		            .expectComplete();
	}

	Publisher<Void> scenario(){
		return Mono.just(1)
		    .thenEmpty(Mono.delay(Duration.ofSeconds(123)).then());
	}
	@Test
	public void normalTime() {
		StepVerifier.withVirtualTime(this::scenario)
		            .thenAwait(Duration.ofSeconds(123))
		            .expectComplete();
	}
}