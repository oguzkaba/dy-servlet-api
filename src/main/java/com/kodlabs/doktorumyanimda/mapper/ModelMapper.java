package com.kodlabs.doktorumyanimda.mapper;

import org.modelmapper.convention.MatchingStrategies;

public class ModelMapper {
    private static org.modelmapper.ModelMapper mapper = null;
    public static org.modelmapper.ModelMapper getInstance(){
        if(mapper == null){
            mapper = new org.modelmapper.ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        }
        return mapper;
    }
}
