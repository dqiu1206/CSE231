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

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;
import edu.wustl.cse231s.NotYetImplementedException;
import locking.core.AccountUtils;
import locking.core.AccountWithLock;

/**
 * @author David Qiu
 * @author Ben Choi (benjaminchoi@wustl.edu)
 */

public class BankAccountTryLocking {

	private BankAccountTryLocking() {
		throw new IntendedForStaticAccessOnlyError();
	}

	/**
	 * Transfers a given amount of money from the sender to the recipient. This
	 * method should be made thread-safe with the use of the ReentrantLock
	 * associated with the sender and recipient. Make use of the
	 * checkBalanceAndTransfer method given to you to quickly check if there is
	 * enough money in the sender's account to transfer the given amount to the
	 * recipient. The method should return true if the transfer is successful, false
	 * otherwise.
	 * 
	 * Note: the idea for this method was taken from Java Concurrency in Practice
	 * (Chapter 13, pg. 280) and was slightly modified to fit our purposes
	 * 
	 * @param sender,
	 *            the AccountWithLock instance of the sender
	 * @param recipient,
	 *            the AccountWithLock instance of the recipient
	 * @param amount,
	 *            the amount the sender is trying to send to the recipient
	 * @return true if the transfer is successful, false otherwise
	 */
	public static boolean transferMoney(AccountWithLock sender, AccountWithLock recipient, int amount) {
		boolean found = false;
		int senderHash = sender.getUniqueIdNumber();
		int recipientHash = recipient.getUniqueIdNumber();
		if(senderHash<recipientHash) {
			while(!found) {
				if(sender.getLock().tryLock()) {
					try {
						if(recipient.getLock().tryLock()) {
							try {
								if(AccountUtils.checkBalanceAndTransfer(sender, recipient, amount)) {
									return true;
								}
							}
							finally {
								recipient.getLock().unlock();
							}
							found = true;
						}
						
					}
					finally {
						sender.getLock().unlock();
					}
				}
			}
		}
		if(senderHash>recipientHash) {
			while(!found) {
				if(recipient.getLock().tryLock()) {
					try {
						if(sender.getLock().tryLock()) {
							try {
								if(AccountUtils.checkBalanceAndTransfer(sender, recipient, amount)) {
									return true;
								}
							}
							finally {
								sender.getLock().unlock();
							}
							found = true;
						}
						
					}
					finally {
						recipient.getLock().unlock();
					}
				}
			}
		}
		return false;
		
	}

}
