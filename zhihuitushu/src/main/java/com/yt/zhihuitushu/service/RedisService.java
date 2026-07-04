package com.yt.zhihuitushu.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void set(String key, String value, long timeout, TimeUnit unit);

    String get(String key);

    Boolean exists(String key);

    void delete(String key);

    Long increment(String key);

    void setExpire(String key, long timeout, TimeUnit unit);
}