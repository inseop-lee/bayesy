package io.bayesy.preprocessor.adapter;

import io.bayesy.preprocessing.PreprocessingEntity;

public class DefaultPreprocessingAdapter extends PreprocessingAdapter {

    protected DefaultPreprocessingAdapter(PreprocessingEntity rawData) {
	super(rawData);
    }
    

    @Override
    public String getPreprossessingValue() {
	return rawData.getValue();
    }

}
