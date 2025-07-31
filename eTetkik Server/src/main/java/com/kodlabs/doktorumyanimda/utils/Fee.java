package com.kodlabs.doktorumyanimda.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class Fee {
    public final static List<Object> peakTime = IntStream.rangeClosed(1, 30).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    public final static List<Object> peakBigTime = IntStream.of(180, 365).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    public final static List<Integer> feeList = IntStream.rangeClosed(0, 5000)
            .filter(x -> {
                if(x <= 600){
                    return x % 50==0;
                }else if(x == 750){
                    return true;
                }else if(x >= 1000 && x <= 2000){
                    return x % 250 == 0;
                }
                return x % 1000 == 0;
            }).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
}
