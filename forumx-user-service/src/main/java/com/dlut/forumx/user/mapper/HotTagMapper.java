package com.dlut.forumx.user.mapper;

import com.dlut.forumx.user.entity.HotTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HotTagMapper {
	int insert(HotTag hotTag);

	int update(HotTag hotTag);

	int incrUserCount(@Param("tagName") String tagName);

	int incrPostCount(@Param("tagName") String tagName);

	List<HotTag> selectHotTags(@Param("limit") int limit);

	HotTag selectByTagName(@Param("tagName") String tagName);
}
