package com.gyc.community.service;

import com.gyc.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        //先判断某个用户是否为我点赞了，如果点赞了，再点一次就是取消点赞
        if(isMember){
            redisTemplate.opsForSet().remove(entityLikeKey,userId);

        }else{
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }
    }

    //查询实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体点赞的状态
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if(isMember) return 1;
        return 0;
    }
}