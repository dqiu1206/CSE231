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
package leggedrace.studio;

import static edu.wustl.cse231s.rice.habanero.Habanero.async;
import static edu.wustl.cse231s.rice.habanero.Habanero.finish;
import static edu.wustl.cse231s.rice.habanero.Habanero.forall;
import static edu.wustl.cse231s.rice.habanero.Habanero.newPhaser;
import static edu.wustl.cse231s.rice.habanero.Habanero.phaserNext;
import static edu.wustl.cse231s.rice.habanero.Habanero.phased;

import edu.rice.hj.api.HjPhaser;
import edu.rice.hj.api.HjPhaserMode;
import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.rice.habanero.options.PhasedOption;
import leggedrace.core.LeggedRace;
import leggedrace.core.Participant;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ForallPhasedPointToPointLeggedRace implements LeggedRace {
	/**
	 * The parallel solution to the simulated legged race which does make use of phasers,
	 * including an array of phasers and the next() barrier.
	 */
	@Override
	public void takeSteps(Participant[] participants, int stepCount) throws SuspendableException {
		finish(()->{
			HjPhaser[] phaserArray = new HjPhaser[participants.length];
			for(int i=0;i<phaserArray.length;i++) {
				phaserArray[i] = newPhaser(HjPhaserMode.DEFAULT_MODE);
			}
			for(int i=0;i<participants.length;i++) {
				final int ii = i;
				async(phased(phaserArray[getPartnerIndex(i)].inMode(HjPhaserMode.WAIT),
						phaserArray[i].inMode(HjPhaserMode.SIG)),()->{
					for(int j=0;j<stepCount;j++) {
						participants[ii].takeStep(j);
						phaserNext();
					}
	
				});
			}
			
			
		});

	}

	/**
	 * Used in the FORALL_PHASED_POINT_TO_POINT implementation in order to access the correct
	 * phaser in the array of phasers. Every participant has an associated partner index
	 * which waits for a signal from the participant index. This method gets the value of the
	 * partner index from the participant index.
	 * @param index, more specifically that of the participant
	 * @return partner index, given the index of the associated participant
	 */
	private static int getPartnerIndex(int index) {
		boolean isOddIndex = (index & 0x1) == 0x1;
		if (isOddIndex) {
			return index - 1;
		} else {
			return index + 1;
		}
	}
}
