package com.kodlabs.doktorumyanimda.videoChat;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
