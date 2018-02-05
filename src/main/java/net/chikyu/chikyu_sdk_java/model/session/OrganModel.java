package net.chikyu.chikyu_sdk_java.model.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu_sdk_java.model.ApiDataModel;

public class OrganModel extends ApiDataModel {
    @JsonProperty(value = "organ_id")
    public Integer organId;

    @JsonProperty(value = "organ_name")
    public String organName;
}
