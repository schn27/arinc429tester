/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.serialize;

import java.io.IOException;
import java.io.Writer;
import java.util.BitSet;
import java.util.Map;
import org.json.simple.Jsonable;
import schn27.arinc429tester.Convertor;
import schn27.arinc429tester.LabelFilter;

/**
 *
 * @author AVIA
 */
public class Config implements Jsonable {
	public Config(LabelFilter labelFilter, BitSet noSdiWords, Map<Integer, Convertor> convertors) {
		this.labelFilter = labelFilter;
		this.noSdiWords = noSdiWords;
		this.convertors = convertors;
	}

	@Override
	public String toJson() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void toJson(Writer writer) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public LabelFilter labelFilter;
	public BitSet noSdiWords;
	public Map<Integer, Convertor> convertors;
}
