package com.heima.model.behavior.vos;

import lombok.Data;

@Data
public class ArticleInfoVo {
    private Boolean islike;
    private Boolean isunlike;
    private Boolean iscollect;
    private Boolean isfollow;

    public ArticleInfoVo(boolean islike, boolean isunlike, boolean iscollect, boolean isfollow){
        this.islike = islike;
        this.isunlike = isunlike;
        this.iscollect = iscollect;
        this.isfollow = isfollow;
    }
}
