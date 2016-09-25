/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.serialize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;
import schn27.arinc429tester.Arinc429Word;
import schn27.arinc429tester.Convertor;
import schn27.arinc429tester.LabelFilter;
import schn27.arinc429tester.NumberSystem;
import schn27.arinc429tester.Sequence;
import schn27.arinc429tester.SequenceItem;
import schn27.arinc429tester.TimeMarkedArinc429Word;

/**
 *
 * @author AVIA
 */
public final class Serializer {
	public static void saveConfig(String fileName, Config config) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)))) { 			
			Jsoner.serialize(getJsonObjectForConfig(config), bw);
		} catch (IOException ex) {
			Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static Config loadConfig(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) { 			
			return readConfigFromJsonObject((JsonObject)Jsoner.deserialize(br));
		} catch (IOException | DeserializationException ex) {
			Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return null;
	}
	
	public static void saveState(String fileName, State state) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)))) {
			Jsoner.serialize(getJsonObjectForState(state), bw);
		} catch (IOException ex) {
			Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static State loadState(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) { 			
			return readStateFromJsonObject((JsonObject)Jsoner.deserialize(br));
		} catch (IOException | DeserializationException ex) {
			Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return null;
	}

	private static JsonObject getJsonObjectForConfig(Config config) {
		JsonObject json = new JsonObject();
		json.put("labelfilter", getJsonObjectFor(config.labelFilter));
			
		List<Integer> noSdiWordsList = new ArrayList<>();
		config.noSdiWords.stream().forEach(noSdiWordsList::add);
		json.put("nosdiwords", noSdiWordsList);
			
		json.put("convertors", getJsonObjectFor(config.convertors));
		
		return json;
	}
	
	private static JsonObject getJsonObjectFor(LabelFilter labelFilter) {
		JsonObject o = new JsonObject();
		
		o.put("includemode", labelFilter.includeMode);
		o.put("numbersystem", labelFilter.numberSystem.toString());
		
		List<Integer> labels = new ArrayList<>();
		
		for (int label : labelFilter.getLabels()) {
			if (label >= 0) {
				labels.add(label);
			}
		}
		
		o.put("labels", labels);
		
		return o;
	}
	
	private static Map<String, JsonObject> getJsonObjectFor(Map<Integer, Convertor> convertors) {
		Map<String, JsonObject> o = new HashMap<>();
		
		convertors.forEach((id, convertor) -> {
			JsonObject convertorJson = new JsonObject();
			convertorJson.put("type", convertor.type.toString());
			convertorJson.put("signbit", convertor.signBit);
			convertorJson.put("hibit", convertor.hiBit);
			convertorJson.put("lobit", convertor.loBit);
			convertorJson.put("hibitvalue", convertor.hiBitValue);
			o.put(Integer.toString(id), convertorJson);
		});
		
		return o;
	}	

	private static Config readConfigFromJsonObject(JsonObject json) {
		LabelFilter labelFilter = readLabelFilterFromJsonObject((JsonObject)json.get("labelfilter"));
		
		BitSet noSdiWords = new BitSet(256);
		((JsonArray)json.get("nosdiwords")).forEach((label) -> noSdiWords.set(((BigDecimal)label).intValue()));

		Map<Integer, Convertor> convertors = new HashMap<>();
		Map<String, JsonObject> jsonMap = json.getMap("convertors");
		jsonMap.forEach((label, convertorJson) -> convertors.put(
				Integer.parseInt(label), 
				readConvertorFromJsonObject(Integer.parseInt(label), convertorJson)));
		
		return new Config(labelFilter, noSdiWords, convertors);
	}
	
	private static LabelFilter readLabelFilterFromJsonObject(JsonObject json) {
		LabelFilter labelFilter = new LabelFilter();
		labelFilter.includeMode = json.getBooleanOrDefault("includemode", false);
		
		switch (json.getStringOrDefault("numbersystem", "OCT").toUpperCase()) {
		case "BIN":
			labelFilter.numberSystem = NumberSystem.BIN;
			break;
		case "OCT":
			labelFilter.numberSystem = NumberSystem.OCT;
			break;
		case "DEC":
			labelFilter.numberSystem = NumberSystem.DEC;
			break;
		case "HEX":
			labelFilter.numberSystem = NumberSystem.HEX;
			break;
		default:
			labelFilter.numberSystem = NumberSystem.OCT;
		}
		
		Collection<BigDecimal> labels = json.getCollection("labels");
		int[] labelsArray = new int[labels.size()];
		int[] i = {0};	// hack to use it as index in lambda
		labels.stream().forEach((value) -> labelsArray[i[0]++] = value.intValue());
		
		labelFilter.setLabels(labelsArray);
		
		return labelFilter;
	}
	
	private static Convertor readConvertorFromJsonObject(int label, JsonObject json) {
		Convertor.Type type = Convertor.Type.COMPLEMENT;
		switch (json.getStringOrDefault("type", "COMPLEMENT").toUpperCase()) {
		case "COMPLEMENT":
			type = Convertor.Type.COMPLEMENT;
			break;
		case "DIRECT":
			type = Convertor.Type.DIRECT;
			break;
		case "BCD":
			type = Convertor.Type.BCD;
			break;
		}
		
		int signBit = json.getIntegerOrDefault("signbit", -1);
		int hiBit = json.getIntegerOrDefault("hibit", 29);
		int loBit = json.getIntegerOrDefault("lobit", 11);
		double hiBitValue = json.getDoubleOrDefault("hibitvalue", 1);
		
		return new Convertor(label, type, signBit, hiBit, loBit, hiBitValue);
	}	

	private static JsonObject getJsonObjectForState(State state) {
		JsonObject json = new JsonObject();
		json.put("config", getJsonObjectForConfig(state.config));
		
		List<JsonObject> sequenceJson = new ArrayList<>();
		
		state.sequence.getList().forEach((item) -> {
			JsonObject itemJson = new JsonObject();
			itemJson.put("time", item.tmword.timemark.toEpochMilli());
			itemJson.put("word", item.tmword.word.raw & 0xFFFFFFFFL);
			itemJson.put("period", item.period);
			itemJson.put("minperiod", item.minPeriod);
			itemJson.put("maxperiod", item.maxPeriod);
			sequenceJson.add(itemJson);
		});
		
		json.put("sequence", sequenceJson);
		
		return json;
	}
	
	private static State readStateFromJsonObject(JsonObject json) {
		Config config = readConfigFromJsonObject((JsonObject)json.get("config"));

		Sequence sequence = new Sequence();
		((JsonArray)json.get("sequence")).forEach((itemJson) -> {
			int period = ((JsonObject)itemJson).getIntegerOrDefault("period", -1);
			int minPeriod = ((JsonObject)itemJson).getIntegerOrDefault("minperiod", -1);
			int maxPeriod = ((JsonObject)itemJson).getIntegerOrDefault("maxperiod", -1);
			Instant time = Instant.ofEpochMilli(((JsonObject)itemJson).getLongOrDefault("time", 0));
			int word = ((JsonObject)itemJson).getLongOrDefault("word", 0).intValue();
			sequence.put(new SequenceItem(
					new TimeMarkedArinc429Word(time, new Arinc429Word(word)), 
					period, minPeriod, maxPeriod));
		});

		return new State(sequence, config);
	}

}
