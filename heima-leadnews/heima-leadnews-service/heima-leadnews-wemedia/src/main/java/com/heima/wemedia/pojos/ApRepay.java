package com.heima.wemedia.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("ap_repays")
public class ApRepay implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String commentId;
    private Integer authorId;
    private String authorName;
    private Integer type;
    private String content;
    private String image;
    private Integer likes;
    private Integer reply;
    private Short flag;
    private Double longitude;
    private Double latitude;
    private String address;
    private Long createdTime;
    private Long updatedTime;
    private Integer operation;
}
