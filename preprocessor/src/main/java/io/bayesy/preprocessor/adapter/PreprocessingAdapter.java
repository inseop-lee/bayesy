package io.bayesy.preprocessor.adapter;

import com.google.common.base.Strings;

import io.bayesy.preprocessing.PreprocessingEntity;

/**
 * @author inseop.lee
 *
 */
abstract public class PreprocessingAdapter {

    protected PreprocessingEntity rawData;
    protected String matchedKey;
    

    protected PreprocessingAdapter(PreprocessingEntity rawData, String matchedKey) {
	this.rawData = rawData;
	this.matchedKey = matchedKey;
    }
    
    protected PreprocessingAdapter(PreprocessingEntity rawData) {
	this.rawData = rawData;
    }
    
    public final String getMatchedKey() {
	return Strings.isNullOrEmpty(matchedKey) ? rawData.getKey() : matchedKey;
    }
    
    abstract public String getPreprossessingValue();
}
