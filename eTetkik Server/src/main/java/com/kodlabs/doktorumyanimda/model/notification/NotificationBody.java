package com.kodlabs.doktorumyanimda.model.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationBody {
    public String app_id;
    public List<String> include_player_ids = new ArrayList<>();
    public Map<String,Object> data = new HashMap<>();
    public Map<String,String> contents = new HashMap<>();
    public Map<String,String> headings = new HashMap<>();
    public List<NotificationButton> buttons = new ArrayList<>();
}
