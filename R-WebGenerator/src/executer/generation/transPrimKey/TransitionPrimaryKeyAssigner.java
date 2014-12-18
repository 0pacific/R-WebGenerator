package executer.generation.transPrimKey;

import pageElement.PageElement;
import transition.Transition;
import transition.TransitionManager;
import webPage.WebPage;
import webPage.WebPageManager;

public class TransitionPrimaryKeyAssigner {
	/*
	 * SUMMARY :
	 * ‘S‘JˆÚ‚ÉAåƒL[‚ğ—^‚¦‚é
	 */
	public void assignKeys() {
		// ‘JˆÚ‚ÌåƒL[‚Í1‚©‚ç‡‚É—^‚¦‚Ä‚¢‚­
		int key = 1;
	
		TransitionManager transManager = TransitionManager.getInstance();

		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			trans.transKey = key;
			key++;
		}
	}
}
