package com.kodlabs.doktorumyanimda.events.azure;

import com.kodlabs.doktorumyanimda.events.EventsType;
import com.kodlabs.doktorumyanimda.events.IEventsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AzureEventsEntity implements IEventsEntity {
    private EventsType tableName;
    private String partitionKey;
    private String rowKey;
    private Map<String, Object> info;

    public AzureEventsEntity(EventsType tableName, String partitionKey, Map<String, Object> info) {
        this.tableName = tableName;
        this.partitionKey = partitionKey;
        this.info = info;
        this.rowKey = UUID.randomUUID().toString();
    }
}
