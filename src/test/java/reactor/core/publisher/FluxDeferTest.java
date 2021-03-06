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

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.test.subscriber.AssertSubscriber;

public class FluxDeferTest {

	@Test(expected = NullPointerException.class)
	public void supplierNull() {
		Flux.<Integer>defer(null);
	}

	@Test
	public void deferAssigned() {
		Supplier<Publisher<?>> defer = () -> null;
		Assert.assertTrue(new FluxDefer<>(defer).upstream().equals(defer));
	}

	@Test
	public void supplierReturnsNull() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create();

		Flux.<Integer>defer(() -> null).subscribe(ts);

		ts.assertNoValues()
		  .assertNotComplete()
		  .assertError(NullPointerException.class);
	}

	@Test
	public void supplierThrows() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create();

		Flux.<Integer>defer(() -> {
			throw new RuntimeException("forced failure");
		}).subscribe(ts);

		ts.assertNoValues()
		  .assertNotComplete()
		  .assertError(RuntimeException.class)
		  .assertErrorMessage("forced failure");
	}

	@Test
	public void normal() {
		AssertSubscriber<Integer> ts = AssertSubscriber.create();

		Flux.defer(() -> Flux.just(1)).subscribe(ts);

		ts.assertValues(1)
		  .assertNoError()
		  .assertComplete();
	}
}
