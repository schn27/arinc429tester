/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

import java.time.Instant;
import java.util.List;

/**
 *
 * @author amalikov
 */
public class FilteredSequence extends Sequence {
	public FilteredSequence(Sequence original) {
		super();
		this.original = original;
		this.filter = null;
	}

	@Override
	public void put(Instant timemark, Arinc429Word word) {
		original.put(timemark, word);
		if (filter.isAccepted(word.getLabel() & 0xFF)) {
			super.put(timemark, word);
		}
	}
	
	@Override
	public void clear() {
		original.clear();
		super.clear();
	}
	
	public void setFilter(LabelFilter filter) {
		this.filter = filter;
		super.clear();
		List<TimeMarkedArinc429Word> list = original.getList();
		for (TimeMarkedArinc429Word w : list) {
			if (filter.isAccepted(w.word.getLabel() & 0xFF)) {
				super.put(w.timemark, w.word);
			}
		}
	}
	
	private final Sequence original;
	private LabelFilter filter;
}
