package com.peipao.qdl.guide.service;


import com.peipao.qdl.guide.model.Guide;
import com.peipao.qdl.guide.model.GuideItem;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Meteor.wu
 * @since 2018/1/11 11:42
 */
@Service
public class GuideUtilService {

    /*
     * Guide to map
     */
    public Map<String, Object> object2Map(Guide guide) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", guide.getGuideId());
        map.put("guideId", guide.getGuideId());
        map.put("title", guide.getTitle());
        map.put("keyword", guide.getKeyword());
        return map;
    }

    /*
     * GuideItem to map
     */
    public Map<String, Object> object2Map(GuideItem guideItem) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", guideItem.getGuideItemId());
        map.put("guideItemId", guideItem.getGuideItemId());
        map.put("itemTitle", guideItem.getItemTitle());
        map.put("desc", guideItem.getDesc());
        map.put("type", guideItem.getType());
        map.put("packageName", guideItem.getPackageName() == null ? "" : guideItem.getPackageName());
        map.put("path", guideItem.getPath() == null ? "" : guideItem.getPath());
        map.put("imageURL", guideItem.getImageURL() == null ? "" : guideItem.getImageURL());
        return map;
    }
}
