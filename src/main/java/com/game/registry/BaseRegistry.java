package com.game.registry;


import com.game.utils.Log;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <pre>
 * 注册表基类
 * </pre>
 *
 * @author yuxuan
 * @time 2020-11-26 16:35
 */
public abstract class BaseRegistry<K, V> {

    /**
     * 实例缓存<Key,Instance>
     */
    private final Map<K, V> instancePool = new ConcurrentHashMap<>();


    public void init() {
    }

    /**
     * <pre>
     * 创建实例
     * </pre>
     *
     * @param key    实例key
     * @param clazz  实例的类
     * @param params 实例 的类构造器参数类数组
     * @param args   实例关联的类构造器参数 数据
     */
    @SuppressWarnings("unchecked")
    public final V create(K key, Class<? extends V> clazz, Class<?>[] params, Object... args) {
        V instance = null;
        try {
            Constructor<?> contr = clazz.getConstructor(params);
            if (contr != null) {
                instance = (V) contr.newInstance(args);
            }
        } catch (Exception e) {
            Log.error("创建实例异常,clazz:" + clazz.getSimpleName(), e);
        }
        if (instance == null) {
            return null;
        }
        instancePool.put(key, instance);
        return instance;
    }

    /**
     * <pre>
     * 放入实例
     * </pre>
     */
    public V put(K key, V value) {
        if (key == null) {
            return null;
        }
        return instancePool.put(key, value);
    }

    /**
     * <pre>
     * 获取实例
     * </pre>
     *
     * @param key 实例key
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return instancePool.get(key);
    }

    /**
     * <pre>
     * 是否包含key
     * </pre>
     */
    public boolean contains(K key) {
        if (key == null) {
            return false;
        }
        return instancePool.containsKey(key);
    }

    /**
     * <pre>
     * 移除实例
     * </pre>
     *
     * @param key 实例key
     */
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        return instancePool.remove(key);
    }

    /**
     * <pre>
     * 清空所有实例
     * </pre>
     */
    public void clearAll() {
        instancePool.clear();
    }

    protected Map<K, V> getInstancePool() {
        return instancePool;
    }

}
