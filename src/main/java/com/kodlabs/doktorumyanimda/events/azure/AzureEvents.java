package com.kodlabs.doktorumyanimda.events.azure;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.kodlabs.doktorumyanimda.events.IEvents;
import com.kodlabs.doktorumyanimda.events.IEventsEntity;

public class AzureEvents implements IEvents {
    private final AzureEventsEntity entity;
    private final static String connectionString = "DefaultEndpointsProtocol=https;AccountName=doktorumsa;AccountKey=qMFb12QlX/YTWTHBxLWhsbbf+GKJmmGE34C34qZCmI61/rTMHmTo8V6iFsIEQqaaeq6VPwrm/xpHmna9ma54Aw==;EndpointSuffix=core.windows.net";
    public AzureEvents(IEventsEntity entity){
        this.entity = (AzureEventsEntity) entity;

    }
    @Override
    public void insert() {
        if(entity == null){
            return;
        }
        TableClient tableClient = new TableClientBuilder()
                .connectionString(connectionString) // or use any of the other authentication methods
                .tableName(entity.getTableName().getValue())
                .buildClient();
        String partitionKey = entity.getPartitionKey();
        String rowKey = entity.getRowKey();
        TableEntity tableEntity = new TableEntity(partitionKey, rowKey).setProperties(entity.getInfo());
        tableClient.upsertEntity(tableEntity);
    }
}
