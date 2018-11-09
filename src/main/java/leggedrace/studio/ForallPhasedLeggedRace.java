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

import static edu.wustl.cse231s.rice.habanero.Habanero.forall;
import static edu.wustl.cse231s.rice.habanero.Habanero.phased;
import static edu.wustl.cse231s.rice.habanero.Habanero.phaserNext;

import edu.rice.hj.api.SuspendableException;
import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.rice.habanero.options.PhasedEmptyOption;
import edu.wustl.cse231s.rice.habanero.options.PhasedOption;
import leggedrace.core.LeggedRace;
import leggedrace.core.Participant;

/**
 * @author David Qiu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class ForallPhasedLeggedRace implements LeggedRace {
	/**
	 * The parallel solution to the simulated legged race which does make use of
	 * phasers, but only next() barriers.
	 */
	@Override
	public void takeSteps(Participant[] participants, int stepCount) throws SuspendableException {
		PhasedEmptyOption phasedEmpty = new PhasedEmptyOption();
		PhasedEmptyOption phasedEmpty2 = new PhasedEmptyOption();
		forall(phasedEmpty,0,participants.length,(i)->{
			for(int j=0;j<stepCount;j++) {
				participants[i].takeStep(j);
				phaserNext();
			}
			
		});
		
	}
}
