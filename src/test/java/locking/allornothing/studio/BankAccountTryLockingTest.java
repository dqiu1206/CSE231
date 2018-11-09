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
package locking.allornothing.studio;

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;
import static edu.wustl.cse231s.rice.habanero.Habanero.launchHabaneroApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import locking.allornothing.studio.BankAccountTryLocking;
import locking.core.AccountWithLock;
import locking.core.DefaultAccount;

/**
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * 
 *         {@link BankAccountTryLocking#transferMoney(AccountWithLock, AccountWithLock, int)}
 */
public class BankAccountTryLockingTest {
	@Test(timeout = 5000)
	public void test() {
		DefaultAccount[] accounts = new DefaultAccount[20];
		for (int i = 0; i < 20; ++i) {
			accounts[i] = new DefaultAccount(i, 5000);
		}
		for (int i = 0; i < 20; ++i) {
			for (int j = 19; j >= 0; --j) {
				if (i != j) {
					int iBalance = accounts[i].getBalance();
					int jBalance = accounts[j].getBalance();
					assertTrue("The transfer should have been successful, but it was not",
							BankAccountTryLocking.transferMoney(accounts[i], accounts[j], 50));
					assertEquals("The sender's balance did not update properly", iBalance - 50,
							accounts[i].getBalance());
					assertEquals("The recipient's balance did not update properly", jBalance + 50,
							accounts[j].getBalance());
				} else {
					assertFalse(
							"The transfer should not have been successful as the sender and recipient are the same people,"
									+ "but it was",
							BankAccountTryLocking.transferMoney(accounts[i], accounts[j], 50));
				}
			}
		}
		for (int i = 0; i < 20; ++i) {
			assertEquals("The account balances did not update properly", 5000, accounts[i].getBalance());
		}
		launchHabaneroApp(() -> {
			finish(() -> {
				for (int i = 0; i < 1000; ++i) {
					async(() -> {
						assertTrue("The transfer failed to work asynchronously",
								BankAccountTryLocking.transferMoney(accounts[0], accounts[1], 2));
						assertTrue("The transfer failed to work asynchronously",
								BankAccountTryLocking.transferMoney(accounts[1], accounts[0], 1));
					});
				}
			});
		});
		assertEquals("The account balances did not update properly in parallel", 4000, accounts[0].getBalance());
		assertEquals("The account balances did not update properly in parallel", 6000, accounts[1].getBalance());
	}
}
