/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package lambda.demo.asyncfinish;

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;
import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static edu.wustl.cse231s.sleep.SleepUtils.sleepRandom;

import edu.rice.hj.api.HjSuspendable;
import edu.rice.hj.api.SuspendableException;

class ApplesPrinter implements HjSuspendable {
	@Override
	public void run() throws SuspendableException {
		sleepRandom(1_000);
		System.out.println("-apples");
	}
}

class OrangesPrinter implements HjSuspendable {
	@Override
	public void run() throws SuspendableException {
		sleepRandom(1_000);
		System.out.println("--oranges");
	}
}

class BananasPrinter implements HjSuspendable {
	@Override
	public void run() throws SuspendableException {
		sleepRandom(1_000);
		System.out.println("---bananas");
	}
}

class MangoesPrinter implements HjSuspendable {
	@Override
	public void run() throws SuspendableException {
		sleepRandom(1_000);
		System.out.println("----mangoes");
	}
}

class FruitInAnyOrderPrinter implements HjSuspendable {
	@Override
	public void run() throws SuspendableException {
		async(new ApplesPrinter());
		async(new OrangesPrinter());
		async(new BananasPrinter());
		async(new MangoesPrinter());
	}
}

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NamedClassesAsyncAndFinishExample {
	public static void main(final String[] args) {
		launchHabaneroApp(() -> {
			System.out.println("start");

			finish(new FruitInAnyOrderPrinter());

			System.out.println("stop");
		});
	}
}
