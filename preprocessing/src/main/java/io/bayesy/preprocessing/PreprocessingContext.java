package io.bayesy.preprocessing;

import java.util.Map;

import io.bayesy.preprocessing.codec.PreprocessingCodec;

public class PreprocessingContext {
    private Map<String, PreprocessingCodec> matches;

    public Map<String, String> preprocessingData(Map<String, String> evidences) {
	// TODO Auto-generated method stub
	return evidences;
    }
    
    public void addMatch(String nodeName, PreprocessingCodec codec) {
	matches.put(nodeName, codec);
    }
}
