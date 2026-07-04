package com.yt.zhihuitushu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yt.zhihuitushu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE users SET failed_attempts = failed_attempts + 1 WHERE id = #{id}")
    void incrementFailedAttempts(@Param("id") Long id);

    @Update("UPDATE users SET failed_attempts = 0, locked_at = NULL WHERE id = #{id}")
    void resetFailedAttempts(@Param("id") Long id);

    @Update("UPDATE users SET locked_at = #{lockedAt} WHERE id = #{id}")
    void lockUser(@Param("id") Long id, @Param("lockedAt") java.time.LocalDateTime lockedAt);
}